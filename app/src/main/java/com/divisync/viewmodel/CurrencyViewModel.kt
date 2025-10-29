package com.divisync.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.divisync.data.ExchangeRateRepository
import com.divisync.data.ConversionHistoryManager
import kotlinx.coroutines.launch

class CurrencyViewModel(application: Application) : AndroidViewModel(application) {
    var amount by mutableStateOf("100")
    var sourceCurrency by mutableStateOf("USD")
    var targetCurrency by mutableStateOf("COP")
    var convertedAmount by mutableStateOf(0.0)
    var exchangeRate by mutableStateOf(0.0)
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var lastUpdate by mutableStateOf("")
    var rateSource by mutableStateOf("")
    var isOffline by mutableStateOf(false)
    
    private val repository = ExchangeRateRepository(application.applicationContext)

    fun convertCurrency(saveToHistory: Boolean = true) {
        val amountValue = amount.toDoubleOrNull() ?: return
        if (amountValue <= 0) return
        
        isLoading = true
        error = null
        
        viewModelScope.launch {
            try {
                val res = repository.getRate(sourceCurrency, targetCurrency)
                exchangeRate = res.rate
                convertedAmount = applyCurrencyRounding(amountValue * res.rate, targetCurrency)
                lastUpdate = "UTC: " + java.time.Instant.ofEpochMilli(res.timestampMs).toString()
                rateSource = res.source
                isOffline = res.offline
                error = null

                if (saveToHistory) {
                    ConversionHistoryManager.addConversion(
                        amount = amount,
                        sourceCurrency = sourceCurrency,
                        targetCurrency = targetCurrency,
                        result = String.format("%.2f", convertedAmount),
                        rate = String.format("%.4f", res.rate)
                    )
                }
            } catch (e: Exception) {
                useFallbackRates(amountValue, saveToHistory)
                error = null
            } finally {
                isLoading = false
            }
        }
    }

    private fun applyCurrencyRounding(value: Double, currency: String): Double {
        val decimals = when (currency.uppercase()) {
            "JPY" -> 0
            "KWD" -> 3
            else -> 2
        }
        val factor = Math.pow(10.0, decimals.toDouble())
        return kotlin.math.round(value * factor) / factor
    }
    
    private fun useFallbackRates(amountValue: Double, saveToHistory: Boolean = true) {
        // Tasas fijas de respaldo actualizadas (Octubre 2025)
        val fallbackRates = mapOf(
            "USD-COP" to 4200.0,
            "COP-USD" to 0.00024,
            "USD-EUR" to 0.92,
            "EUR-USD" to 1.09,
            "USD-MXN" to 17.25,
            "MXN-USD" to 0.058,
            "USD-BRL" to 5.0,
            "BRL-USD" to 0.20,
            "USD-GBP" to 0.80,
            "GBP-USD" to 1.25,
            "USD-JPY" to 148.0,
            "JPY-USD" to 0.0068,
            "USD-CAD" to 1.35,
            "CAD-USD" to 0.74
        )
        
        val key = "$sourceCurrency-$targetCurrency"
        val rate = fallbackRates[key] ?: 1.0
        
        exchangeRate = rate
        convertedAmount = amountValue * rate
        lastUpdate = "Tasas de respaldo"
        
        // Guardar en historial solo si se solicita
        if (saveToHistory) {
            ConversionHistoryManager.addConversion(
                amount = amount,
                sourceCurrency = sourceCurrency,
                targetCurrency = targetCurrency,
                result = String.format("%.2f", convertedAmount),
                rate = String.format("%.4f", rate)
            )
        }
    }
    
    fun swapCurrencies() {
        val temp = sourceCurrency
        sourceCurrency = targetCurrency
        targetCurrency = temp
        if (convertedAmount > 0) {
            convertCurrency(saveToHistory = false)
        }
    }
}

