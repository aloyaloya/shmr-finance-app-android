package com.example.shmr_finance_app_android.presentation.feature.categories.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.di.daggerViewModel
import com.example.shmr_finance_app_android.presentation.feature.categories.component.SearchTextField
import com.example.shmr_finance_app_android.presentation.feature.categories.model.CategoryUiModel
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesScreenViewModel
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesUiState
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig
import com.example.shmr_finance_app_android.presentation.shared.components.EmptyState
import com.example.shmr_finance_app_android.presentation.shared.components.ErrorState
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.shared.components.LoadingState

@Composable
fun CategoriesScreen(
    viewModel: CategoriesScreenViewModel = daggerViewModel(),
    updateConfigState: (ScreenConfig) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchRequest by viewModel.searchRequest.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        updateConfigState(
            ScreenConfig(
                topBarConfig = TopBarConfig(titleResId = R.string.categories_screen_title)
            )
        )
    }

    Column(Modifier.fillMaxSize()) {
        SearchTextField(
            value = searchRequest,
            onChange = { viewModel.onChangeSearchRequest(it) },
            onActionClick = { viewModel.updateState() }
        )
        HorizontalDivider()
        when (uiState) {
            CategoriesUiState.Loading -> LoadingState()
            is CategoriesUiState.Error -> ErrorState(
                messageResId = (uiState as CategoriesUiState.Error).messageResId,
                onRetry = viewModel::init
            )

            CategoriesUiState.Empty -> EmptyState(
                messageResId = R.string.no_categories_found
            )

            CategoriesUiState.SearchEmpty -> EmptyState(
                messageResId = R.string.empty_filtered_categories
            )

            is CategoriesUiState.Content -> CategoriesSuccessState(
                categories = (uiState as CategoriesUiState.Content).categories
            )
        }
    }
}

@Composable
private fun CategoriesSuccessState(
    categories: List<CategoryUiModel>
) {
    LazyColumn {
        items(categories, key = { category -> category.id }) { category ->
            ListItemCard(
                modifier = Modifier
                    .clickable { } // Переход на экран Мои расходы
                    .height(70.dp),
                item = category.toListItem()
            )
        }
    }
}
