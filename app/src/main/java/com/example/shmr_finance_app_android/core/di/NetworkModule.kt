package com.example.shmr_finance_app_android.core.di

import android.content.Context
import com.example.shmr_finance_app_android.BuildConfig
import com.example.shmr_finance_app_android.data.remote.api.FinanceApiService
import com.example.shmr_finance_app_android.data.remote.api.NetworkChecker
import com.example.shmr_finance_app_android.data.remote.api.NetworkCheckerImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Отвечает за предоставление всех зависимостей, связанных с сетевыми запросами:
 * - Настройка HTTP-клиента [OkHttpClient]
 * - Конфигурация JSON-парсера [Moshi]
 * - Проверка доступности сети [NetworkChecker]
 * - Создание и настройка Retrofit [Retrofit]
 * - Предоставление API-сервиса для работы с запросами [FinanceApiService]
 */
@Module
object NetworkModule {

    private const val BASE_URL = "https://shmr-finance.ru/api/v1/"
    private const val AUTH_TOKEN = BuildConfig.API_TOKEN

    @Provides
    @Named("maxRetries")
    fun provideMaxRetries(): Int = 3

    @Provides
    @Named("retryDelay")
    fun provideRetryDelay(): Long = 2000L

    @Provides
    @Singleton
    fun provideRetryInterceptor(
        @Named("maxRetries") maxRetries: Int,
        @Named("retryDelay") delay: Long
    ): RetryInterceptor {
        return RetryInterceptor(maxRetries, delay)
    }

    /**
     * Отвечает за создание и настройку OkHttpClient:
     * - Добавление Retry-механизма для перезапросов [RetryInterceptor]
     * - Вставка токена в заголовки всех запросов
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(retryInterceptor: RetryInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(retryInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $AUTH_TOKEN")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    /**
     * Отвечает за создание Moshi-конвертера
     * с поддержкой Kotlin-моделей [KotlinJsonAdapterFactory]
     */
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    /**
     * Отвечает за предоставление механизма мониторинга доступности сети
     * на основе констекста приложения [Context]
     */
    @Provides
    @Singleton
    fun provideNetworkChecker(appContext: Context): NetworkChecker {
        return NetworkCheckerImpl(appContext)
    }

    /**
     * Отвечает за создание и настройку [Retrofit]:
     * - Указание базового URL
     * - Подключение кастомного OkHttpClient
     * - Подключение Moshi-конвертера
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    /**
     * Отвечает за создание инстанса API-сервиса
     * для работы с запросами
     */
    @Provides
    @Singleton
    fun provideFinanceApiService(retrofit: Retrofit): FinanceApiService {
        return retrofit.create(FinanceApiService::class.java)
    }
}