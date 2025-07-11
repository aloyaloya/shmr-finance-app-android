package com.example.shmr_finance_app_android.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * Фабрика ViewModel для интеграции с Dagger:
 * - Ищет нужную ViewModel в предоставленной карте [creators]
 * - Поддерживает и прямое соответствие классу, и совместимость через [isAssignableFrom]
 * - Используется при создании ViewModel в Dagger-модуле [ViewModelModule]
 */
class ViewModelProviderFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass]
            ?: creators.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: throw IllegalArgumentException("Unknown ViewModel class: $modelClass")

        return creator.get() as T
    }
}