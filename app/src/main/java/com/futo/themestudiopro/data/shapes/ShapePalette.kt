package com.futo.themestudiopro.data.shapes

import androidx.compose.ui.graphics.Color

/**
 * ثلاثية لون: أساسي، داكن للظل، فاتح للمعة.
 */
data class ShadeTriple(
    val base: Color,
    val dark: Color,
    val light: Color,
)

/**
 * لوحة الألوان لمولّد الأشكال.
 */
object ShapePalette {

    val TRIPLES: List<ShadeTriple> = listOf(
        triple(0xFF2196F3),
        triple(0xFF03A9F4),
        triple(0xFF00BCD4),
        triple(0xFF009688),
        triple(0xFF4CAF50),
        triple(0xFF8BC34A),
        triple(0xFFFFC107),
        triple(0xFFFF9800),
        triple(0xFFFF5722),
        triple(0xFFF44336),
        triple(0xFFE91E63),
        triple(0xFF9C27B0),
        triple(0xFF673AB7),
        triple(0xFF3F51B5),
        triple(0xFF607D8B),
        triple(0xFF795548),
    )

    val SILVER = ShadeTriple(
        base = Color(0xFFB0BEC5),
        dark = Color(0xFF607D8B),
        light = Color(0xFFECEFF1),
    )

    val GOLD = ShadeTriple(
        base = Color(0xFFFFC107),
        dark = Color(0xFFB28704),
        light = Color(0xFFFFECB3),
    )

    private fun triple(base: Long): ShadeTriple {
        val baseColor = Color(base)
        return ShadeTriple(
            base = baseColor,
            dark = darken(baseColor, 0.55f),
            light = lighten(baseColor, 0.35f),
        )
    }

    private fun darken(color: Color, factor: Float): Color {
        return Color(
            red = color.red * factor,
            green = color.green * factor,
            blue = color.blue * factor,
            alpha = color.alpha,
        )
    }

    private fun lighten(color: Color, factor: Float): Color {
        return Color(
            red = color.red + (1f - color.red) * factor,
            green = color.green + (1f - color.green) * factor,
            blue = color.blue + (1f - color.blue) * factor,
            alpha = color.alpha,
        )
    }
}
