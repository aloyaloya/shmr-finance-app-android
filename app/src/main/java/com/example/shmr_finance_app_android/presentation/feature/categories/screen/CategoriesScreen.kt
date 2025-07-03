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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.navigation.Route
import com.example.shmr_finance_app_android.presentation.feature.categories.component.SearchTextField
import com.example.shmr_finance_app_android.presentation.feature.categories.model.IncomeCategoryUiModel
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesScreenState
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesScreenViewModel
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig
import com.example.shmr_finance_app_android.presentation.shared.components.EmptyState
import com.example.shmr_finance_app_android.presentation.shared.components.ErrorState
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.shared.components.LoadingState

@Composable
fun CategoriesScreen(
    viewModel: CategoriesScreenViewModel = hiltViewModel(),
    updateConfigState: (ScreenConfig) -> Unit
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val searchRequest by viewModel.searchRequest.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        updateConfigState(
            ScreenConfig(
                route = Route.Root.Categories.path,
                topBarConfig = TopBarConfig(
                    titleResId = R.string.categories_screen_title
                )
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
        when (state) {
            is CategoriesScreenState.Loading -> LoadingState()
            is CategoriesScreenState.Error -> ErrorState(
                messageResId = (state as CategoriesScreenState.Error).messageResId,
                onRetry = (state as CategoriesScreenState.Error).retryAction
            )

            is CategoriesScreenState.Empty -> EmptyState(
                messageResId = R.string.no_categories_found
            )

            is CategoriesScreenState.SearchEmpty -> EmptyState(
                messageResId = R.string.empty_filtered_categories
            )

            is CategoriesScreenState.Success -> CategoriesSuccessState(
                categories = (state as CategoriesScreenState.Success).categories
            )
        }
    }
}

@Composable
private fun CategoriesSuccessState(
    categories: List<IncomeCategoryUiModel>
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
