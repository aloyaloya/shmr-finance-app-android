package com.example.shmr_finance_app_android.core.di

import android.app.Application
import com.example.shmr_finance_app_android.App
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        DataSourceModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {
    fun inject(app: App)
    fun activityComponent(): ActivityComponent.Factory

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }
}