package com.divisync.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class ExchangeHostResponse(
    val success: Boolean? = true,
    val base: String? = null,
    val rates: Map<String, Double>? = null
)

interface ExchangeHostApi {
    @GET("latest")
    suspend fun latest(
        @Query("base") baseCurrency: String,
        @Query("symbols") symbols: String? = null
    ): ExchangeHostResponse
}

object ExchangeHostService {
    fun api(baseUrl: String): ExchangeHostApi = Retrofit.Builder()
        .baseUrl(normalizeBaseUrl(baseUrl))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ExchangeHostApi::class.java)

    private fun normalizeBaseUrl(url: String): String {
        var u = url.trim()
        if (u.endsWith("/latest", ignoreCase = true)) {
            u = u.removeSuffix("/latest")
        }
        if (!u.endsWith('/')) u += "/"
        return u
    }
}


