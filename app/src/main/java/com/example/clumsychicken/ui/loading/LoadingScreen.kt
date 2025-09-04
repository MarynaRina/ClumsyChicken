package com.example.clumsychicken.ui.loading

import androidx.navigation.NavController
import com.example.clumsychicken.core.navigation.GameMode

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clumsychicken.core.ui.theme.*
import com.example.clumsychicken.ui.components.GradientBackdrop
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(navController: NavController, mode: GameMode) {
    LaunchedEffect(mode) {
        delay(2000)
        navController.navigate("game/${mode.name}")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GradientBackdrop()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            CircularProgressIndicator(
                modifier = Modifier.size(60.dp),
                color = GameColors.Primary,
                strokeWidth = 4.dp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = AppText.LOADING,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = GameColors.TextLight,
                fontFamily = BodyFont
            )
        }
    }
}