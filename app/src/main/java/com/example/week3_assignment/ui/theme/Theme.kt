package com.example.week3_assignment.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = SkyBlue600,
    onPrimary = Color.White,
    primaryContainer = SkyBlue100,
    onPrimaryContainer = Slate900,
    secondary = Indigo600,
    onSecondary = Color.White,
    secondaryContainer = Indigo100,
    onSecondaryContainer = Slate900,
    background = BackgroundLight,
    onBackground = Slate900,
    surface = Color.White,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate600
)

@Composable
fun Week3AssignmentTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
