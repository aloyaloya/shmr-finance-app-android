package com.example.shmr_finance_app_android.presentation.feature.categories.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig
import com.example.shmr_finance_app_android.core.navigation.Route
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesScreenState
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesScreenViewModel
import com.example.shmr_finance_app_android.presentation.feature.categories.component.SearchTextField
import com.example.shmr_finance_app_android.presentation.feature.categories.model.IncomeCategoryUiModel

@Composable
fun CategoriesScreen(
    viewModel: CategoriesScreenViewModel = hiltViewModel(),
    updateConfigState: (ScreenConfig) -> Unit
) {
    val state by viewModel.screenState.collectAsState()
    val searchRequest by viewModel.searchRequest.collectAsState()

    LaunchedEffect(updateConfigState) {
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
            onActionClick = {  } // Кнопка поиска
        )
        HorizontalDivider()
        when (state) {
            is CategoriesScreenState.Loading -> CategoriesLoadingState()
            is CategoriesScreenState.Error -> CategoriesErrorState(
                messageResId = (state as CategoriesScreenState.Error).messageResId,
                onRetry = (state as CategoriesScreenState.Error).retryAction
            )
            is CategoriesScreenState.Empty -> CategoriesEmptyState()
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

@Composable
private fun CategoriesLoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
    }
}

@Composable
private fun CategoriesErrorState(
    messageResId: Int,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(messageResId),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.large_spacer)))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Composable
private fun CategoriesEmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_categories_found),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}