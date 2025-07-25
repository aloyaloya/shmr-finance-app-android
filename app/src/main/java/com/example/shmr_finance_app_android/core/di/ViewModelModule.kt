package com.example.shmr_finance_app_android.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shmr_finance_app_android.presentation.feature.analytic.viewmodel.AnalyticViewModel
import com.example.shmr_finance_app_android.presentation.feature.auth.AuthViewModel
import com.example.shmr_finance_app_android.presentation.feature.balance.viewmodels.BalanceScreenViewModel
import com.example.shmr_finance_app_android.presentation.feature.balance.viewmodels.BalanceUpdateScreenViewModel
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesScreenViewModel
import com.example.shmr_finance_app_android.presentation.feature.history.viewmodel.HistoryScreenViewModel
import com.example.shmr_finance_app_android.presentation.feature.main.viewmodel.MainScreenViewModel
import com.example.shmr_finance_app_android.presentation.feature.settings.viewmodel.SettingsScreenViewModel
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionCreationViewModel
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionTodayViewModel
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionUpdateViewModel
import com.example.shmr_finance_app_android.presentation.shared.theme.ThemeViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

/**
 * Модуль Dagger, отвечающий за связывание ViewModel'ей с их ключами:
 * - Использует аннотацию [@IntoMap] и кастомный ключ [ViewModelKey]
 * - Позволяет Dagger предоставлять ViewModel по запросу через [ViewModelProvider.Factory]
 * - Содержит биндинг всех ViewModel, используемых в приложении
 */
@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BalanceScreenViewModel::class)
    fun bindBalanceViewModel(vm: BalanceScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BalanceUpdateScreenViewModel::class)
    fun bindBalanceUpdateViewModel(vm: BalanceUpdateScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesScreenViewModel::class)
    fun bindCategoriesViewModel(vm: CategoriesScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryScreenViewModel::class)
    fun bindHistoryViewModel(vm: HistoryScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AnalyticViewModel::class)
    fun bindAnalyticViewModel(vm: AnalyticViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainScreenViewModel::class)
    fun bindMainViewModel(vm: MainScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsScreenViewModel::class)
    fun bindSettingsViewModel(vm: SettingsScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TransactionTodayViewModel::class)
    fun bindTransactionTodayViewModel(vm: TransactionTodayViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TransactionCreationViewModel::class)
    fun bindTransactionCreationViewModel(vm: TransactionCreationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TransactionUpdateViewModel::class)
    fun bindTransactionUpdateViewModel(vm: TransactionUpdateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ThemeViewModel::class)
    fun bindThemeViewModel(vm: ThemeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    fun bindAuthViewModel(vm: AuthViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)