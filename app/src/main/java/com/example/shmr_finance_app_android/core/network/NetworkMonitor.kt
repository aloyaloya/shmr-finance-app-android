package com.example.shmr_finance_app_android.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.shmr_finance_app_android.data.sync.SyncAllWorker

class NetworkMonitor(private val context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun registerNetworkCallback() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(
            request,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Log.d("NetworkMonitor", "Internet is available")

                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()

                    val syncRequest = OneTimeWorkRequestBuilder<SyncAllWorker>()
                        .setConstraints(constraints)
                        .addTag("network_auto_sync")
                        .build()

                    WorkManager.getInstance(context).enqueueUniqueWork(
                        "sync_work",
                        ExistingWorkPolicy.KEEP,
                        syncRequest
                    )
                }

                override fun onLost(network: Network) {
                    Log.d("NetworkMonitor", "Internet lost")
                }
            }
        )
    }
}