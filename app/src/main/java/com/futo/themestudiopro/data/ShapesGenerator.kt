package com.futo.themestudiopro.data

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.Path as AndroidPath
import android.graphics.RectF
import com.futo.themestudiopro.data.shapes.ShapeFamilies
import com.futo.themestudiopro.data.shapes.ShapePalette
import com.futo.themestudiopro.data.shapes.ShapeSpec
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * المولّد الرئيسي للأشكال — ينتج 3000+ شكل.
 * يُنتج أيضًا Bitmap PNG عند التصدير.
 */
object ShapesGenerator {

    val shapes: List<ShapeSpec> by lazy { generateAll() }

    fun byFamily(familyId: String): List<ShapeSpec> =
        shapes.filter { it.familyId == familyId }

    val totalCount: Int get() = shapes.size

    // ═══════════════════════════════════════════════════════
    //  توليد Bitmap PNG للتصدير
    // ═══════════════════════════════════════════════════════

    /**
     * يُنتج صورة PNG لشكل واحد.
     */
    fun generatePNG(
        shapeId: String,
        fillColor: Int,
        rotation: Float = 0f,
        sharpness: Float = 0.5f,
        tilt: Float = 0f,
        size: Int = 256,
        is3D: Boolean = false,
    ): ByteArray? {
        return try {
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(AndroidColor.TRANSPARENT)

            val cx = size / 2f
            val cy = size / 2f
            val radius = size * 0.42f

            val familyId = shapeId.substringBefore("_").ifEmpty { "glass" }
            val family = ShapeFamilies.findById(familyId) ?: ShapeFamilies.ALL.first()
            val variant = shapeId.substringAfterLast("_").toIntOrNull() ?: 0

            drawShapeToCanvas(
                canvas = canvas,
                family = family.id,
                variant = variant,
                cx = cx,
                cy = cy,
                radius = radius,
                fillColor = fillColor,
                rotation = rotation,
                sharpness = sharpness,
                tilt = tilt,
                is3D = is3D,
            )

            val stream = java.io.ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            bitmap.recycle()
            stream.toByteArray()
        } catch (e: Exception) {
            null
        }
    }

    private fun drawShapeToCanvas(
        canvas: Canvas,
        family: String,
        variant: Int,
        cx: Float,
        cy: Float,
        radius: Float,
        fillColor: Int,
        rotation: Float,
        sharpness: Float,
        tilt: Float,
        is3D: Boolean,
    ) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = fillColor
        paint.style = Paint.Style.FILL

        val matrix = android.graphics.Matrix()
        matrix.postRotate(rotation + tilt, cx, cy)
        canvas.save()
        canvas.concat(matrix)

        when (family) {
            "glass" -> drawGlassSimple(canvas, paint, cx, cy, radius, variant, fillColor)
            "flat" -> drawFlatSimple(canvas, paint, cx, cy, radius, variant)
            "geometry3d" -> drawCubeSimple(canvas, paint, cx, cy, radius, fillColor)
            "arrow" -> drawArrowSimple(canvas, paint, cx, cy, radius, variant)
            "icon" -> drawIconSimple(canvas, paint, cx, cy, radius, variant, fillColor)
            "neon" -> drawNeonSimple(canvas, paint, cx, cy, radius, fillColor)
            "luxury" -> drawStarSimple(canvas, paint, cx, cy, radius, fillColor)
            "torus3d" -> drawTorusSimple(canvas, paint, cx, cy, radius, fillColor)
            "toggle" -> drawToggleSimple(canvas, paint, cx, cy, radius, fillColor)
            "pattern" -> drawPatternSimple(canvas, paint, cx, cy, radius, fillColor)
            "organic" -> drawLeafSimple(canvas, paint, cx, cy, radius, fillColor)
            else -> drawCircleSimple(canvas, paint, cx, cy, radius, fillColor)
        }

