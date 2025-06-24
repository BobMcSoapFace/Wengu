package com.languageApp.wengu.ui.theme

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
    primary = Color(0xFF5548AB),
    secondary = Color(0xFF876DD6),
    tertiary = Color(0xFF9D86E2),

    background = Color(0xFF453A8D),
    surface = Color(0xFF423B70),
    onPrimary = Color(0xFFC4DBD6),
    onSecondary = Color(0xFFC4DBD6),
    onTertiary = Color(0xFF97A5A2),
    onBackground = Color(0xFFC4DBD6),
    onSurface = Color(0xFFC4DBD6),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF397367),
    secondary = Color(0xFF5DA399),
    tertiary = Color(0xFF54494B),

    background = Color(0xFFC4DBD6),
    surface = Color(0xFF8DAAAC),
    onPrimary = Color(0xFFC4DBD6),
    onSecondary = Color.Black,
    onTertiary = Color(0xFF97A5A2),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun WenguTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
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