package com.divisync.data

import androidx.compose.runtime.mutableStateListOf
import com.divisync.ui.screens.ConversionHistory

object ConversionHistoryManager {
    val historyList = mutableStateListOf<ConversionHistory>()
    
    fun addConversion(
        amount: String,
        sourceCurrency: String,
        targetCurrency: String,
        result: String,
        rate: String
    ) {
        val newConversion = ConversionHistory(
            amount = amount,
            sourceCurrency = sourceCurrency,
            targetCurrency = targetCurrency,
            result = result,
            rate = rate,
            isFavorite = false
        )
        // Agregar al inicio de la lista (más reciente primero)
        historyList.add(0, newConversion)
    }
    
    fun clearHistory() {
        historyList.clear()
    }
    
    fun toggleFavorite(conversion: ConversionHistory) {
        val index = historyList.indexOf(conversion)
        if (index != -1) {
            historyList[index] = conversion.copy(isFavorite = !conversion.isFavorite)
        }
    }
}

