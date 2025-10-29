package com.divisync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.divisync.data.ThemeManager
import com.divisync.ui.screens.CurrencyConverterScreenNew
import com.divisync.ui.screens.HistoryScreen
import com.divisync.ui.screens.SettingsScreen
import com.divisync.ui.screens.SplashScreen
import com.divisync.ui.theme.DiviSyncTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by ThemeManager.isDarkTheme
            
            DiviSyncTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DiviSyncApp()
                }
            }
        }
    }
}

enum class Screen {
    SPLASH,
    CONVERTER,
    HISTORY,
    SETTINGS
}

@Composable
fun DiviSyncApp() {
    var currentScreen by remember { mutableStateOf(Screen.SPLASH) }
    
    LaunchedEffect(Unit) {
        delay(2500) // Mostrar splash por 2.5 segundos
        currentScreen = Screen.CONVERTER
    }
    
    when (currentScreen) {
        Screen.SPLASH -> {
            SplashScreen()
        }
        Screen.CONVERTER -> {
            CurrencyConverterScreenNew(
                onHistoryClick = { currentScreen = Screen.HISTORY },
                onSettingsClick = { currentScreen = Screen.SETTINGS }
            )
        }
        Screen.HISTORY -> {
            HistoryScreen(
                onBackClick = { currentScreen = Screen.CONVERTER }
            )
        }
        Screen.SETTINGS -> {
            SettingsScreen(
                onBackClick = { currentScreen = Screen.CONVERTER }
            )
        }
    }
}

