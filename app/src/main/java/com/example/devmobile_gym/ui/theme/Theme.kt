package com.example.devmobile_gym.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    onPrimary = White,
    background = DarkBackground,
    onBackground = White,
    surface = CardBackground,
    onSurface = White
)

@Composable
fun DevmobileGymTheme(
    darkTheme: Boolean = true, // forçando dark por enquanto
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
