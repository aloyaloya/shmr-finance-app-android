package com.example.shmr_finance_app_android.core.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
inline fun <reified VM : ViewModel> daggerViewModel(): VM {
    val component = LocalActivityComponent.current
    val factory = component.viewModelProvider()
    return viewModel(factory = factory)
}