        canvas.restore()
    }

    private fun drawCircleSimple(canvas: Canvas, paint: Paint, cx: Float, cy: Float, r: Float, color: Int) {
        paint.color = color
        canvas.drawCircle(cx, cy, r, paint)
        // لمعة
        paint.color = lightenColor(color, 0.3f)
        canvas.drawCircle(cx - r * 0.3f, cy - r * 0.3f, r * 0.4f, paint)
    }

    private fun drawGlassSimple(canvas: Canvas, paint: Paint, cx: Float, cy: Float, r: Float, variant: Int, color: Int) {
        val path = AndroidPath()
        when (variant % 4) {
            0 -> canvas.drawCircle(cx, cy, r, paint.apply { this.color = color })
            1 -> {
                path.addRoundRect(RectF(cx - r, cy - r, cx + r, cy + r), r * 0.25f, r * 0.25f, AndroidPath.Direction.CW)
                canvas.drawPath(path, paint.apply { this.color = color })
            }
            2 -> {
                path.addRoundRect(RectF(cx - r * 1.2f, cy - r * 0.7f, cx + r * 1.2f, cy + r * 0.7f), r * 0.3f, r * 0.3f, AndroidPath.Direction.CW)
                canvas.drawPath(path, paint.apply { this.color = color })
            }
            else -> {
                path.addOval(RectF(cx - r * 0.9f, cy - r, cx + r * 0.9f, cy + r), AndroidPath.Direction.CW)
                canvas.drawPath(path, paint.apply { this.color = color })
            }
        }
        // لمعة علوية
        paint.color = AndroidColor.argb(120, 255, 255, 255)
        canvas.drawCircle(cx - r * 0.25f, cy - r * 0.4f, r * 0.35f, paint)
    }

    private fun drawFlatSimple(canvas: Canvas, paint: Paint, cx: Float, cy: Float, r: Float, variant: Int) {
        val sides = when (variant % 6) {
            0 -> 3
            1 -> 4
            2 -> 5
            3 -> 6
            4 -> 8
            else -> 12
        }
        val path = AndroidPath()
        for (i in 0 until sides) {
            val angle = 2.0 * Math.PI * i / sides - Math.PI / 2
            val x = cx + (r * cos(angle)).toFloat()
            val y = cy + (r * sin(angle)).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun drawCubeSimple(canvas: Canvas, paint: Paint, cx: Float, cy: Float, r: Float, color: Int) {
        paint.color = color
        canvas.drawRect(cx - r * 0.8f, cy - r * 0.8f, cx + r * 0.8f, cy + r * 0.8f, paint)
        paint.color = lightenColor(color, 0.2f)
        canvas.drawRect(cx - r * 0.8f, cy - r * 0.8f, cx + r * 0.8f, cy, paint)
    }

    private fun drawArrowSimple(canvas: Canvas, paint: Paint, cx: Float, cy: Float, r: Float, variant: Int) {
        val w = r * 1.6f
        val h = r * 1.6f
        val path = AndroidPath()
        when (variant % 4) {
            0 -> { // يمين
                path.moveTo(cx - w / 2f, cy - h / 4f)
                path.lineTo(cx + w / 6f, cy - h / 4f)
                path.lineTo(cx + w / 6f, cy - h / 2f)
                path.lineTo(cx + w / 2f, cy)
                path.lineTo(cx + w / 6f, cy + h / 2f)
                path.lineTo(cx + w / 6f, cy + h / 4f)
                path.lineTo(cx - w / 2f, cy + h / 4f)
            }
            1 -> { // يسار
                path.moveTo(cx + w / 2f, cy - h / 4f)
                path.lineTo(cx - w / 6f, cy - h / 4f)
                path.lineTo(cx - w / 6f, cy - h / 2f)
                path.lineTo(cx - w / 2f, cy)
                path.lineTo(cx - w / 6f, cy + h / 2f)
                path.lineTo(cx - w / 6f, cy + h / 4f)
                path.lineTo(cx + w / 2f, cy + h / 4f)
            }
            2 -> { // أعلى
                path.moveTo(cx - w / 4f, cy + h / 2f)
                path.lineTo(cx - w / 4f, cy - h / 6f)
                path.lineTo(cx - w / 2f, cy - h / 6f)
                path.lineTo(cx, cy - h / 2f)
                path.lineTo(cx + w / 2f, cy - h / 6f)
                path.lineTo(cx + w / 4f, cy - h / 6f)
                path.lineTo(cx + w / 4f, cy + h / 2f)
            }
            else -> { // أسفل
                path.moveTo(cx - w / 4f, cy - h / 2f)
                path.lineTo(cx - w / 4f, cy + h / 6f)
                path.lineTo(cx - w / 2f, cy + h / 6f)
                path.lineTo(cx, cy + h / 2f)
                path.lineTo(cx + w / 2f, cy + h / 6f)
                path.lineTo(cx + w / 4f, cy + h / 6f)
                path.lineTo(cx + w / 4f, cy - h / 2f)
            }
        }
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun drawIconSimple(canvas: Canvas, paint: Paint, cx: Float, cy: Float, r: Float, variant: Int, color: Int) {
        when (variant % 8) {
            0 -> { // صح
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = r * 0.3f
                paint.strokeCap = Paint.Cap.ROUND
                paint.color = color
                val path = AndroidPath()
                path.moveTo(cx - r * 0.6f, cy)
                path.lineTo(cx - r * 0.1f, cy + r * 0.5f)
                path.lineTo(cx + r * 0.7f, cy - r * 0.5f)
                canvas.drawPath(path, paint)
            }
            1 -> { // خطأ
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = r * 0.3f
                paint.color = color
                canvas.drawLine(cx - r * 0.5f, cy - r * 0.5f, cx + r * 0.5f, cy + r * 0.5f, paint)
                canvas.drawLine(cx + r * 0.5f, cy - r * 0.5f, cx - r * 0.5f, cy + r * 0.5f, paint)
            }
            2 -> { // زائد
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = r * 0.25f
                paint.color = color
                canvas.drawLine(cx - r * 0.5f, cy, cx + r * 0.5f, cy, paint)
                canvas.drawLine(cx, cy - r * 0.5f, cx, cy + r * 0.5f, paint)
            }
            else -> drawStarSimple(canvas, paint, cx, cy, r, color)
        }
    }

    private fun drawStarSimple(canvas: Canvas, paint: Paint, cx: Float, cy: Float, r: Float, color: Int) {
        paint.color = color
        paint.style = Paint.Style.FILL
        val path = AndroidPath()
        val points = 5
        val inner = r * 0.42f
        val step = Math.PI / points
        for (i in 0 until points * 2) {
            val rr = if (i % 2 == 0) r else inner
            val angle = step * i - Math.PI / 2
            val x = cx + (rr * cos(angle)).toFloat()
            val y = cy + (rr * sin(angle)).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun drawNeonSimple(canvas: Canvas, paint: Paint, cx: Float, cy: Float, r: Float, color: Int) {
        paint.color = AndroidColor.argb(80, AndroidColor.red(color), AndroidColor.green(color), AndroidColor.blue(color))
        canvas.drawCircle(cx, cy, r * 1.2f, paint)
        paint.color = color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = r * 0.2f
        canvas.drawCircle(cx, cy, r * 0.85f, paint)
        paint.style = Paint.Style.FILL
    }

    private fun drawTorusSimple(canvas: Canvas, paint: Paint, cx: Float, cy: Float, r: Float, color: Int) {
        paint.color = color
        canvas.drawCircle(cx, cy, r, paint)
        paint.color = AndroidColor.argb(180, 0, 0, 0)
        canvas.drawCircle(cx, cy, r * 0.45f, paint)
    }

    private fun drawToggleSimple(canvas: Canvas, paint: Paint, cx: Float, cy: Float, r: Float, color: Int) {
        paint.color = color
        canvas.drawRoundRect(RectF(cx - r, cy - r * 0.5f, cx + r, cy + r * 0.5f), r * 0.5f, r * 0.5f, paint)
        paint.color = AndroidColor.WHITE
        canvas.drawCircle(cx + r * 0.3f, cy, r * 0.4f, paint)
    }

    private fun drawPatternSimple(canvas: Canvas, paint: Paint, cx: Float, cy: Float, r: Float, color: Int) {
        paint.color = color
        canvas.drawRect(cx - r * 0.8f, cy - r * 0.8f, cx + r * 0.8f, cy + r * 0.8f, paint)
        paint.color = AndroidColor.WHITE
        for (i in -2..2) {
            for (j in -2..2) {
                canvas.drawCircle(cx + i * r * 0.3f, cy + j * r * 0.3f, r * 0.05f, paint)
            }
        }
    }

    private fun drawLeafSimple(canvas: Canvas, paint: Paint, cx: Float, cy: Float, r: Float, color: Int) {
        paint.color = color
        val path = AndroidPath()
        path.moveTo(cx, cy - r)
        path.quadTo(cx + r, cy, cx, cy + r)
        path.quadTo(cx - r, cy, cx, cy - r)
        canvas.drawPath(path, paint)
    }

    private fun lightenColor(color: Int, factor: Float): Int {
        val a = AndroidColor.alpha(color)
        val r = (AndroidColor.red(color) + (255 - AndroidColor.red(color)) * factor).toInt().coerceIn(0, 255)
        val g = (AndroidColor.green(color) + (255 - AndroidColor.green(color)) * factor).toInt().coerceIn(0, 255)
        val b = (AndroidColor.blue(color) + (255 - AndroidColor.blue(color)) * factor).toInt().coerceIn(0, 255)
        return AndroidColor.argb(a, r, g, b)
    }

    // ═══════════════════════════════════════════════════════
    //  التوليد (شكل قائمة ShapeSpec — للمعاينة)
    // ═══════════════════════════════════════════════════════

    private fun generateAll(): List<ShapeSpec> {
        val result = mutableListOf<ShapeSpec>()
        val palette = ShapePalette.TRIPLES

        for (family in ShapeFamilies.ALL) {
            val variants = variantCountFor(family.id)
            for (v in 0 until variants) {
                for ((colorIdx, triple) in palette.withIndex()) {
                    result.add(
                        ShapeSpec(
                            id = "${family.id}_${v}_${colorIdx}",
                            familyId = family.id,
                            variantName = "${family.id}_${v}_${colorIdx}",
                            primaryColor = triple.base,
                            secondaryColor = triple.dark,
                            variant = v,
                        )
                    )
                }
            }
        }
        return result
    }

    private fun variantCountFor(familyId: String): Int = when (familyId) {
        "glass"       -> 12
        "geometry3d"  -> 16
        "flat"        -> 14
        "arrow"       -> 12
        "icon"        -> 20
        "keycap"      -> 10
        "gradient"    -> 12
        "torus3d"     -> 10
        "neon"        -> 10
        "organic"     -> 12
        "pattern"     -> 10
        "toggle"      -> 8
        "luxury"      -> 10
        "letter"      -> 8
        "hybrid3d"    -> 10
        "mixed"       -> 12
        else          -> 8
    }
}
