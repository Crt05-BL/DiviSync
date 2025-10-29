package com.divisync.data

import android.content.Context
import android.util.Log
import com.divisync.api.ExchangeHostService
import com.divisync.api.FixerApiService
import com.divisync.config.EnvConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

data class RateResult(
    val rate: Double,
    val timestampMs: Long,
    val source: String, // FIXER | EXCHANGE_HOST | CACHE
    val offline: Boolean
)

class ExchangeRateRepository(context: Context) {
    private val cache = SecureCache(context)

    private val fixerUrl = EnvConfig.getString("FIXER_API_URL", "https://data.fixer.io/api/latest")
    private val fixerKeyEnv = EnvConfig.getString("FIXER_API_KEY", "")
    private val hostUrl = EnvConfig.getString("EXCHANGE_HOST_URL", "https://api.exchangerate.host/latest")
    private val cacheTtlHours = EnvConfig.getInt("CACHE_TTL_HOURS", 1)
    private val spreadPercent = EnvConfig.getDouble("SPREAD_PERCENT", 0.0)

    private val fixerApi = FixerApiService.api(fixerUrl)
    private val hostApi = ExchangeHostService.api(hostUrl)

    suspend fun getRate(base: String, quote: String): RateResult = withContext(Dispatchers.IO) {
        val baseU = base.uppercase(Locale.ROOT)
        val quoteU = quote.uppercase(Locale.ROOT)
        
        Log.d("ExchangeRate", "=== SOLICITANDO TASA: $baseU -> $quoteU ===")

        // 1) Usar caché si vigente
        cache.loadRates(baseU)?.let { (_, rates, ts) ->
            if (isFresh(ts)) {
                rates[quoteU]?.let { r ->
                    Log.d("ExchangeRate", "✅ CACHE: $baseU->$quoteU = $r (fresh)")
                    return@withContext RateResult(applySpread(r), ts, source = "CACHE", offline = true)
                }
            }
        }

        // 2) Intentar Fixer
        val apiKey = if (fixerKeyEnv.isNotBlank()) fixerKeyEnv else com.divisync.BuildConfig.FIXER_API_KEY
        Log.d("ExchangeRate", "🔑 Fixer API Key: ${if (apiKey.isNotBlank()) "***${apiKey.takeLast(4)}" else "VACÍA"}")
        
        val fixerRes = runCatching {
            fixerApi.latest(apiKey, 1, baseU, "$baseU,$quoteU")
        }.getOrNull()

        val fixerRates = fixerRes?.rates
        val fixerBase = fixerRes?.base?.uppercase(Locale.ROOT)
        val fixerTs = (fixerRes?.timestamp ?: (System.currentTimeMillis() / 1000L)) * 1000L
        
        Log.d("ExchangeRate", "📡 Fixer Response: success=${fixerRes?.success}, base=$fixerBase, rates=$fixerRates")
        if (!fixerRates.isNullOrEmpty()) {
            // Calcular SIEMPRE por cruce: rate(base->quote) = rates[quote]/rates[base] relativo a fixerBase
            val rb = fixerRates[baseU] ?: if (fixerBase == baseU) 1.0 else null
            val rq = fixerRates[quoteU]
            Log.d("ExchangeRate", "🧮 Fixer cálculo: rb=$rb, rq=$rq, fixerBase=$fixerBase")
            
            if (rq != null && (rb != null && rb != 0.0)) {
                val derived = if (fixerBase == baseU) rq else rq / rb
                Log.d("ExchangeRate", "✅ Fixer resultado: $baseU->$quoteU = $derived")
                val normalized = mapOf(quoteU to derived)
                cache.saveRates(baseU, normalized, fixerTs)
                return@withContext RateResult(applySpread(derived), fixerTs, source = "FIXER", offline = false)
            }
            // Como respaldo, normalizar todo el mapa si es posible
            val adjusted: Map<String, Double> = if (fixerBase != null && fixerBase != baseU) {
                val rateBaseU = fixerRates[baseU] ?: 1.0
                fixerRates.mapValues { (_, v) -> v / rateBaseU }
            } else fixerRates
            adjusted[quoteU]?.let { r ->
                Log.d("ExchangeRate", "✅ Fixer respaldo: $baseU->$quoteU = $r")
                cache.saveRates(baseU, adjusted, fixerTs)
                return@withContext RateResult(applySpread(r), fixerTs, source = "FIXER", offline = false)
            }
        }

        // 3) Respaldo exchangerate.host
        Log.d("ExchangeRate", "🔄 Intentando exchangerate.host...")
        val hostRes = runCatching {
            hostApi.latest(baseU, "$baseU,$quoteU")
        }.getOrNull()
        val hostRates = hostRes?.rates
        val hostBase = hostRes?.base?.uppercase(Locale.ROOT)
        val hostTs = System.currentTimeMillis()
        
        Log.d("ExchangeRate", "📡 ExchangeHost Response: success=${hostRes?.success}, base=$hostBase, rates=$hostRates")
        if (!hostRates.isNullOrEmpty()) {
            val rb = hostRates[baseU] ?: if (hostBase == baseU) 1.0 else null
            val rq = hostRates[quoteU]
            Log.d("ExchangeRate", "🧮 ExchangeHost cálculo: rb=$rb, rq=$rq, hostBase=$hostBase")
            
            if (rq != null && (rb != null && rb != 0.0)) {
                val derived = if (hostBase == baseU) rq else rq / rb
                Log.d("ExchangeRate", "✅ ExchangeHost resultado: $baseU->$quoteU = $derived")
                cache.saveRates(baseU, mapOf(quoteU to derived), hostTs)
                return@withContext RateResult(applySpread(derived), hostTs, source = "EXCHANGE_HOST", offline = false)
            }
            val adjusted: Map<String, Double> = if (hostBase != null && hostBase != baseU) {
                val rateBaseU = hostRates[baseU] ?: 1.0
                hostRates.mapValues { (_, v) -> v / rateBaseU }
            } else hostRates
            adjusted[quoteU]?.let { r ->
                Log.d("ExchangeRate", "✅ ExchangeHost respaldo: $baseU->$quoteU = $r")
                cache.saveRates(baseU, adjusted, hostTs)
                return@withContext RateResult(applySpread(r), hostTs, source = "EXCHANGE_HOST", offline = false)
            }
        }

        // 3b) Intento extra: cálculo cruzado vía USD usando exchangerate.host
        Log.d("ExchangeRate", "🔄 Intentando cálculo cruzado vía USD...")
        runCatching {
            val crossBase = "USD"
            val crossRes = hostApi.latest(crossBase, "$baseU,$quoteU")
            val crossRates = crossRes.rates ?: emptyMap()
            val rateUsdToQuote = crossRates[quoteU]
            val rateUsdToBase = crossRates[baseU]
            Log.d("ExchangeRate", "🧮 Cálculo cruzado: USD->$quoteU=$rateUsdToQuote, USD->$baseU=$rateUsdToBase")
            if (rateUsdToQuote != null && rateUsdToBase != null && rateUsdToBase != 0.0) {
                val derived = rateUsdToQuote / rateUsdToBase
                Log.d("ExchangeRate", "✅ Cálculo cruzado resultado: $baseU->$quoteU = $derived")
                val ts = System.currentTimeMillis()
                cache.saveRates(baseU, mapOf(quoteU to derived), ts)
                return@withContext RateResult(applySpread(derived), ts, source = "EXCHANGE_HOST", offline = false)
            }
        }

        // 4) Intentar caché expirada como último recurso
        cache.loadRates(baseU)?.second?.get(quoteU)?.let { r ->
            Log.d("ExchangeRate", "⚠️ CACHE EXPIRADA: $baseU->$quoteU = $r")
            return@withContext RateResult(applySpread(r), System.currentTimeMillis(), source = "CACHE", offline = true)
        }

        // 5) Sin datos: identidad
        Log.d("ExchangeRate", "❌ SIN DATOS: devolviendo 1.0 para $baseU->$quoteU")
        RateResult(1.0, System.currentTimeMillis(), source = "CACHE", offline = true)
    }

    private fun isFresh(timestampMs: Long): Boolean {
        val ttlMs = cacheTtlHours * 60L * 60L * 1000L
        return (System.currentTimeMillis() - timestampMs) <= ttlMs
    }

    private fun applySpread(rate: Double): Double {
        if (spreadPercent == 0.0) return rate
        return rate * (1.0 + spreadPercent / 100.0)
    }
}


