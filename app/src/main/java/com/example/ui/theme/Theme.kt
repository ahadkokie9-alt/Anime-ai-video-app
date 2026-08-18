package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val SleekColorScheme = darkColorScheme(
    primary = SleekPurplePrimary,
    onPrimary = Color.White,
    primaryContainer = SleekPurpleContainer,
    onPrimaryContainer = SleekPurpleGlow,
    secondary = SleekPurpleLight,
    onSecondary = Color.White,
    secondaryContainer = SleekSurfaceVariant,
    onSecondaryContainer = SleekSlate200,
    tertiary = SleekCyanAccent,
    onTertiary = Color.White,
    background = SleekBackground,
    onBackground = SleekSlate100,
    surface = SleekSurface,
    onSurface = SleekSlate100,
    surfaceVariant = SleekSurfaceVariant,
    onSurfaceVariant = SleekSlate300,
    outline = SleekSlate800,
    outlineVariant = Color(0xFF161E2E),
    error = SleekErrorRed,
    onError = Color.White
)

val SleekShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun AnimeVideoAITheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SleekColorScheme,
        typography = Typography,
        shapes = SleekShapes,
        content = content
    )
}
