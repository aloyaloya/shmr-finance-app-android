package com.example.shmr_finance_app_android.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.Reusable
import javax.inject.Inject

/**
 * Проверяет доступность интернет-соединения.
 * Возвращает `true`, если сеть доступна
 */
interface NetworkChecker {
    fun isNetworkAvailable(): Boolean
}

@Reusable
class NetworkCheckerImpl @Inject constructor(
    private val context: Context
) : NetworkChecker {

    override fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false

        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}