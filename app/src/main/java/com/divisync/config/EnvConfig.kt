package com.divisync.config

import android.content.Context
import java.util.Properties

object EnvConfig {
    private const val ENV_FILE = ".env"
    private const val ENV_ALT_FILE = "env.properties"
    private var loaded = false
    private val properties = Properties()

    @Synchronized
    fun init(context: Context) {
        if (loaded) return
        val loadedOk = runCatching {
            // Intentar .env primero
            context.assets.open(ENV_FILE).use { input ->
                properties.load(input)
            }
        }.recoverCatching {
            // Intentar env.properties como alternativa
            context.assets.open(ENV_ALT_FILE).use { input ->
                properties.load(input)
            }
        }.isSuccess

        if (!loadedOk) {
            // Defaults minimalistas si no existe archivo de entorno
            properties.setProperty("ENV", "production")
            properties.setProperty("UPDATE_INTERVAL_HOURS", "1")
            properties.setProperty("CACHE_TTL_HOURS", "1")
            properties.setProperty("FIXER_API_URL", "https://data.fixer.io/api/latest")
            properties.setProperty("FIXER_API_KEY", "")
            properties.setProperty("EXCHANGE_HOST_URL", "https://api.exchangerate.host/latest")
            properties.setProperty("SPREAD_PERCENT", "0.0")
            properties.setProperty("SUPPORTED_CURRENCIES", "USD,EUR,MXN,COP,ARS,BRL,CLP,JPY,GBP,CAD")
        }
        loaded = true
    }

    fun getString(key: String, default: String = ""): String = properties.getProperty(key, default)
    fun getDouble(key: String, default: Double = 0.0): Double = properties.getProperty(key)?.toDoubleOrNull() ?: default
    fun getInt(key: String, default: Int = 0): Int = properties.getProperty(key)?.toIntOrNull() ?: default
}


