package com.example.shmr_finance_app_android.core.di

import androidx.lifecycle.ViewModelProvider
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    fun viewModelProvider(): ViewModelProvider.Factory
}