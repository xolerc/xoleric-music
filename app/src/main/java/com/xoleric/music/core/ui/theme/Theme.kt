package com.xoleric.music.core.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = XolericColors.NeonCyan,
    onPrimary = XolericColors.Black,
    primaryContainer = XolericColors.NeonCyan.copy(alpha = 0.12f),
    onPrimaryContainer = XolericColors.NeonCyan,
    secondary = XolericColors.NeonBlue,
    onSecondary = XolericColors.Black,
    secondaryContainer = XolericColors.NeonBlue.copy(alpha = 0.12f),
    onSecondaryContainer = XolericColors.NeonBlue,
    tertiary = XolericColors.NeonViolet,
    onTertiary = XolericColors.Black,
    tertiaryContainer = XolericColors.NeonViolet.copy(alpha = 0.12f),
    onTertiaryContainer = XolericColors.NeonViolet,
    background = XolericColors.Black,
    onBackground = XolericColors.TextPrimary,
    surface = XolericColors.Surface,
    onSurface = XolericColors.TextPrimary,
    surfaceVariant = XolericColors.SurfaceVariant,
    onSurfaceVariant = XolericColors.TextSecondary,
    outline = XolericColors.Divider,
    error = XolericColors.Error,
    onError = XolericColors.TextPrimary,
    errorContainer = XolericColors.Error.copy(alpha = 0.12f),
    onErrorContainer = XolericColors.Error
)

data class XolericAccents(
    val accent: androidx.compose.ui.graphics.Color = XolericColors.NeonCyan,
    val accentAlpha012: androidx.compose.ui.graphics.Color = accent.copy(alpha = 0.12f),
    val accentAlpha020: androidx.compose.ui.graphics.Color = accent.copy(alpha = 0.20f),
    val accentAlpha040: androidx.compose.ui.graphics.Color = accent.copy(alpha = 0.40f),
    val accentAlpha060: androidx.compose.ui.graphics.Color = accent.copy(alpha = 0.60f),
    val accentAlpha080: androidx.compose.ui.graphics.Color = accent.copy(alpha = 0.80f)
)

val LocalAccents = staticCompositionLocalOf { XolericAccents() }

@Composable
fun XolericTheme(
    accentName: String = "cyan",
    content: @Composable () -> Unit
) {
    val accent = XolericColors.fromAccentName(accentName)
    val accents = XolericAccents(accent = accent)
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = XolericColors.Black.toArgb()
            window.navigationBarColor = XolericColors.Black.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = XolericTypography,
        content = {
            androidx.compose.runtime.CompositionLocalProvider(
                LocalAccents provides accents
            ) {
                content()
            }
        }
    )
}
