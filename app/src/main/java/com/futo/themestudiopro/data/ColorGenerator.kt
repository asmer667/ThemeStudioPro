package com.futo.themestudiopro.data

/**
 * مولّد الألوان — يضيف 1000+ لون جديد فوق PresetColors الموجود.
 *
 * ينتج:
 *  - 256 لونًا صلبًا داكنًا (16 hue × 16 درجة)
 *  - 256 لونًا صلبًا فاتحًا
 *  - 256 تدرّجًا داكنًا
 *  - 256 تدرّجًا فاتحًا
 *  = 1024 لونًا إضافيًا
 */
object ColorGenerator {

    private val solidDark: List<SolidPreset> by lazy { generateSolidDark() }
    private val solidLight: List<SolidPreset> by lazy { generateSolidLight() }
    private val gradientDark: List<GradientPreset> by lazy { generateGradientDark() }
    private val gradientLight: List<GradientPreset> by lazy { generateGradientLight() }

    val ALL_SOLID: List<SolidPreset> get() = solidDark + solidLight
    val ALL_GRADIENT: List<GradientPreset> get() = gradientDark + gradientLight
    val totalCount: Int get() = ALL_SOLID.size + ALL_GRADIENT.size

    // ═══ 16 hue أساسية (0..360) ═══
    private val hues = listOf(
        0f,    // أحمر
        20f,   // برتقالي محروق
        40f,   // برتقالي ذهبي
        60f,   // ذهبي
        80f,   // أخضر ليموني
        120f,  // أخضر
        160f,  // أخضر مزرق
        180f,  // سماوي
        200f,  // أزرق فاتح
        220f,  // أزرق
        240f,  // أزرق غامق
        270f,  // بنفسجي غامق
        290f,  // بنفسجي
        320f,  // وردي
        340f,  // وردي محمر
        360f,  // أحمر مجدّد
    )

    // ═══ ألوان صلبة داكنة ═══
    private fun generateSolidDark(): List<SolidPreset> {
        val result = mutableListOf<SolidPreset>()
        for (hue in hues) {
            for (lightness in 1..16) {
                val accentHsl = hsl(hue, 0.85f, 0.55f)
                val bgHsl = hsl(hue, 0.35f, 0.06f + lightness * 0.01f)
                val surfaceHsl = hsl(hue, 0.32f, 0.10f + lightness * 0.012f)
                val containerHsl = hsl(hue, 0.35f, 0.14f + lightness * 0.015f)

                result.add(
                    SolidPreset(
                        name = "GenDark_${hue.toInt()}_$lightness",
                        accent = accentHsl,
                        bg = bgHsl,
                        surface = surfaceHsl,
                        container = containerHsl,
                    )
                )
            }
        }
        return result
    }

    // ═══ ألوان صلبة فاتحة ═══
    private fun generateSolidLight(): List<SolidPreset> {
        val result = mutableListOf<SolidPreset>()
        for (hue in hues) {
            for (lightness in 1..16) {
                val accentHsl = hsl(hue, 0.85f, 0.55f)
                val bgHsl = hsl(hue, 0.30f, 0.97f - lightness * 0.005f)
                val surfaceHsl = hsl(hue, 0.28f, 0.95f - lightness * 0.007f)
                val containerHsl = hsl(hue, 0.35f, 0.92f - lightness * 0.010f)

                result.add(
                    SolidPreset(
                        name = "GenLight_${hue.toInt()}_$lightness",
                        accent = accentHsl,
                        bg = bgHsl,
                        surface = surfaceHsl,
                        container = containerHsl,
                    )
                )
            }
        }
        return result
    }

    // ═══ تدرجات داكنة ═══
    private fun generateGradientDark(): List<GradientPreset> {
        val result = mutableListOf<GradientPreset>()
        for ((i, hue1) in hues.withIndex()) {
            for (j in 1..16) {
                val hue2 = (hue1 + 30f * j) % 360f
                val c1 = hsl(hue1, 0.85f, 0.55f)
                val c2 = hsl(hue2, 0.85f, 0.55f)
                val bg = hsl(hue1, 0.35f, 0.08f)

                result.add(
                    GradientPreset(
                        name = "GenGDark_${i}_$j",
                        color1 = c1,
                        color2 = c2,
                        bg = bg,
                    )
                )
            }
        }
        return result
    }

    // ═══ تدرجات فاتحة ═══
    private fun generateGradientLight(): List<GradientPreset> {
        val result = mutableListOf<GradientPreset>()
        for ((i, hue1) in hues.withIndex()) {
            for (j in 1..16) {
                val hue2 = (hue1 + 30f * j) % 360f
                val c1 = hsl(hue1, 0.75f, 0.65f)
                val c2 = hsl(hue2, 0.75f, 0.65f)
                val bg = hsl(hue1, 0.30f, 0.96f)

                result.add(
                    GradientPreset(
                        name = "GenGLight_${i}_$j",
                        color1 = c1,
                        color2 = c2,
                        bg = bg,
                    )
                )
            }
        }
        return result
    }

    // ═══ تحويل HSL إلى hex #AARRGGBB ═══
    private fun hsl(hue: Float, saturation: Float, lightness: Float): String {
        val h = ((hue % 360f) + 360f) % 360f
        val s = saturation.coerceIn(0f, 1f)
        val l = lightness.coerceIn(0f, 1f)

        val c = (1f - kotlin.math.abs(2f * l - 1f)) * s
        val x = c * (1f - kotlin.math.abs(((h / 60f) % 2f) - 1f))
        val m = l - c / 2f

        val (r1, g1, b1) = when {
            h < 60f -> Triple(c, x, 0f)
            h < 120f -> Triple(x, c, 0f)
            h < 180f -> Triple(0f, c, x)
            h < 240f -> Triple(0f, x, c)
            h < 300f -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }

        val r = ((r1 + m) * 255f).toInt().coerceIn(0, 255)
        val g = ((g1 + m) * 255f).toInt().coerceIn(0, 255)
        val b = ((b1 + m) * 255f).toInt().coerceIn(0, 255)

        return "#FF%02X%02X%02X".format(r, g, b)
    }
}
