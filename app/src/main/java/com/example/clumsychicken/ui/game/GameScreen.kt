package com.example.clumsychicken.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clumsychicken.R
import com.example.clumsychicken.core.navigation.GameMode
import com.example.clumsychicken.core.ui.theme.AppText
import com.example.clumsychicken.core.ui.theme.BodyFont
import com.example.clumsychicken.core.ui.theme.GameColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

data class TappedEgg(
    val id: Long,
    val x: Float,
    val y: Float,
    val radius: Float,
    val isBad: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Composable
fun GameScreen(
    mode: GameMode,
    onFinish: (Int) -> Unit,
    onExit: () -> Unit,
    vm: GameViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    var tappedEggs by remember { mutableStateOf(listOf<TappedEgg>()) }

    LaunchedEffect(mode) { vm.start(mode) }

    LaunchedEffect(tappedEggs) {
        if (tappedEggs.isNotEmpty()) {
            kotlinx.coroutines.delay(300)
            tappedEggs = tappedEggs.filter {
                System.currentTimeMillis() - it.timestamp < 300
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.game_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.2f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.2f)
                        )
                    )
                )
        )

        Column(Modifier.fillMaxSize()) {
            TopBar(
                score = state.score,
                time = state.timeLeft,
                isPaused = state.isPaused,
                onPause = { vm.togglePause() },
                onExit = onExit
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .onGloballyPositioned {
                        val s: IntSize = it.size
                        vm.setCanvasSize(s.width.toFloat(), s.height.toFloat())
                    }
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val hitEgg = state.eggs.firstOrNull { egg ->
                                val dx = offset.x - egg.x * state.width
                                val dy = offset.y - egg.y * state.height
                                kotlin.math.hypot(dx, dy) <= egg.radius && !egg.isBroken
                            }

                            if (hitEgg != null) {
                                tappedEggs = tappedEggs + TappedEgg(
                                    id = hitEgg.id,
                                    x = hitEgg.x,
                                    y = hitEgg.y,
                                    radius = hitEgg.radius,
                                    isBad = hitEgg.isBad
                                )
                            }

                            vm.tap(offset.x, offset.y)
                        }
                    }
            ) {
                val eggBitmap: ImageBitmap = ImageBitmap.imageResource(id = R.drawable.ic_egg)
                val badBitmap: ImageBitmap = ImageBitmap.imageResource(id = R.drawable.ic_bad_egg)
                val crackedBitmap: ImageBitmap = ImageBitmap.imageResource(id = R.drawable.ic_cracked_egg)

                Canvas(Modifier.fillMaxSize()) {
                    state.eggs.forEach { e ->
                        drawEgg(e, eggBitmap, badBitmap, crackedBitmap)
                    }

                    tappedEggs.forEach { tappedEgg ->
                        drawTappedEgg(tappedEgg, eggBitmap, badBitmap)
                    }
                }

                if (state.timeLeft <= 0 && state.isGameStarted) {
                    LaunchedEffect(Unit) {
                        val score = withContext(Dispatchers.Main) { vm.finishAndPersist() }
                        onFinish(score)
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawEgg(
    egg: com.example.clumsychicken.domain.model.FallingEgg,
    eggBitmap: ImageBitmap,
    badBitmap: ImageBitmap,
    crackedBitmap: ImageBitmap
) {
    val cx = egg.x * size.width
    val cy = egg.y * size.height
    val r = egg.radius
    val bmp = when {
        egg.isBroken -> crackedBitmap
        egg.isBad -> badBitmap
        else -> eggBitmap
    }
    val aspect = bmp.width.toFloat() / bmp.height.toFloat()
    val dstHeight = r * 2f
    val dstWidth = dstHeight * aspect
    val dstSize = IntSize(dstWidth.roundToInt(), dstHeight.roundToInt())

    val dstOffset = IntOffset(
        (cx - dstWidth / 2f).roundToInt(),
        (cy - dstHeight / 2f).roundToInt()
    )
    val srcSize = IntSize(bmp.width, bmp.height)
    val srcOffset = IntOffset(0, 0)
    val alpha = if (egg.isBroken) {
        (egg.brokenTtlMs.coerceAtLeast(0) / 2000f).coerceIn(0f, 1f)
    } else 1f

    drawImage(
        image = bmp,
        srcOffset = srcOffset,
        srcSize = srcSize,
        dstOffset = dstOffset,
        dstSize = dstSize,
        alpha = alpha
    )
}

private fun DrawScope.drawTappedEgg(
    tappedEgg: TappedEgg,
    eggBitmap: ImageBitmap,
    badBitmap: ImageBitmap
) {
    val elapsed = System.currentTimeMillis() - tappedEgg.timestamp
    val progress = (elapsed / 300f).coerceIn(0f, 1f)

    val scale = when {
        progress < 0.3f -> 1f + (progress / 0.3f) * 0.3f
        else -> 1.3f * (1f - ((progress - 0.3f) / 0.7f))
    }

    val alpha = (1f - progress).coerceIn(0f, 1f)

    if (alpha > 0f && scale > 0f) {
        val cx = tappedEgg.x * size.width
        val cy = tappedEgg.y * size.height
        val r = tappedEgg.radius * scale
        val bmp = if (tappedEgg.isBad) badBitmap else eggBitmap
        val aspect = bmp.width.toFloat() / bmp.height.toFloat()
        val dstHeight = r * 2f
        val dstWidth = dstHeight * aspect
        val dstSize = IntSize(dstWidth.roundToInt(), dstHeight.roundToInt())

        val dstOffset = IntOffset(
            (cx - dstWidth / 2f).roundToInt(),
            (cy - dstHeight / 2f).roundToInt()
        )
        val srcSize = IntSize(bmp.width, bmp.height)
        val srcOffset = IntOffset(0, 0)

        drawImage(
            image = bmp,
            srcOffset = srcOffset,
            srcSize = srcSize,
            dstOffset = dstOffset,
            dstSize = dstSize,
            alpha = alpha
        )
    }
}

@Composable
private fun TopBar(
    score: Int,
    time: Int,
    isPaused: Boolean,
    onPause: () -> Unit,
    onExit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = GameColors.Primary.copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.shadow(4.dp, RoundedCornerShape(16.dp))
        ) {
            Text(
                text = "${AppText.CURRENT_SCORE}: $score",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = BodyFont,
                    fontSize = 20.sp,
                    color = GameColors.TextLight,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (time <= 10) GameColors.Accent.copy(alpha = 0.9f)
                              else GameColors.Primary.copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.shadow(4.dp, RoundedCornerShape(16.dp))
        ) {
            Text(
                text = "${AppText.TIME}: $time",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = BodyFont,
                    fontSize = 20.sp,
                    color = GameColors.TextLight,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(
                onClick = onPause,
                modifier = Modifier
                    .size(44.dp)
                    .shadow(4.dp, CircleShape)
                    .background(
                        color = GameColors.Primary,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = if (isPaused) AppText.RESUME else AppText.PAUSE,
                    tint = GameColors.TextLight,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(
                onClick = onExit,
                modifier = Modifier
                    .size(44.dp)
                    .shadow(4.dp, CircleShape)
                    .background(
                        color = GameColors.Accent,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = AppText.EXIT,
                    tint = GameColors.TextLight,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
