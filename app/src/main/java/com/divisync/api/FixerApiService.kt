package com.divisync.api

import com.divisync.config.EnvConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class FixerResponse(
    val success: Boolean?,
    val timestamp: Long?,
    val base: String?,
    val rates: Map<String, Double>?
)

interface FixerApi {
    @GET("latest")
    suspend fun latest(
        @Query("access_key") apiKey: String,
        @Query("format") format: Int = 1,
        @Query("base") base: String,
        @Query("symbols") symbols: String? = null
    ): FixerResponse
}

object FixerApiService {
    @Volatile private var retrofit: Retrofit? = null

    fun api(baseUrl: String): FixerApi {
        val normalized = normalizeBaseUrl(baseUrl)
        val instance = retrofit ?: synchronized(this) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(Interceptor { chain ->
                    chain.proceed(chain.request())
                })
                .build()

            Retrofit.Builder()
                .baseUrl(normalized)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .also { retrofit = it }
        }
        return instance.create(FixerApi::class.java)
    }

    private fun normalizeBaseUrl(url: String): String {
        var u = url.trim()
        // Si el usuario pasó una URL completa con /latest, recortar a la base
        if (u.endsWith("/latest", ignoreCase = true)) {
            u = u.removeSuffix("/latest")
        }
        if (!u.endsWith('/')) u += "/"
        return u
    }
}


