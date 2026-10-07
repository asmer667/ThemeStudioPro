package com.futo.themestudiopro.utils

import android.graphics.Color
import java.util.Locale
import kotlin.math.roundToInt

/**
 * أدوات تحويل وتعديل الألوان.
 */
object ColorUtils {

    /**
     * تحويل نص hex إلى Int (ARGB).
     * يدعم: #RRGGBB، #AARRGGBB، #RRGGBBAA (FUTO format)
     * يعيد Color.TRANSPARENT إن فشل.
     */
    fun parseColor(input: String?): Int {
        if (input.isNullOrBlank()) return Color.TRANSPARENT

        // إزالة الـ # والمسافات
        val hex = input.trim().removePrefix("#")

        return try {
            when (hex.length) {
                6 -> {
                    // RRGGBB → أضف alpha كامل
                    ("FF$hex").toLong(16).toInt()
                }
                8 -> {
                    // إذا كان أول حرفين alpha صريح (FUTO format)
                    // لكن احتمال أنه AARRGGBB من Android
                    // نتبنى FUTO: RRGGBBAA
                    val rrggbb = hex.substring(0, 6)
                    val aa = hex.substring(6, 8)
                    // نُحوّل من RRGGBBAA إلى AARRGGBB
                    ("$aa$rrggbb").toLong(16).toInt()
                }
                else -> Color.TRANSPARENT
            }
        } catch (e: Exception) {
            Color.TRANSPARENT
        }
    }

    /**
     * تحويل Int إلى نص hex بصيغة FUTO (#RRGGBBAA).
     */
    fun toHexFuto(color: Int): String {
        val a = Color.alpha(color)
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        return String.format(Locale.US, "#%02X%02X%02X%02X", r, g, b, a)
    }

    /**
     * تحويل Int إلى نص hex بصيغة Android (#AARRGGBB).
     */
    fun toHexAndroid(color: Int): String {
        return String.format(Locale.US, "#%08X", color)
    }

    /**
     * تفتيح لون بنسبة.
     * @param factor 0..1 — 0 بدون تغيير، 1 أبيض
     */
    fun lighten(color: Int, factor: Float): Int {
        val f = factor.coerceIn(0f, 1f)
        val r = (Color.red(color) + (255 - Color.red(color)) * f).roundToInt().coerceIn(0, 255)
        val g = (Color.green(color) + (255 - Color.green(color)) * f).roundToInt().coerceIn(0, 255)
        val b = (Color.blue(color) + (255 - Color.blue(color)) * f).roundToInt().coerceIn(0, 255)
        return Color.argb(Color.alpha(color), r, g, b)
    }

    /**
     * تغميق لون بنسبة.
     * @param factor 0..1 — 0 بدون تغيير، 1 أسود
     */
    fun darken(color: Int, factor: Float): Int {
        val f = (1f - factor.coerceIn(0f, 1f))
        val r = (Color.red(color) * f).roundToInt().coerceIn(0, 255)
        val g = (Color.green(color) * f).roundToInt().coerceIn(0, 255)
        val b = (Color.blue(color) * f).roundToInt().coerceIn(0, 255)
        return Color.argb(Color.alpha(color), r, g, b)
    }

    /**
     * هل اللون داكن؟ (يعتمد على الإضاءة النسبية)
     */
    fun isDark(color: Int): Boolean {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        val luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255.0
        return luminance < 0.5
    }

    /**
     * إرجاع لون نص مناسب (أبيض/أسود) على خلفية.
     */
    fun contrastText(background: Int): Int {
        return if (isDark(background)) Color.WHITE else Color.BLACK
    }

    /**
     * خلط لونين بنسبة.
     * @param ratio 0..1 — 0 = color1، 1 = color2
     */
    fun blend(color1: Int, color2: Int, ratio: Float): Int {
        val r = ratio.coerceIn(0f, 1f)
        val a = (Color.alpha(color1) + (Color.alpha(color2) - Color.alpha(color1)) * r).roundToInt()
        val cr = (Color.red(color1) + (Color.red(color2) - Color.red(color1)) * r).roundToInt()
        val cg = (Color.green(color1) + (Color.green(color2) - Color.green(color1)) * r).roundToInt()
        val cb = (Color.blue(color1) + (Color.blue(color2) - Color.blue(color1)) * r).roundToInt()
        return Color.argb(a, cr, cg, cb)
    }

    /**
     * استخراج اللون السائد من تدرّج.
     * (نأخذ اللون الأول، يمكن تطويرها لاحقًا)
     */
    fun dominantFromGradient(color1: Int, color2: Int): Int {
        return blend(color1, color2, 0.5f)
    }

    /**
     * تعديل شفافية لون.
     * @param alpha 0..1
     */
    fun withAlpha(color: Int, alpha: Float): Int {
        val a = (alpha.coerceIn(0f, 1f) * 255).roundToInt()
        return Color.argb(a, Color.red(color), Color.green(color), Color.blue(color))
    }
}
