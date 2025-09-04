package com.example.clumsychicken.ui.result

import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clumsychicken.core.ui.theme.*
import com.example.clumsychicken.ui.components.AppPrimaryButton
import com.example.clumsychicken.ui.components.GradientBackdrop

@Composable
fun ResultScreen(
    score: Int,
    onPlayAgain: () -> Unit,
    onMainMenu: () -> Unit,
    vm: ResultViewModel = hiltViewModel()
) {
    val high by vm.high.collectAsState(initial = 0)
    val isNewRecord by remember(high, score) { mutableStateOf(score > high) }

    val haptics = LocalHapticFeedback.current
    val playAgainInteraction = remember { MutableInteractionSource() }
    val playAgainPressed by playAgainInteraction.collectIsPressedAsState()
    val mainMenuInteraction = remember { MutableInteractionSource() }
    val mainMenuPressed by mainMenuInteraction.collectIsPressedAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val scoreGlow by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scoreGlow"
    )

    val cardScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "cardScale"
    )

    val playAgainScale by animateFloatAsState(
        targetValue = if (playAgainPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "playAgainScale"
    )

    val mainMenuScale by animateFloatAsState(
        targetValue = if (mainMenuPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "mainMenuScale"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        GradientBackdrop()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .scale(cardScale)
                    .shadow(16.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = GameColors.Background)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isNewRecord) AppText.NEW_RECORD else AppText.GAME_OVER,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isNewRecord) GameColors.Accent else GameColors.Primary,
                        textAlign = TextAlign.Center,
                        fontFamily = TitleFont,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Text(
                        text = "${AppText.YOUR_SCORE}:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = GameColors.TextDark.copy(alpha = 0.7f),
                        fontFamily = BodyFont,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = score.toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = GameColors.Primary.copy(alpha = scoreGlow),
                        fontFamily = TitleFont,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "${AppText.BEST_SCORE}: $high",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = GameColors.TextDark.copy(alpha = 0.6f),
                        fontFamily = BodyFont,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    AppPrimaryButton(
                        text = AppText.PLAY_AGAIN,
                        interactionSource = playAgainInteraction,
                        pressed = playAgainPressed,
                        onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onPlayAgain()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(playAgainScale)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AppPrimaryButton(
                        text = AppText.MAIN_MENU,
                        interactionSource = mainMenuInteraction,
                        pressed = mainMenuPressed,
                        onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onMainMenu()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(mainMenuScale)
                    )
                }
            }
        }
    }
}