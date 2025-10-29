package com.divisync.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class ExchangeRateResponse(
    val success: Boolean? = true,
    val base: String? = "",
    val rates: Map<String, Double>? = emptyMap()
)

interface ExchangeRateApi {
    @GET("latest")
    suspend fun getExchangeRates(
        @Query("base") baseCurrency: String
    ): ExchangeRateResponse
}

object ExchangeRateService {
    private const val BASE_URL = "https://api.exchangerate.host/"
    
    val api: ExchangeRateApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRateApi::class.java)
    }
}

