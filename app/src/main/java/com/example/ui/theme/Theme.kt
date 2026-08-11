package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val P2WColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = OnGoldText,
    primaryContainer = DarkGold,
    secondary = VipPurple,
    onSecondary = Color.White,
    tertiary = NeonRedAccent,
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkSurface,
    onSurface = Color.White,
    surfaceVariant = DarkSurfaceVariant
)

@Composable
fun HayDuaTienChoToiTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = P2WColorScheme,
        typography = Typography,
        content = content
    )
}

