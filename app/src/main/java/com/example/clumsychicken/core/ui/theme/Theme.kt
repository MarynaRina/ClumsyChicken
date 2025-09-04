package com.example.clumsychicken.core.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val GameColorScheme = lightColorScheme(
    primary = GameColors.Primary,
    tertiary = GameColors.Accent,
    background = GameColors.Background,
    surface = Color(0xFFFFFFFF),
    onPrimary = GameColors.TextLight,
    onSecondary = GameColors.TextLight,
    onTertiary = GameColors.TextLight,
    onBackground = GameColors.TextDark,
    onSurface = GameColors.TextDark
)

@Composable
fun ClumsyChickenTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }

    MaterialTheme(
        colorScheme = GameColorScheme,
        typography = Typography,
        content = content
    )
}