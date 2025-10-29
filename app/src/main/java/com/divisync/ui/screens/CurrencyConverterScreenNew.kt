package com.divisync.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.divisync.data.CurrencyData
import com.divisync.data.LanguageManager
import com.divisync.data.Strings
import com.divisync.viewmodel.CurrencyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterScreenNew(
    onHistoryClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    viewModel: CurrencyViewModel = viewModel()
) {
    val colorScheme = MaterialTheme.colorScheme
    val currentLang by LanguageManager.currentLanguage
    var isVisible by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    var showStatusDialog by remember { mutableStateOf(false) }
    
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer"
    )
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100)
        isVisible = true
        // Conversión inicial (sin guardar en historial)
        viewModel.convertCurrency(saveToHistory = false)
    }
    
    Scaffold(
        containerColor = colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DiviSync",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp,
                            color = colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.size(12.dp)
                        ) {}
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configuración",
                            tint = colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showStatusDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Fuentes y estado",
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + scaleIn()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Título principal
                        Text(
                            text = Strings.get(Strings.intelligentConverter),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                                modifier = Modifier.size(8.dp)
                            ) {
                                if (viewModel.isLoading) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Color(0xFF4CAF50).copy(alpha = shimmerAlpha),
                                                CircleShape
                                            )
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color(0xFF4CAF50), CircleShape)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (viewModel.isLoading) Strings.get(Strings.updating) else Strings.get(Strings.realTimeRates),
                                fontSize = 14.sp,
                                color = colorScheme.onBackground.copy(alpha = 0.7f),
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
                
            if (showStatusDialog) {
                AlertDialog(
                    onDismissRequest = { showStatusDialog = false },
                    confirmButton = {
                        TextButton(onClick = { showStatusDialog = false }) {
                            Text("Cerrar")
                        }
                    },
                    title = { Text("Fuentes y estado") },
                    text = {
                        Column {
                            Text("Proveedor: ${viewModel.rateSource}")
                            Text("Offline: ${if (viewModel.isOffline) "Sí" else "No"}")
                            Text("Última actualización: ${viewModel.lastUpdate}")
                        }
                    }
                )
            }
                // Card principal con diseño revolucionario
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        // Divisa origen
                        Text(
                            text = Strings.get(Strings.youSend),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface.copy(alpha = 0.6f),
                            letterSpacing = 1.sp
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Campo de entrada
                            OutlinedTextField(
                                value = viewModel.amount,
                                onValueChange = { viewModel.amount = it },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorScheme.onSurface
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = colorScheme.primary,
                                    unfocusedBorderColor = colorScheme.onSurface.copy(alpha = 0.2f),
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                placeholder = {
                                    Text(
                                        text = "0",
                                        fontSize = 36.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorScheme.onSurface.copy(alpha = 0.3f)
                                    )
                                }
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            // Selector de divisa origen
                            CurrencySelectorCompact(
                                currency = viewModel.sourceCurrency,
                                onCurrencyChange = { viewModel.sourceCurrency = it }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        // Botón de intercambio con animación
                        val rotation by animateFloatAsState(
                            targetValue = if (viewModel.isLoading) 360f else 0f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart
                            ),
                            label = "rotation"
                        )
                        
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            FloatingActionButton(
                                onClick = { viewModel.swapCurrencies() },
                                modifier = Modifier.size(56.dp),
                                containerColor = Color.Transparent,
                                elevation = FloatingActionButtonDefaults.elevation(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    colorScheme.primary,
                                                    Color(0xFF4CAF50)
                                                )
                                            ),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Intercambiar",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(28.dp)
                                            .rotate(if (viewModel.isLoading) rotation else 0f)
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        // Divisa destino
                        Text(
                            text = Strings.get(Strings.youReceive),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface.copy(alpha = 0.6f),
                            letterSpacing = 1.sp
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Resultado
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = colorScheme.primary.copy(alpha = 0.1f)
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    val locale = java.util.Locale.getDefault()
                                    val formatted = com.divisync.util.CurrencyFormat.formatAmount(
                                        amount = viewModel.convertedAmount,
                                        currencyCode = viewModel.targetCurrency,
                                        locale = locale
                                    )
                                    Text(
                                        text = if (viewModel.convertedAmount > 0) formatted else com.divisync.util.CurrencyFormat.formatAmount(0.0, viewModel.targetCurrency, locale),
                                        fontSize = 36.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorScheme.primary
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            // Selector de divisa destino
                            CurrencySelectorCompact(
                                currency = viewModel.targetCurrency,
                                onCurrencyChange = { viewModel.targetCurrency = it }
                            )
                        }
                        
                        if (viewModel.exchangeRate > 0) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "1 ${viewModel.sourceCurrency} = ${String.format("%.4f", viewModel.exchangeRate)} ${viewModel.targetCurrency}",
                                    fontSize = 13.sp,
                                    color = colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = viewModel.lastUpdate,
                                    fontSize = 11.sp,
                                    color = Color(0xFF4CAF50)
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(28.dp))
                
                // Botón de conversión GRANDE
                Button(
                    onClick = { viewModel.convertCurrency() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp),
                    enabled = !viewModel.isLoading
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        colorScheme.primary,
                                        Color(0xFF4CAF50)
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                color = Color.White,
                                strokeWidth = 3.dp
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = Strings.get(Strings.convertNow),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    letterSpacing = 1.sp
                                )
                            }
                        }
                    }
                }
                
                if (viewModel.error != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFEF5350).copy(alpha = 0.1f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFEF5350),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = viewModel.error ?: "",
                                fontSize = 14.sp,
                                color = Color(0xFFEF5350)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Detalles de la conversión
                if (viewModel.convertedAmount > 0) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            colorScheme.primary.copy(alpha = 0.08f),
                                            Color(0xFF4CAF50).copy(alpha = 0.05f)
                                        )
                                    )
                                )
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = Strings.get(Strings.finalResult),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.onSurface.copy(alpha = 0.5f),
                                letterSpacing = 1.5.sp
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "${viewModel.amount} ${viewModel.sourceCurrency}",
                                fontSize = 20.sp,
                                color = colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "=",
                                fontSize = 32.sp,
                                color = colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = String.format("%.2f", viewModel.convertedAmount),
                                fontSize = 56.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = colorScheme.primary,
                                letterSpacing = (-2).sp
                            )
                            
                            Text(
                                text = viewModel.targetCurrency,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CAF50),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Botón de Historial VISIBLE
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onHistoryClick),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorScheme.surface
                    ),
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
                            tint = colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = Strings.get(Strings.viewHistory),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySelectorCompact(
    currency: String,
    onCurrencyChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val colorScheme = MaterialTheme.colorScheme
    
    val selectedCurrency = CurrencyData.currencies.find { it.code == currency }
    val filteredCurrencies = if (expanded) {
        CurrencyData.searchCurrencies(searchQuery)
    } else {
        emptyList()
    }
    
    Box {
        // Botón selector
        Surface(
            onClick = { expanded = true },
            shape = RoundedCornerShape(12.dp),
            color = colorScheme.primary.copy(alpha = 0.15f),
            modifier = Modifier.size(width = 110.dp, height = 64.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = selectedCurrency?.flag ?: "🌐",
                    fontSize = 24.sp
                )
                Text(
                    text = currency,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )
            }
        }
        
        // Modal de selección mejorado
        if (expanded) {
            AlertDialog(
                onDismissRequest = {
                    expanded = false
                    searchQuery = ""
                },
                title = {
                    Column {
                        Text(
                            text = "Seleccionar Moneda",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(Strings.get(Strings.searchCurrency)) },
                            leadingIcon = {
                                Icon(Icons.Default.Search, "Buscar")
                            },
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                },
                text = {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                    ) {
                        items(filteredCurrencies.size) { index ->
                            val curr = filteredCurrencies[index]
                            Surface(
                                onClick = {
                                    onCurrencyChange(curr.code)
                                    expanded = false
                                    searchQuery = ""
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp, horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = curr.flag, fontSize = 28.sp)
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(
                                            text = curr.code,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = curr.name,
                                            fontSize = 13.sp,
                                            color = colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }
                            if (index < filteredCurrencies.size - 1) {
                                HorizontalDivider(
                                    color = colorScheme.onSurface.copy(alpha = 0.1f)
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            expanded = false
                            searchQuery = ""
                        }
                    ) {
                        Text("Cerrar", fontSize = 16.sp)
                    }
                }
            )
        }
    }
}

