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
    primary = AmberGold500,
    onPrimary = SlateNavy900,
    primaryContainer = SlateNavy800,
    onPrimaryContainer = AmberGold100,
    secondary = GlobalBlue600,
    onSecondary = Color.White,
    background = SlateNavy900,
    surface = SlateNavy800,
    onBackground = Color.White,
    onSurface = Color.White,
    outline = SlateNavy700
)

private val LightColorScheme = lightColorScheme(
    primary = SlateNavy900,
    onPrimary = Color.White,
    primaryContainer = SlateNavy100,
    onPrimaryContainer = SlateNavy900,
    secondary = AmberGold600,
    onSecondary = Color.White,
    tertiary = GlobalBlue600,
    background = SlateNavy50,
    surface = Color.White,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    outline = CardBorderLight
)

@Composable
fun WorldNewsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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

