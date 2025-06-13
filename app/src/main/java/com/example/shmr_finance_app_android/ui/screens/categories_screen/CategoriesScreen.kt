package com.example.shmr_finance_app_android.ui.screens.categories_screen

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.data.mapper.toListItem
import com.example.shmr_finance_app_android.data.model.domain.Category
import com.example.shmr_finance_app_android.ui.components.ListItemCard
import com.example.shmr_finance_app_android.ui.screens.categories_screen.components.SearchTextField
import com.example.shmr_finance_app_android.ui.viewmodels.CategoriesScreenState
import com.example.shmr_finance_app_android.ui.viewmodels.CategoriesScreenViewModel

@Composable
fun CategoriesScreen(
    viewModel: CategoriesScreenViewModel = viewModel() // пока не дошли до DI - вью модель здесь
) {
    val state by viewModel.screenState.collectAsState()
    val searchRequest by viewModel.searchRequest.collectAsState()

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
                message = (state as CategoriesScreenState.Error).message,
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
    categories: List<Category>
) {
    LazyColumn {
        items(categories, key = { category -> category.id }) { category ->
            ListItemCard(
                item = category.toListItem(),
                onClick = { } // Переход на экран Мои расходы
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
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
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