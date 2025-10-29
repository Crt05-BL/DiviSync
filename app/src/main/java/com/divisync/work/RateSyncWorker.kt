package com.divisync.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.divisync.config.EnvConfig
import com.divisync.data.ExchangeRateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RateSyncWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            // Estrategia simple: precargar tasas para un conjunto de monedas soportadas contra una base común (USD)
            val repo = ExchangeRateRepository(applicationContext)
            val supported = EnvConfig.getString("SUPPORTED_CURRENCIES", "USD,EUR,MXN,JPY,GBP,CAD").split(',').map { it.trim() }
            val baseList = listOf("USD", "EUR")
            baseList.forEach { base ->
                supported.forEach { quote -> if (quote != base) {
                    repo.getRate(base, quote)
                }}
            }
            Result.success()
        }.getOrElse { Result.retry() }
    }
}


