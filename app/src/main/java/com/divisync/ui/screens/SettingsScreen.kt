package com.divisync.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.divisync.data.LanguageManager
import com.divisync.data.Strings
import com.divisync.data.ThemeManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    val currentLang by LanguageManager.currentLanguage
    
    val languageNames = mapOf(
        "es" to "Español",
        "en" to "English",
        "pt" to "Português",
        "fr" to "Français"
    )
    
    val isDarkTheme by ThemeManager.isDarkTheme
    var notificationsEnabled by remember { mutableStateOf(true) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        containerColor = colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = Strings.get(Strings.settings),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            // Sección Apariencia
            SectionTitle(text = Strings.get(Strings.appearance))
            
            // Idioma
            SettingCard(
                icon = "🌐",
                title = Strings.get(Strings.language),
                subtitle = languageNames[currentLang] ?: "Español",
                onClick = { showLanguageDialog = true }
            )
            
            // Tema
            SettingCardWithSwitch(
                icon = "🌙",
                title = Strings.get(Strings.darkTheme),
                subtitle = if (isDarkTheme) Strings.get(Strings.activated) else Strings.get(Strings.deactivated),
                checked = isDarkTheme,
                onCheckedChange = { ThemeManager.toggleTheme() }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Sección Notificaciones
            SectionTitle(text = Strings.get(Strings.notifications))
            
            // Alertas de divisas
            SettingCardWithSwitch(
                icon = "🔔",
                title = Strings.get(Strings.currencyAlerts),
                subtitle = Strings.get(Strings.receiveImportantNotifications),
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Sección Información
            SectionTitle(text = Strings.get(Strings.information))
            
            // Acerca de
            SettingCard(
                icon = "ℹ️",
                title = Strings.get(Strings.about),
                subtitle = Strings.get(Strings.versionSupportPolicies),
                onClick = { showAboutDialog = true }
            )
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
    
    // Diálogo de idioma
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = {
                Text(
                    text = Strings.get(Strings.selectLanguage),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Column {
                    LanguageOption(
                        language = "Español",
                        flag = "🇪🇸",
                        selected = currentLang == "es",
                        onClick = {
                            LanguageManager.setLanguage("es")
                            showLanguageDialog = false
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    LanguageOption(
                        language = "English",
                        flag = "🇺🇸",
                        selected = currentLang == "en",
                        onClick = {
                            LanguageManager.setLanguage("en")
                            showLanguageDialog = false
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    LanguageOption(
                        language = "Português",
                        flag = "🇧🇷",
                        selected = currentLang == "pt",
                        onClick = {
                            LanguageManager.setLanguage("pt")
                            showLanguageDialog = false
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    LanguageOption(
                        language = "Français",
                        flag = "🇫🇷",
                        selected = currentLang == "fr",
                        onClick = {
                            LanguageManager.setLanguage("fr")
                            showLanguageDialog = false
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text(Strings.get(Strings.close), fontSize = 16.sp)
                }
            }
        )
    }
    
    // Diálogo Acerca de
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Text(
                    text = "DiviSync",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "Versión 1.0.0",
                        fontSize = 16.sp,
                        color = colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = Strings.get(Strings.smartCurrencyConverter),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = Strings.get(Strings.convertDescription),
                        fontSize = 14.sp,
                        color = colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "© 2025 DiviSync",
                        fontSize = 13.sp,
                        color = colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text(Strings.get(Strings.close), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun SectionTitle(text: String) {
    val colorScheme = MaterialTheme.colorScheme
    Text(
        text = text,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = colorScheme.onBackground.copy(alpha = 0.6f),
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}

@Composable
fun SettingCard(
    icon: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = icon,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Column {
                    Text(
                        text = title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    )
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
fun SettingCardWithSwitch(
    icon: String,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = icon,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Column {
                    Text(
                        text = title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    )
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = colorScheme.primary
                )
            )
        }
    }
}

@Composable
fun LanguageOption(
    language: String,
    flag: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = flag,
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
            Text(
                text = language,
                fontSize = 17.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) colorScheme.primary else colorScheme.onSurface
            )
        }
        if (selected) {
            Text(
                text = "✓",
                fontSize = 20.sp,
                color = colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

