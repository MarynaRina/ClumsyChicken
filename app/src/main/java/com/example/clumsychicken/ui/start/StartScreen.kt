package com.example.clumsychicken.ui.start

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clumsychicken.core.ui.theme.*
import com.example.clumsychicken.ui.components.GradientBackdrop

@Composable
fun StartScreen(onPlayEasy: () -> Unit, onPlayHard: () -> Unit) {
    var showDifficulty by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val titleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "titleAlpha"
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
            if (!showDifficulty) {
                Text(
                    text = AppText.TITLE,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = GameColors.TextLight.copy(alpha = titleAlpha),
                    textAlign = TextAlign.Center,
                    fontFamily = TitleFont,
                    modifier = Modifier.padding(bottom = 64.dp)
                )
            }

            AnimatedContent(
                targetState = showDifficulty,
                transitionSpec = {
                    fadeIn(tween(300)) togetherWith fadeOut(tween(200))
                },
                label = "contentTransition"
            ) { showDifficultyScreen ->
                if (!showDifficultyScreen) {
                    AnimatedPlayButton(
                        onClick = { showDifficulty = true }
                    )
                } else {
                    AnimatedDifficultyCard(
                        onPlayEasy = onPlayEasy,
                        onPlayHard = onPlayHard,
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedPlayButton(onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "buttonScale"
    )

    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 4.dp.value else 12.dp.value,
        animationSpec = tween(150),
        label = "buttonElevation"
    )

    Button(
        onClick = {
            isPressed = true
            onClick()
        },
        shape = RoundedCornerShape(32.dp),
        colors = ButtonDefaults.buttonColors(containerColor = GameColors.Primary),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation.dp,
            pressedElevation = 4.dp
        ),
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .height(65.dp)
            .scale(scale)
    ) {
        Text(
            AppText.PLAY,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = GameColors.TextLight,
            fontFamily = BodyFont
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(200)
            isPressed = false
        }
    }
}

@Composable
private fun AnimatedDifficultyCard(
    onPlayEasy: () -> Unit,
    onPlayHard: () -> Unit,
) {
    var easyPressed by remember { mutableStateOf(false) }
    var hardPressed by remember { mutableStateOf(false) }

    val cardScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "cardScale"
    )

    val easyScale by animateFloatAsState(
        targetValue = if (easyPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "easyScale"
    )

    val hardScale by animateFloatAsState(
        targetValue = if (hardPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "hardScale"
    )

    Card(
        modifier = Modifier
            .scale(cardScale)
            .shadow(12.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GameColors.Background)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = AppText.CHOOSE_DIFFICULTY,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = GameColors.TextDark,
                textAlign = TextAlign.Center,
                fontFamily = BodyFont,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .animateContentSize()
            )

            Button(
                onClick = {
                    easyPressed = true
                    onPlayEasy()
                },
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GameColors.Primary),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = if (easyPressed) 2.dp else 6.dp,
                    pressedElevation = 2.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .scale(easyScale)
            ) {
                Text(AppText.EASY, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, fontFamily = BodyFont)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    hardPressed = true
                    onPlayHard()
                },
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GameColors.Accent),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = if (hardPressed) 2.dp else 6.dp,
                    pressedElevation = 2.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .scale(hardScale)
            ) {
                Text(AppText.HARD, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, fontFamily = BodyFont)
            }
        }
    }

    LaunchedEffect(easyPressed) {
        if (easyPressed) {
            kotlinx.coroutines.delay(100)
            easyPressed = false
        }
    }

    LaunchedEffect(hardPressed) {
        if (hardPressed) {
            kotlinx.coroutines.delay(100)
            hardPressed = false
        }
    }
}