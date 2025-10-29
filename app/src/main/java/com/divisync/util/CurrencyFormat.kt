package com.divisync.util

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyFormat {
    fun formatAmount(amount: Double, currencyCode: String, locale: Locale = Locale.getDefault()): String {
        val format = NumberFormat.getCurrencyInstance(locale)
        return runCatching {
            format.currency = Currency.getInstance(currencyCode)
            format.format(amount)
        }.getOrElse {
            // Fallback simple
            String.format(locale, "%s %.2f", currencyCode, amount)
        }
    }
}


