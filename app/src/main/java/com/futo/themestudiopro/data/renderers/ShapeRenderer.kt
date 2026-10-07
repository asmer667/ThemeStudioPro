package com.futo.themestudiopro.data.renderers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.futo.themestudiopro.data.shapes.RenderStyle
import com.futo.themestudiopro.data.shapes.ShapeParams
import com.futo.themestudiopro.data.shapes.ShadeTriple
import com.futo.themestudiopro.data.renderers.drawGlassCircle
import com.futo.themestudiopro.data.renderers.drawGlassRoundedSquare
import com.futo.themestudiopro.data.renderers.RenderUtils.drawGlassCircle
import com.futo.themestudiopro.data.renderers.RenderUtils.drawGlassRoundedSquare
import com.futo.themestudiopro.data.renderers.RenderUtils.drawSoftShadow
import com.futo.themestudiopro.data.renderers.RenderUtils.drawGloss
import com.futo.themestudiopro.data.renderers.RenderUtils.drawDepthGradient
import com.futo.themestudiopro.data.renderers.RenderUtils.roundedRectPath
import com.futo.themestudiopro.data.renderers.RenderUtils.polygonPath
import com.futo.themestudiopro.data.renderers.RenderUtils.starPath

/**
 * المحرّك الموحّد لرسم الأشكال.
 * يُوزّع العمل على الـ renderers المتخصّصة.
 */
object ShapeRenderer {

    /**
     * نقطة الدخول الرئيسية — تُستدعى من Canvas.
     */
    fun DrawScope.drawShape(
        style: RenderStyle,
        center: Offset,
        radius: Float,
        triple: ShadeTriple,
        variant: Int,
        params: ShapeParams,
    ) {
        when (style) {
            RenderStyle.GLASS       -> drawGlass(center, radius, triple, variant, params)
            RenderStyle.FLAT        -> drawFlat(center, radius, triple, variant, params)
            RenderStyle.GEOMETRY_3D -> drawGeometry3D(center, radius, triple, variant, params)
            RenderStyle.ARROW       -> drawArrow(center, radius, triple, variant, params)
            RenderStyle.ICON        -> drawIcon(center, radius, triple, variant, params)
            RenderStyle.KEYCAP      -> drawKeycap(center, radius, triple, variant, params)
            RenderStyle.GRADIENT    -> drawGradient(center, radius, triple, variant, params)
            RenderStyle.TORUS_3D    -> drawTorus3D(center, radius, triple, variant, params)
            RenderStyle.NEON        -> drawNeon(center, radius, triple, variant, params)
            RenderStyle.ORGANIC     -> drawOrganic(center, radius, triple, variant, params)
            RenderStyle.PATTERN     -> drawPattern(center, radius, triple, variant, params)
            RenderStyle.TOGGLE      -> drawToggle(center, radius, triple, variant, params)
            RenderStyle.LUXURY      -> drawLuxury(center, radius, triple, variant, params)
            RenderStyle.LETTER      -> drawLetter(center, radius, triple, variant, params)
            RenderStyle.HYBRID_3D   -> drawHybrid3D(center, radius, triple, variant, params)
            RenderStyle.MIXED       -> drawMixed(center, radius, triple, variant, params)
        }
    }

