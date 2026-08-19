package com.xoleric.music.core.ui.theme

import androidx.compose.ui.graphics.Color

object XolericColors {
    val Black = Color(0xFF0A0A0A)
    val Graphite = Color(0xFF1A1A2E)
    val Charcoal = Color(0xFF16213E)
    val Surface = Color(0xFF1E1E30)
    val SurfaceVariant = Color(0xFF2A2A40)
    val SurfaceElevated = Color(0xFF32324A)

    val NeonCyan = Color(0xFF00E5FF)
    val NeonBlue = Color(0xFF2979FF)
    val NeonViolet = Color(0xFF7C4DFF)
    val NeonMagenta = Color(0xFFE040FB)

    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xB3FFFFFF)
    val TextTertiary = Color(0x66FFFFFF)

    val Divider = Color(0x1AFFFFFF)
    val Glass = Color(0x14FFFFFF)
    val GlassBorder = Color(0x1AFFFFFF)

    val Error = Color(0xFFFF5252)
    val Success = Color(0xFF69F0AE)
    val Warning = Color(0xFFFFD740)

    val MiniPlayerBg = Color(0xE61A1A2E)
    val BottomNavBg = Color(0xE60A0A0A)

    fun fromAccentName(name: String): Color = when (name) {
        "cyan" -> NeonCyan
        "blue" -> NeonBlue
        "violet" -> NeonViolet
        "magenta" -> NeonMagenta
        else -> NeonCyan
    }
}
