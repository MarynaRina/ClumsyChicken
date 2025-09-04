package com.example.clumsychicken.core.ui.theme

import androidx.compose.ui.text.font.Font
import com.example.clumsychicken.R
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val TitleFont = FontFamily(Font(R.font.luckiest_guy_regular))
val BodyFont = FontFamily(Font(R.font.fredoka_regular))

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = TitleFont,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)