    // ═══ 1. GLASS ═══
    private fun DrawScope.drawGlass(
        center: Offset, radius: Float, triple: ShadeTriple, variant: Int, params: ShapeParams,
    ) {
        when (variant % 4) {
            0 -> drawGlassCircle(center, radius, triple.dark, triple.base, triple.light, params.gloss)
            1 -> drawGlassRoundedSquare(
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2f, radius * 2f),
                cornerRadius = radius * params.cornerRadius * 2f,
                dark = triple.dark, base = triple.base, light = triple.light, gloss = params.gloss,
            )
            2 -> {
                // مستطيل زجاجي أفقي
                val w = radius * 1.8f
                val h = radius * 1.0f
                drawGlassRoundedSquare(
                    topLeft = Offset(center.x - w / 2f, center.y - h / 2f),
                    size = Size(w, h),
                    cornerRadius = h * 0.25f,
                    dark = triple.dark, base = triple.base, light = triple.light, gloss = params.gloss,
                )
            }
            3 -> {
                // بيضة زجاجية
                drawGlassRoundedSquare(
                    topLeft = Offset(center.x - radius, center.y - radius * 1.2f),
                    size = Size(radius * 2f, radius * 2.4f),
                    cornerRadius = radius,
                    dark = triple.dark, base = triple.base, light = triple.light, gloss = params.gloss,
                )
            }
        }
    }

    // ═══ 2. FLAT ═══
    private fun DrawScope.drawFlat(
        center: Offset, radius: Float, triple: ShadeTriple, variant: Int, params: ShapeParams,
    ) {
        val path = when (variant % 6) {
            0 -> polygonPath(center, radius, 3)
            1 -> polygonPath(center, radius, 4, 45f)
            2 -> polygonPath(center, radius, 5)
            3 -> polygonPath(center, radius, 6)
            4 -> polygonPath(center, radius, 8)
            else -> polygonPath(center, radius, 12)
        }
        drawPath(path, color = triple.base)
    }

    // ═══ 3. GEOMETRY_3D ═══
    private fun DrawScope.drawGeometry3D(
        center: Offset, radius: Float, triple: ShadeTriple, variant: Int, params: ShapeParams,
    ) {
        when (variant % 4) {
            0 -> {
                // مكعب بسيط
                val w = radius * 1.6f
                val h = radius * 1.6f
                val top = roundedRectPath(
                    Offset(center.x - w / 2f, center.y - h / 2f),
                    Size(w, h * 0.6f),
                    radius * 0.15f,
                )
                val side = roundedRectPath(
                    Offset(center.x - w / 2f, center.y - h * 0.1f),
                    Size(w, h * 0.6f),
                    radius * 0.15f,
                )
                drawPath(top, color = triple.light)
                drawPath(side, color = triple.dark)
            }
            1 -> {
                // هرم
                val path = polygonPath(center, radius, 3)
                drawPath(path, color = triple.base)
                drawPath(polygonPath(center, radius * 0.6f, 3), color = triple.light)
            }
            2 -> {
                // كرة زجاجية
                drawGlassCircle(center, radius, triple.dark, triple.base, triple.light, 0.6f)
            }
            else -> {
                // معين مجسّم
                val path = polygonPath(center, radius, 4)
                drawPath(path, color = triple.base)
                drawPath(polygonPath(center, radius * 0.5f, 4), color = triple.light)
            }
        }
    }

    // ═══ 4. ARROW ═══
    private fun DrawScope.drawArrow(
        center: Offset, radius: Float, triple: ShadeTriple, variant: Int, params: ShapeParams,
    ) {
        val w = radius * 1.6f
        val h = radius * 1.6f
        val path = androidx.compose.ui.graphics.Path()
        when (variant % 4) {
            0 -> {
                // يمين
                path.moveTo(center.x - w / 2f, center.y - h / 4f)
                path.lineTo(center.x + w / 6f, center.y - h / 4f)
                path.lineTo(center.x + w / 6f, center.y - h / 2f)
                path.lineTo(center.x + w / 2f, center.y)
                path.lineTo(center.x + w / 6f, center.y + h / 2f)
                path.lineTo(center.x + w / 6f, center.y + h / 4f)
                path.lineTo(center.x - w / 2f, center.y + h / 4f)
            }
            1 -> {
                // يسار
                path.moveTo(center.x + w / 2f, center.y - h / 4f)
                path.lineTo(center.x - w / 6f, center.y - h / 4f)
                path.lineTo(center.x - w / 6f, center.y - h / 2f)
                path.lineTo(center.x - w / 2f, center.y)
                path.lineTo(center.x - w / 6f, center.y + h / 2f)
                path.lineTo(center.x - w / 6f, center.y + h / 4f)
                path.lineTo(center.x + w / 2f, center.y + h / 4f)
            }
            2 -> {
                // أعلى
                path.moveTo(center.x - w / 4f, center.y + h / 2f)
                path.lineTo(center.x - w / 4f, center.y - h / 6f)
                path.lineTo(center.x - w / 2f, center.y - h / 6f)
                path.lineTo(center.x, center.y - h / 2f)
                path.lineTo(center.x + w / 2f, center.y - h / 6f)
                path.lineTo(center.x + w / 4f, center.y - h / 6f)
                path.lineTo(center.x + w / 4f, center.y + h / 2f)
            }
            else -> {
                // أسفل
                path.moveTo(center.x - w / 4f, center.y - h / 2f)
                path.lineTo(center.x - w / 4f, center.y + h / 6f)
                path.lineTo(center.x - w / 2f, center.y + h / 6f)
                path.lineTo(center.x, center.y + h / 2f)
                path.lineTo(center.x + w / 2f, center.y + h / 6f)
                path.lineTo(center.x + w / 4f, center.y + h / 6f)
                path.lineTo(center.x + w / 4f, center.y - h / 2f)
            }
        }
        path.close()
        drawPath(path, color = triple.base)
    }

    // ═══ 5. ICON ═══
    private fun DrawScope.drawIcon(
        center: Offset, radius: Float, triple: ShadeTriple, variant: Int, params: ShapeParams,
    ) {
        when (variant % 8) {
            0 -> {
                // صح
                val path = androidx.compose.ui.graphics.Path()
                path.moveTo(center.x - radius * 0.6f, center.y)
                path.lineTo(center.x - radius * 0.1f, center.y + radius * 0.5f)
                path.lineTo(center.x + radius * 0.7f, center.y - radius * 0.5f)
                drawPath(
                    path,
                    color = triple.base,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = radius * 0.3f,
                    ),
                )
            }
            1 -> {
                // خطأ
                drawLine(
                    color = triple.base,
                    start = Offset(center.x - radius * 0.5f, center.y - radius * 0.5f),
                    end = Offset(center.x + radius * 0.5f, center.y + radius * 0.5f),
                    strokeWidth = radius * 0.3f,
                )
                drawLine(
                    color = triple.base,
                    start = Offset(center.x + radius * 0.5f, center.y - radius * 0.5f),
                    end = Offset(center.x - radius * 0.5f, center.y + radius * 0.5f),
                    strokeWidth = radius * 0.3f,
                )
            }
            2 -> {
                // زائد
                drawLine(
                    color = triple.base,
                    start = Offset(center.x - radius * 0.5f, center.y),
                    end = Offset(center.x + radius * 0.5f, center.y),
                    strokeWidth = radius * 0.25f,
                )
                drawLine(
                    color = triple.base,
                    start = Offset(center.x, center.y - radius * 0.5f),
                    end = Offset(center.x, center.y + radius * 0.5f),
                    strokeWidth = radius * 0.25f,
                )
            }
            3 -> {
                // ناقص
                drawLine(
                    color = triple.base,
                    start = Offset(center.x - radius * 0.5f, center.y),
                    end = Offset(center.x + radius * 0.5f, center.y),
                    strokeWidth = radius * 0.25f,
                )
            }
            4 -> {
                // نجمة
                drawPath(
                    starPath(center, radius, radius * 0.45f, 5),
                    color = triple.base,
                )
            }
            5 -> {
                // قلب
                val path = androidx.compose.ui.graphics.Path()
                path.moveTo(center.x, center.y + radius * 0.6f)
                path.cubicTo(
                    center.x - radius * 1.2f, center.y - radius * 0.2f,
                    center.x - radius * 0.5f, center.y - radius * 0.9f,
                    center.x, center.y - radius * 0.25f,
                )
                path.cubicTo(
                    center.x + radius * 0.5f, center.y - radius * 0.9f,
                    center.x + radius * 1.2f, center.y - radius * 0.2f,
                    center.x, center.y + radius * 0.6f,
                )
                drawPath(path, color = triple.base)
            }
            6 -> {
                // مربع مدوّر
                drawPath(
                    roundedRectPath(
                        Offset(center.x - radius * 0.6f, center.y - radius * 0.6f),
                        Size(radius * 1.2f, radius * 1.2f),
                        radius * 0.25f,
                    ),
                    color = triple.base,
                )
            }
            else -> {
                // جرس
                drawPath(
                    polygonPath(center, radius * 0.7f, 5),
                    color = triple.base,
                )
            }
        }
    }

    // ═══ 6. KEYCAP ═══
    private fun DrawScope.drawKeycap(
        center: Offset, radius: Float, triple: ShadeTriple, variant: Int, params: ShapeParams,
    ) {
        val w = radius * 2f
        val h = radius * 1.4f
        val path = roundedRectPath(
            Offset(center.x - w / 2f, center.y - h / 2f),
            Size(w, h),
            radius * 0.3f,
        )
        drawPath(path, color = triple.base)
        drawPath(
            path,
            color = triple.light,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = radius * 0.08f),
        )
    }

    // ═══ 7. GRADIENT ═══
    private fun DrawScope.drawGradient(
        center: Offset, radius: Float, triple: ShadeTriple, variant: Int, params: ShapeParams,
    ) {
        val w = radius * 2f
        val h = radius * 1.3f
        val path = roundedRectPath(
            Offset(center.x - w / 2f, center.y - h / 2f),
            Size(w, h),
            radius * 0.2f,
        )
        drawPath(
            path,
            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                colors = listOf(triple.light, triple.base, triple.dark),
                start = Offset(center.x - w / 2f, center.y - h / 2f),
                end = Offset(center.x + w / 2f, center.y + h / 2f),
            ),
        )
    }

    // ═══ 8. TORUS_3D ═══
    private fun DrawScope.drawTorus3D(
        center: Offset, radius: Float, triple: ShadeTriple, variant: Int, params: ShapeParams,
    ) {
        // حلقة
        drawCircle(
            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                colors = listOf(triple.light, triple.base, triple.dark),
                start = Offset(center.x - radius, center.y - radius),
                end = Offset(center.x + radius, center.y + radius),
            ),
            radius = radius,
            center = center,
        )
        drawCircle(
            color = Color.Black.copy(alpha = 0.35f),
            radius = radius * 0.45f,
            center = center,
        )
    }

    // ═══ 9. NEON ═══
    private fun DrawScope.drawNeon(
        center: Offset, radius: Float, triple: ShadeTriple, variant: Int, params: ShapeParams,
    ) {
        val glow = triple.base
        // هالة خارجية
        drawCircle(
            brush = androidx.compose.ui.graphics.Brush.radialGradient(
                colors = listOf(glow.copy(alpha = 0.5f), Color.Transparent),
                center = center,
                radius = radius * 1.3f,
            ),
            radius = radius * 1.3f,
            center = center,
        )
        // الحلقة
        drawCircle(
            color = glow,
            radius = radius * 0.85f,
            center = center,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = radius * 0.2f),
        )
    }

    // ═══ 10. ORGANIC ═══
    private fun DrawScope.drawOrganic(
        center: Offset, radius: Float, triple: ShadeTriple, variant: Int, params: ShapeParams,
    ) {
        when (variant % 4) {
            0 -> {
                // ورقة
                val path = androidx.compose.ui.graphics.Path()
                path.moveTo(center.x, center.y - radius)
                path.quadraticBezierTo(center.x + radius, center.y, center.x, center.y + radius)
                path.quadraticBezierTo(center.x - radius, center.y, center.x, center.y - radius)
                drawPath(path, color = triple.base)
            }
            1 -> {
                // قطرة
                val path = androidx.compose.ui.graphics.Path()
                path.moveTo(center.x, center.y - radius)
                path.quadraticBezierTo(center.x + radius, center.y - radius * 0.3f, center.x + radius * 0.7f, center.y + radius * 0.5f)
                path.quadraticBezierTo(center.x, center.y + radius * 1.2f, center.x - radius * 0.7f, center.y + radius * 0.5f)
                path.quadraticBezierTo(center.x - radius, center.y - radius * 0.3f, center.x, center.y - radius)
                drawPath(path, color = triple.base)
            }
            2 -> {
                // نجمة زهرة
                drawPath(starPath(center, radius, radius * 0.5f, 6), color = triple.base)
            }
            else -> {
                // فاكهة (تفاحة)
                drawCircle(color = triple.base, radius = radius * 0.9f, center = center)
                drawLine(
                    color = Color(0xFF4CAF50),
                    start = Offset(center.x, center.y - radius * 0.9f),
                    end = Offset(center.x + radius * 0.3f, center.y - radius * 1.3f),
                    strokeWidth = radius * 0.12f,
                )
            }
        }
    }

    // ═══ 11. PATTERN ═══
    private fun DrawScope.drawPattern(
        center: Offset, radius: Float, triple: ShadeTriple, variant: Int, params: ShapeParams,
    ) {
        val size = radius * 1.6f
        val topLeft = Offset(center.x - size / 2f, center.y - size / 2f)
        val path = roundedRectPath(topLeft, Size(size, size), radius * 0.15f)
        drawPath(path, color = triple.base.copy(alpha = 0.9f))

        when (variant % 4) {
            0 -> {
                // نقاط
                val cols = 5
                val step = size / cols
                for (i in 1 until cols) {
                    for (j in 1 until cols) {
                        drawCircle(
                            color = triple.light,
                            radius = size * 0.03f,
                            center = Offset(topLeft.x + step * i, topLeft.y + step * j),
                        )
                    }
                }
            }
            1 -> {
                // خطوط قطرية
                val cols = 5
                val step = size / cols
                for (i in 0 until cols) {
                    drawLine(
                        color = triple.light,
                        start = Offset(topLeft.x + i * step, topLeft.y),
                        end = Offset(topLeft.x, topLeft.y + i * step),
                        strokeWidth = size * 0.03f,
                    )
                }
            }
            2 -> {
                // موجات
                val step = size / 4f
                for (i in 0..4) {
                    drawLine(
                        color = triple.light,
                        start = Offset(topLeft.x, topLeft.y + step * i + step * 0.5f),
                        end = Offset(topLeft.x + size, topLeft.y + step * i + step * 0.5f),
                        strokeWidth = size * 0.02f,
                    )
                }
            }
            else -> {
                // شبكة
                val cols = 4
                val step = size / cols
                for (i in 0..cols) {
                    drawLine(color = triple.light, start = Offset(topLeft.x + step * i, topLeft.y), end = Offset(topLeft.x + step * i, topLeft.y + size), strokeWidth = size * 0.02f)
                    drawLine(color = triple.light, start = Offset(topLeft.x, topLeft.y + step * i), end = Offset(topLeft.x + size, topLeft.y + step * i), strokeWidth = size * 0.02f)
                }
            }
        }
    }

    // ═══ 12. TOGGLE ═══
    private fun DrawScope.drawToggle(
        center: Offset, radius: Float, triple: ShadeTriple, variant: Int, params: ShapeParams,
    ) {
        val w = radius * 2f
        val h = radius * 1f
        val path = roundedRectPath(
            Offset(center.x - w / 2f, center.y - h / 2f),
            Size(w, h),
            h / 2f,
        )
        drawPath(path, color = triple.dark)
        // المقبض
        val knobX = if (variant % 2 == 0) center.x - w * 0.3f else center.x + w * 0.3f
        drawCircle(color = triple.light, radius = h * 0.4f, center = Offset(knobX, center.y))
    }

    // ═══ 13. LUXURY ═══
    private fun DrawScope.drawLuxury(
        center: Offset, radius: Float, triple: ShadeTriple, variant: Int, params: ShapeParams,
    ) {
        // نجمة ذهبية
        val path = starPath(center, radius, radius * 0.42f, 5)
        drawPath(
            path,
            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                colors = listOf(Color(0xFFFFECB3), Color(0xFFFFC107), Color(0xFFB28704)),
                start = Offset(center.x - radius, center.y - radius),
                end = Offset(center.x + radius, center.y + radius),
            ),
        )
    }

    // ═══ 14. LETTER ═══
    private fun DrawScope.drawLetter(
        center: Offset, radius: Float, triple: ShadeTriple, variant: Int, params: ShapeParams,
    ) {
        // دائرة ذهبية مع حرف
        drawCircle(color = triple.base, radius = radius, center = center)
        drawCircle(
            color = triple.light,
            radius = radius,
            center = center,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = radius * 0.1f),
        )
    }

    // ═══ 15. HYBRID_3D ═══
    private fun DrawScope.drawHybrid3D(
        center: Offset, radius: Float, triple: ShadeTriple, variant: Int, params: ShapeParams,
    ) {
        // مكعبان متراكبان
        val offset = radius * 0.35f
        val top = roundedRectPath(
            Offset(center.x - radius * 0.7f + offset, center.y - radius * 0.7f),
            Size(radius * 1.4f, radius * 1.4f),
            radius * 0.15f,
        )
        val bottom = roundedRectPath(
            Offset(center.x - radius * 0.7f - offset, center.y - radius * 0.7f + offset),
            Size(radius * 1.4f, radius * 1.4f),
            radius * 0.15f,
        )
        drawPath(bottom, color = triple.dark)
        drawPath(top, color = triple.base)
    }

    // ═══ 16. MIXED ═══
    private fun DrawScope.drawMixed(
        center: Offset, radius: Float, triple: ShadeTriple, variant: Int, params: ShapeParams,
    ) {
        when (variant % 4) {
            0 -> drawPath(starPath(center, radius, radius * 0.45f, 5), color = triple.base)
            1 -> drawCircle(color = triple.base, radius = radius * 0.9f, center = center)
            2 -> drawPath(polygonPath(center, radius, 6), color = triple.base)
            else -> drawPath(polygonPath(center, radius, 3), color = triple.base)
        }
    }
}
