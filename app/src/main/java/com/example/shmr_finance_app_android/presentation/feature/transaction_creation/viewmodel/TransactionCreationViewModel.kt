package com.example.shmr_finance_app_android.presentation.feature.transaction_creation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.utils.Constants
import com.example.shmr_finance_app_android.core.utils.formatHumanDateToIso
import com.example.shmr_finance_app_android.core.utils.formatLongToHumanDate
import com.example.shmr_finance_app_android.core.utils.getCurrentDateIso
import com.example.shmr_finance_app_android.core.utils.getCurrentTime
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.model.AccountDomain
import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import com.example.shmr_finance_app_android.domain.usecases.CreateTransactionUseCase
import com.example.shmr_finance_app_android.domain.usecases.GetAccountUseCase
import com.example.shmr_finance_app_android.domain.usecases.GetCategoriesByTypeUseCase
import com.example.shmr_finance_app_android.presentation.feature.categories.mapper.CategoryToCategoryUiMapper
import com.example.shmr_finance_app_android.presentation.feature.categories.model.CategoryUiModel
import com.example.shmr_finance_app_android.presentation.feature.transaction_creation.mapper.AccountToBalanceBriefMapper
import com.example.shmr_finance_app_android.presentation.feature.transaction_creation.model.BalanceBriefUiModel
import com.example.shmr_finance_app_android.presentation.feature.transaction_creation.viewmodel.TransactionCreationScreenState.Active
import com.example.shmr_finance_app_android.presentation.feature.transaction_creation.viewmodel.TransactionCreationScreenState.Error
import com.example.shmr_finance_app_android.presentation.feature.transaction_creation.viewmodel.TransactionCreationScreenState.Loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Состояния экрана Создания транзакции с явным разделением:
 * - [Loading] Начальное состояние загрузки
 * - [Active] Состояние при котором ведется создание транзакции
 * - [Error] Состояние ошибки с:
 *  * - Локализованным сообщением ([messageResId])
 *  * - Коллбэком повторной попытки ([retryAction])
 */
sealed interface TransactionCreationScreenState {
    data object Loading : TransactionCreationScreenState
    data class Error(
        val messageResId: Int,
        val retryAction: () -> Unit
    ) : TransactionCreationScreenState

    data class Active(val transaction: TransactionState) : TransactionCreationScreenState
}

