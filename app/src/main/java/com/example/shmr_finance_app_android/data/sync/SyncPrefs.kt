package com.example.shmr_finance_app_android.data.sync

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncPrefs @Inject constructor(
    private val context: Context
) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)
    }

    companion object {
        private const val KEY_LAST_SYNC_TIME = "last_sync_time"
    }

    fun saveLastSyncTime(timeMillis: Long) {
        prefs.edit().putLong(KEY_LAST_SYNC_TIME, timeMillis).apply()
    }

    fun getLastSyncTime(): Long = prefs.getLong(KEY_LAST_SYNC_TIME, 0L)
}