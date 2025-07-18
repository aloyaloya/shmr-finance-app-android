package com.example.shmr_finance_app_android.core.di

import android.app.Application
import com.example.shmr_finance_app_android.App
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Главный Dagger-компонент приложения, предоставляющий зависимости с областью действия [Singleton]:
 * - Объединяет все основные модули
 * - Предоставляет фабрику для создания [ActivityComponent]
 * - Используется для внедрения зависимостей в [App]
 */
@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        RepositoryModule::class,
        DataSourceModule::class,
        ViewModelModule::class,
        WorkersModule::class
    ]
)
interface AppComponent {

    /** Точка внедрения зависимостей в класс приложения [App] */
    fun inject(app: App)

    /** Предоставляет фабрику для создания [ActivityComponent] */
    fun activityComponent(): ActivityComponent.Factory

    /** Билдер для создания [AppComponent] с передачей необходимых зависимостей */
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }
}