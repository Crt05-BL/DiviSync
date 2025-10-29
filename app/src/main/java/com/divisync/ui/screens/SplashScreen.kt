package com.divisync.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.divisync.R

@Composable
fun SplashScreen() {
    // Animación fade in
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500)
        )
    }
    
    // Gradiente azul → morado
    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF2196F3), // Azul
            Color(0xFF9C27B0)  // Morado
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo DiviSync
            Icon(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "DiviSync Logo",
                modifier = Modifier
                    .size(120.dp)
                    .alpha(alpha.value),
                tint = Color.White
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Texto descriptivo
            Text(
                text = "Smart Currency Converter",
                color = Color.White.copy(alpha = 0.9f * alpha.value),
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}
