package com.divisync.data

import androidx.compose.runtime.mutableStateOf

object ThemeManager {
    var isDarkTheme = mutableStateOf(true)
    
    fun toggleTheme() {
        isDarkTheme.value = !isDarkTheme.value
    }
}

