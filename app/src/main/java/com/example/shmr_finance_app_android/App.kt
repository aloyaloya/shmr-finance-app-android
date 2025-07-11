package com.example.shmr_finance_app_android

import android.app.Application
import com.example.shmr_finance_app_android.core.di.AppComponent
import com.example.shmr_finance_app_android.core.di.AppModule
import com.example.shmr_finance_app_android.core.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .appModule(AppModule(this))
            .build()
    }
}