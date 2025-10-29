package com.divisync.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.json.JSONObject

class SecureCache(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "exchange_rates_secure_cache",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveRates(base: String, rates: Map<String, Double>, timestampUtc: Long) {
        val obj = JSONObject()
        obj.put("base", base)
        obj.put("timestamp", timestampUtc)
        val ratesObj = JSONObject()
        rates.forEach { (k, v) -> ratesObj.put(k, v) }
        obj.put("rates", ratesObj)
        prefs.edit().putString("rates_$base", obj.toString()).apply()
    }

    fun loadRates(base: String): Triple<String, Map<String, Double>, Long>? {
        val raw = prefs.getString("rates_$base", null) ?: return null
        val obj = JSONObject(raw)
        val savedBase = obj.optString("base")
        val timestamp = obj.optLong("timestamp")
        val ratesJson = obj.optJSONObject("rates") ?: return null
        val map = mutableMapOf<String, Double>()
        val keys = ratesJson.keys()
        while (keys.hasNext()) {
            val k = keys.next()
            map[k] = ratesJson.optDouble(k)
        }
        return Triple(savedBase, map, timestamp)
    }
}


