package com.divisync

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.Constraints
import androidx.work.WorkManager
import com.divisync.config.EnvConfig
import com.divisync.work.RateSyncWorker
import java.util.concurrent.TimeUnit

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        EnvConfig.init(this)

        runCatching {
            val hours = EnvConfig.getInt("UPDATE_INTERVAL_HOURS", 1).toLong().coerceAtLeast(1L)
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = PeriodicWorkRequestBuilder<RateSyncWorker>(hours, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "rate_sync_hourly",
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
        }.onFailure {
            // No crashear en arranque por WorkManager
        }
    }
}