@RequiresApi(Build.VERSION_CODES.O)
class TransactionCreationViewModel @Inject constructor(
    private val getAccount: GetAccountUseCase,
    private val accountMapper: AccountToBalanceBriefMapper,
    private val getCategoriesByType: GetCategoriesByTypeUseCase,
    private val categoryMapper: CategoryToCategoryUiMapper,
    private val createTransaction: CreateTransactionUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<TransactionCreationScreenState>(Loading)
    val screenState: StateFlow<TransactionCreationScreenState> = _screenState.asStateFlow()

    private val transactionState = MutableStateFlow(TransactionState())

    private val _availableCategories = MutableStateFlow<List<CategoryUiModel>>(emptyList())
    val availableCategories: SharedFlow<List<CategoryUiModel>> = _availableCategories.asStateFlow()

    private val accountId = MutableStateFlow(Constants.TEST_ACCOUNT_ID)

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    /** Флаг видимости диалога выбора даты */
    private val _datePickerModalVisible = MutableStateFlow(false)
    val datePickerModalVisible: StateFlow<Boolean> = _datePickerModalVisible.asStateFlow()

    /** Флаг видимости диалога выбора времени */
    private val _timePickerModalVisible = MutableStateFlow(false)
    val timePickerModalVisible: StateFlow<Boolean> = _timePickerModalVisible.asStateFlow()

    private val _categoriesSelectionSheetVisible = MutableStateFlow(false)
    val categoriesSelectionSheetVisible: StateFlow<Boolean> =
        _categoriesSelectionSheetVisible.asStateFlow()

    /** Флаг видимости всплывающего уведомления */
    private val _snackBarVisible = MutableStateFlow(false)
    val snackBarVisible: StateFlow<Boolean> = _snackBarVisible.asStateFlow()

    private val _snackBarMessageId = MutableStateFlow(0)
    val snackBarMessage: StateFlow<Int> = _snackBarMessageId.asStateFlow()

    fun initialize() {
        loadData()
    }

    private fun loadData() {
        _screenState.value = Loading
        viewModelScope.launch(Dispatchers.IO) {
            val balanceDeferred = async { getAccount(accountId.value) }
            val categoriesDeferred = async {
                getCategoriesByType(transactionState.value.isIncome)
            }

            val balanceResult = balanceDeferred.await()
            val categoriesResult = categoriesDeferred.await()

            handleBalanceResult(balanceResult)
            handleCategoriesResult(categoriesResult)
        }
    }

    /**
     * Обрабатывает результат запроса, преобразуя:
     * - Успех -> [BalanceBriefUiModel] через маппер
     * - Ошибку -> Сообщение об ошибке
     */
    private fun handleBalanceResult(result: Result<AccountDomain>) {
        result
            .onSuccess { data -> handleBalanceSuccess(accountMapper.map(data)) }
            .onFailure { error -> handleError(error) }
    }

    /** Обновляет состояние при успешной загрузке */
    private fun handleBalanceSuccess(data: BalanceBriefUiModel) {
        transactionState.update { current ->
            current.copy(
                balance = data.name,
                currencySymbol = data.currencySymbol
            )
        }

        _screenState.value = Active(transaction = transactionState.value)
    }

    private fun handleCategoriesResult(result: Result<List<CategoryDomain>>) {
        result
            .onSuccess { data -> handleCategoriesSuccess(data.map { categoryMapper.map(it) }) }
            .onFailure { error -> handleError(error) }
    }

    /** Сохроняет данные при успешной загрузке */
    private fun handleCategoriesSuccess(data: List<CategoryUiModel>) {
        _availableCategories.value = data

        transactionState.update { current ->
            current.copy(
                categoryName = data.first().name,
                categoryId = data.first().id
            )
        }

        _screenState.value = Active(transaction = transactionState.value)
    }

    /** Обрабатывает ошибки */
    private fun handleError(error: Throwable) {
        val messageResId = (error as? AppError)?.messageResId ?: R.string.unknown_error
        _screenState.value = Error(
            messageResId = messageResId,
            retryAction = { loadData() }
        )
    }

    fun onFieldChanged(field: TransactionField, value: Any) {
        transactionState.update { current ->
            when (field) {
                TransactionField.BALANCE -> current.copy(balance = value as String)
                TransactionField.CATEGORY -> current.copy(
                    categoryName = (value as CategoryUiModel).name,
                    categoryId = value.id
                )

                TransactionField.AMOUNT -> current.copy(amount = value as String)
                TransactionField.COMMENT -> current.copy(comment = value as String)
                TransactionField.DATE -> current.copy(date = formatLongToHumanDate(value as Long))
                TransactionField.TIME -> current.copy(
                    time = (value as Pair<Int, Int>).toTimeString()
                )
            }
        }

        if (_screenState.value is Active) {
            _screenState.value = Active(transactionState.value)
        }
    }

    fun createTransaction() {
        _screenState.value = Loading
        viewModelScope.launch(Dispatchers.IO) {
            val transactionData = transactionState.value
            handleCreateTransactionResult(
                createTransaction(
                    accountId = accountId.value,
                    categoryId = transactionData.categoryId,
                    amount = transactionData.amount,
                    transactionDate = formatHumanDateToIso(transactionData.date),
                    transactionTime = transactionData.time,
                    comment = transactionData.comment
                )
            )
        }
    }

    private fun handleCreateTransactionResult(result: Result<TransactionDomain>) {
        result
            .onSuccess { _isSuccess.value = true }
            .onFailure {
                showSnackBar(R.string.error_message)
                _screenState.value = Active(transactionState.value)
            }
    }

    /** Валидация введенных данных о транзакции */
    fun validateTransactionData(): Boolean {
        try {
            transactionState.value.amount.toInt()
        } catch (e: NumberFormatException) {
            showSnackBar(R.string.balance_amount_error_message)
            return false
        }

        return true
    }

    fun setTransactionsType(isIncome: Boolean) {
        transactionState.update { current ->
            current.copy(isIncome = isIncome)
        }
    }

    /** Обрабатывает открытие CategoriesBottomSheet */
    fun showCategoryBottomSheet() {
        _categoriesSelectionSheetVisible.value = true
    }

    /** Обрабатывает закрытие CategoriesBottomSheet */
    fun hideCategoryBottomSheet() {
        _categoriesSelectionSheetVisible.value = false
    }

    /** Обрабатывает открытие DatePickerModal */
    fun showDatePickerModal() {
        _datePickerModalVisible.value = true
    }

    /** Обрабатывает закрытие DatePickerModal */
    fun hideDatePickerModal() {
        _datePickerModalVisible.value = false
    }

    /** Обрабатывает открытие TimePickerModal */
    fun showTimePickerModal() {
        _timePickerModalVisible.value = true
    }

    /** Обрабатывает закрытие TimePickerModal */
    fun hideTimePickerModal() {
        _timePickerModalVisible.value = false
    }

    /** Обрабатывает показ ErrorSnackbar */
    private fun showSnackBar(message: Int) {
        if (!_snackBarVisible.value) {
            _snackBarVisible.value = true
            _snackBarMessageId.value = message
            viewModelScope.launch {
                delay(4000)
                dismissSnackBar()
            }
        }
    }

    /** Обрабатывает скрытие ErrorSnackbar */
    fun dismissSnackBar() {
        _snackBarVisible.value = false
    }
}

private fun Pair<Int, Int>.toTimeString(): String {
    val (hours, minutes) = this
    return "${
        hours.toString()
            .padStart(2, '0')
    }:${minutes.toString().padStart(2, '0')}"
}

@RequiresApi(Build.VERSION_CODES.O)
data class TransactionState(
    val balance: String = "",
    val categoryName: String = "",
    val categoryId: Int = 0,
    val amount: String = "",
    val currencySymbol: String = "$",
    val comment: String = "",
    val date: String = getCurrentDateIso(),
    val time: String = getCurrentTime(),
    val isIncome: Boolean = true
)

enum class TransactionField {
    BALANCE,
    CATEGORY,
    AMOUNT,
    COMMENT,
    DATE,
    TIME
}