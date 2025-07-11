package com.example.shmr_finance_app_android.core.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Получает [ViewModel] через Dagger-фабрику, предоставленную [ActivityComponent]:
 * - Использует [LocalActivityComponent] для доступа к Dagger-компоненту
 * - Извлекает [ViewModelProvider.Factory] из компонента
 * - Возвращает ViewModel типа [VM]
 *
 * @return ViewModel типа [VM], созданный через Dagger
 */
@Composable
inline fun <reified VM : ViewModel> daggerViewModel(): VM {
    val component = LocalActivityComponent.current
    val factory = component.viewModelProvider()
    return viewModel(factory = factory)
}