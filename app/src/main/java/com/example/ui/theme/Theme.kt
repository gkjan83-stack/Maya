package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MayaPinkPrimary,
    onPrimary = Color.White,
    primaryContainer = MayaPinkSecondary,
    onPrimaryContainer = Color.White,
    secondary = MayaCyanAccent,
    onSecondary = Color.Black,
    tertiary = MayaAmberAccent,
    background = DarkBgPrimary,
    onBackground = TextLightPrimary,
    surface = DarkCardBg,
    onSurface = TextLightPrimary,
    surfaceVariant = DarkCardElevated,
    onSurfaceVariant = TextLightSecondary,
    outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = MayaPinkPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFD8E4),
    onPrimaryContainer = Color(0xFF3B071E),
    secondary = Color(0xFF0097A7),
    onSecondary = Color.White,
    tertiary = MayaAmberAccent,
    background = Color(0xFFFFF7FA),
    onBackground = Color(0xFF1E1420),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1E1420),
    surfaceVariant = Color(0xFFF3E7EF),
    onSurfaceVariant = Color(0xFF5E4957),
    outline = Color(0xFFD6C2CE)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to dark for voice assistant visualizer aesthetic
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
