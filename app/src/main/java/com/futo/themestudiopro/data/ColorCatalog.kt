package com.futo.themestudiopro.data

import androidx.compose.ui.graphics.Color

/**
 * مساعدات تنسيق الألوان بين PresetColors و ThemeData.
 */
object ColorCatalog {

    val ALL_SOLID: List<SolidPreset> by lazy {
        PresetColors.ALL_SOLID + ColorGenerator.ALL_SOLID
    }

    val ALL_GRADIENT: List<GradientPreset> by lazy {
        PresetColors.ALL_GRADIENT + ColorGenerator.ALL_GRADIENT
    }

    val solidCount: Int get() = ALL_SOLID.size
    val gradientCount: Int get() = ALL_GRADIENT.size
    val totalCount: Int get() = solidCount + gradientCount

    fun applyBackground(theme: ThemeData, preset: SolidPreset): ThemeData {
        return theme.copy(
            keyboardSurface = preset.surface,
            keyboardSurfaceDim = darkenHex(preset.surface, 0.9f),
            keyboardContainer = preset.container,
            keyboardContainerVariant = preset.container,
            background = preset.bg,
            surface = preset.surface,
            surfaceVariant = preset.container,
        )
    }

    fun applyButton(theme: ThemeData, preset: SolidPreset): ThemeData {
        val pressedHex = darkenHex(preset.accent, 0.85f)
        return theme.copy(
            primary = preset.accent,
            primaryContainer = preset.container,
            keyboardPress = pressedHex,
            keyboardContainerVariant = preset.container,
            onPrimary = computeContrast(preset.accent),
        )
    }

    fun applyBackgroundColor(theme: ThemeData, hex: String): ThemeData {
        return theme.copy(
            background = hex,
            keyboardSurface = hex,
            surface = hex,
        )
    }

    fun applyButtonColor(theme: ThemeData, hex: String): ThemeData {
        val pressed = darkenHex(hex, 0.85f)
        return theme.copy(
            primary = hex,
            keyboardPress = pressed,
            primaryContainer = hex,
            onPrimary = computeContrast(hex),
        )
    }

    fun parseColor(hex: String): Color {
        return try {
            val cleaned = hex.removePrefix("#")
            val value = when (cleaned.length) {
                6 -> cleaned.toLong(16) or 0xFF000000L
                8 -> cleaned.toLong(16)
                else -> 0xFF000000L
            }
            Color(value)
        } catch (e: NumberFormatException) {
            Color.Black
        }
    }

    fun darkenHex(hex: String, factor: Float): String {
        val color = parseColor(hex)
        val r = (color.red * factor * 255f).toInt().coerceIn(0, 255)
        val g = (color.green * factor * 255f).toInt().coerceIn(0, 255)
        val b = (color.blue * factor * 255f).toInt().coerceIn(0, 255)
        return "#FF%02X%02X%02X".format(r, g, b)
    }

    fun computeContrast(hex: String): String {
        val color = parseColor(hex)
        val luminance = 0.299f * color.red + 0.587f * color.green + 0.114f * color.blue
        return if (luminance > 0.5f) "#FF000000" else "#FFFFFFFF"
    }
}
