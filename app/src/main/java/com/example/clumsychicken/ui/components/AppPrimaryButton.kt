package com.example.clumsychicken.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.clumsychicken.core.ui.theme.BodyFont
import com.example.clumsychicken.core.ui.theme.GameColors

@Composable
fun AppPrimaryButton(
    text: String,
    interactionSource: MutableInteractionSource,
    pressed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = GameColors.Primary),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (pressed) 2.dp else 8.dp,
            pressedElevation = 2.dp
        ),
        modifier = modifier.height(56.dp)
    ) {
        Text(
            text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = GameColors.TextLight,
            fontFamily = BodyFont
        )
    }
}