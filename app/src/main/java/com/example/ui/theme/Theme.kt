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

private val DarkColorScheme =
  darkColorScheme(
    primary = TelegramLightBlue,
    onPrimary = Color(0xFF0E1621),
    primaryContainer = TelegramBlue,
    onPrimaryContainer = Color.White,
    secondary = TelegramCyan,
    onSecondary = Color(0xFF0E1621),
    background = TelegramDarkBg,
    onBackground = TelegramDarkText,
    surface = TelegramDarkSurface,
    onSurface = TelegramDarkText,
    surfaceVariant = TelegramDarkSurfaceVariant,
    onSurfaceVariant = TelegramDarkTextSecondary,
    outline = CardBorderDark,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = TelegramBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD8ECFC),
    onPrimaryContainer = Color(0xFF0A3C66),
    secondary = TelegramCyan,
    onSecondary = Color.White,
    background = TelegramLightBg,
    onBackground = TelegramLightText,
    surface = TelegramLightSurface,
    onSurface = TelegramLightText,
    surfaceVariant = TelegramLightSurfaceVariant,
    onSurfaceVariant = TelegramLightTextSecondary,
    outline = CardBorderLight,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
