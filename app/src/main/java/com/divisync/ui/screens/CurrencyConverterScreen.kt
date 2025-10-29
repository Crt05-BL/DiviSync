package com.divisync.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.divisync.data.ConversionHistoryManager
import com.divisync.ui.theme.DiviSyncBlue
import com.divisync.ui.theme.DiviSyncPurple
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterScreen(
    onHistoryClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    var amount by remember { mutableStateOf("100") }
    var sourceCurrency by remember { mutableStateOf("USD") }
    var targetCurrency by remember { mutableStateOf("EUR") }
    var showMenu by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }
    var lastConvertedAmount by remember { mutableStateOf("") }
    
    // Animación de entrada
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }
    
    // Las tasas reales ahora se calculan vía ViewModel, este módulo solo gestiona UI.
    val convertedAmount = amount.toFloatOrNull() ?: 0f
    
    // Función para guardar la conversión en el historial
    fun saveToHistory() {
        val amountValue = amount.toFloatOrNull()
        if (amountValue != null && amountValue > 0 && amount != lastConvertedAmount) {
            ConversionHistoryManager.addConversion(
                amount = amount,
                sourceCurrency = sourceCurrency,
                targetCurrency = targetCurrency,
                result = String.format("%.2f", convertedAmount),
                rate = "-"
            )
            lastConvertedAmount = amount
        }
    }
    
    // Datos de divisas con nombres completos
    val currencyNames = mapOf(
        "USD" to "Dólar estadounidense",
        "EUR" to "Euro",
        "GBP" to "Libra esterlina",
        "JPY" to "Yen japonés",
        "CAD" to "Dólar canadiense",
        "AUD" to "Dólar australiano",
        "CHF" to "Franco suizo",
        "CNY" to "Yuan chino",
        "MXN" to "Peso mexicano",
        "BRL" to "Real brasileño"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "DiviSync",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configuración",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DiviSyncBlue,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8F9FD),
                            Color(0xFFE3E9F5)
                        )
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)) +
                        slideInVertically(initialOffsetY = { -40 })
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Título de la sección con subtítulo
                    Text(
                        text = "Convertidor de Divisas",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = DiviSyncBlue
                    )
                    
                    Text(
                        text = "Conversión en tiempo real",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            
            // Card de conversión con sombra mejorada
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = DiviSyncBlue.copy(alpha = 0.2f)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Divisa origen
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "De",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = currencyNames[sourceCurrency] ?: "",
                                fontSize = 13.sp,
                                color = Color.Gray.copy(alpha = 0.7f)
                            )
                        }
                        Surface(
                            shape = CircleShape,
                            color = DiviSyncBlue.copy(alpha = 0.1f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "→",
                                    fontSize = 16.sp,
                                    color = DiviSyncBlue
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    CurrencySelector(
                        currency = sourceCurrency,
                        onCurrencyChange = { 
                            sourceCurrency = it
                            saveToHistory()
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Campo de entrada mejorado
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF8F9FF)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { 
                                amount = it
                                // Guardar en historial cuando cambia el monto
                                if (it.isNotEmpty()) {
                                    saveToHistory()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 42.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = DiviSyncBlue
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DiviSyncBlue,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = "0.00",
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray.copy(alpha = 0.3f),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Botón para intercambiar divisas (mejorado)
                    FloatingActionButton(
                        onClick = {
                            val temp = sourceCurrency
                            sourceCurrency = targetCurrency
                            targetCurrency = temp
                            saveToHistory()
                        },
                        modifier = Modifier.size(64.dp),
                        containerColor = Color.Transparent,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 12.dp
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(DiviSyncBlue, DiviSyncPurple)
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Intercambiar divisas",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Divisa destino
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "A",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = currencyNames[targetCurrency] ?: "",
                                fontSize = 13.sp,
                                color = Color.Gray.copy(alpha = 0.7f)
                            )
                        }
                        Surface(
                            shape = CircleShape,
                            color = DiviSyncPurple.copy(alpha = 0.1f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "✓",
                                    fontSize = 16.sp,
                                    color = DiviSyncPurple
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    CurrencySelector(
                        currency = targetCurrency,
                        onCurrencyChange = { 
                            targetCurrency = it
                            saveToHistory()
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Resultado de la conversión (mejorado)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(20.dp),
                                spotColor = DiviSyncPurple.copy(alpha = 0.2f)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            DiviSyncBlue.copy(alpha = 0.05f),
                                            DiviSyncPurple.copy(alpha = 0.08f)
                                        )
                                    )
                                )
                                .padding(24.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                Text(
                                    text = "≈",
                                    fontSize = 24.sp,
                                    color = Color.Gray.copy(alpha = 0.5f),
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = "Resultado",
                                    fontSize = 16.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "${amount.ifEmpty { "0" }} $sourceCurrency",
                                    fontSize = 18.sp,
                                    color = Color.DarkGray.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = String.format("%.2f", convertedAmount),
                                    fontSize = 52.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DiviSyncBlue,
                                    letterSpacing = (-1).sp
                                )
                                Text(
                                    text = targetCurrency,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DiviSyncPurple,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                // Esta pantalla clásica ya no muestra la tasa precisa, usar la nueva pantalla para detalles
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(28.dp))
            
            // Botón de Historial
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .clickable(onClick = onHistoryClick),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Historial",
                        tint = DiviSyncBlue,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Ver Historial",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DiviSyncBlue
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun CurrencySelector(
    currency: String,
    onCurrencyChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    val currencies = listOf(
        "USD" to "🇺🇸",
        "EUR" to "🇪🇺",
        "GBP" to "🇬🇧",
        "JPY" to "🇯🇵",
        "CAD" to "🇨🇦",
        "AUD" to "🇦🇺",
        "CHF" to "🇨🇭",
        "CNY" to "🇨🇳",
        "MXN" to "🇲🇽",
        "BRL" to "🇧🇷"
    )
    
    val selectedFlag = currencies.find { it.first == currency }?.second ?: "🌐"
    
    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xFFF8F9FA)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = selectedFlag,
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = currency,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Seleccionar divisa",
                    tint = Color.Gray
                )
            }
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            currencies.forEach { (code, flag) ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = flag, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = code, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                        }
                    },
                    onClick = {
                        onCurrencyChange(code)
                        expanded = false
                    }
                )
            }
        }
    }
}
