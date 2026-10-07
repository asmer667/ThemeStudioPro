package com.futo.themestudiopro.data

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import java.io.ByteArrayOutputStream
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * تعريف شكل هندسي.
 */
data class ShapeDef(
    val id: String,
    val name: String,
    val family: String,
    val emoji: String = "◯",
    val param1: Float = 0f,
    val param2: Float = 0f,
)

/**
 * مولّد 3000+ شكل هندسي — 2D و 3D.
 * الأشكال تُولَّد برمجيًا عند الحاجة (لا تُخزَّن كصور).
 */
object ShapesGenerator {

    const val CANVAS_SIZE = 256

    // عائلات الأشكال
    private val SHAPE_FAMILIES = listOf(
        "polygon", "star", "rounded", "ellipse", "flower", "gear",
        "cross", "heart", "shield", "arrow", "blob", "capsule",
        "diamond", "teardrop", "wave", "cloud", "spiral", "crescent",
        "burst", "ring", "key", "leaf", "hexagon", "octagon",
        "pentagon", "triangle", "square", "circle", "pill", "star5",
    )

    /**
     * توليد قائمة كل الأشكال (3000+).
     * يمكن استدعاؤها مرة واحدة وحفظها في lazy.
     */
    val shapes: List<ShapeDef> by lazy {
        buildList {
            // مضلعات منتظمة: 3..40 ضلعًا = 38 شكلًا
            for (n in 3..40) {
                add(ShapeDef("poly$n", "مضلع $n", "polygon", "⬡", n.toFloat()))
            }
            // نجوم: 5..30 رأسًا × 5 نسب داخلية = 130 شكلًا
            for (pts in 5..30) {
                for (ir in listOf(0.25f, 0.35f, 0.45f, 0.55f, 0.70f)) {
                    add(ShapeDef("star${pts}_${(ir * 100).toInt()}", "نجمة $pts", "star", "★", pts.toFloat(), ir))
                }
            }
            // مستطيلات مستديرة: 20 درجة = 20 شكلًا
            for (i in 0..19) {
                add(ShapeDef("rrect$i", "مستدير $i", "rounded", "▢", i / 19f))
            }
            // بيضاويات: 12 نسبة = 12 شكلًا
            for (i in 0..11) {
                add(ShapeDef("ellipse$i", "بيضاوي $i", "ellipse", "⬭", 0.4f + i * 0.1f))
            }
            // زهور: 5..24 بتلة × 4 أعماق = 80 شكلًا
            for (pt in 5..24) {
                for (d in listOf(0.25f, 0.40f, 0.55f, 0.70f)) {
                    add(ShapeDef("flower${pt}_${(d * 100).toInt()}", "زهرة $pt", "flower", "✿", pt.toFloat(), d))
                }
            }
            // تروس: 6..36 سنًا × 3 أعماق = 93 شكلًا
            for (t in 6..36) {
                for (d in listOf(0.12f, 0.20f, 0.28f)) {
                    add(ShapeDef("gear${t}_${(d * 100).toInt()}", "ترس $t", "gear", "⚙", t.toFloat(), d))
                }
            }
            // صلبان: 10 سماكات = 10
            for (i in 0..9) {
                add(ShapeDef("cross$i", "صليب $i", "cross", "✚", 0.20f + i * 0.06f))
            }
            // قلوب: 15 = 15
            for (i in 0..14) {
                add(ShapeDef("heart$i", "قلب $i", "heart", "♥", i / 14f))
            }
            // دروع: 6×3 = 18
            for (i in 0..5) {
                for (pi in 0..2) {
                    add(ShapeDef("shield${i}_$pi", "درع $i$pi", "shield", "🛡", 0.4f + i * 0.12f, pi / 2f))
                }
            }
            // أسهم: 4 اتجاهات × 10 سماكات = 40
            for (dir in 0..3) {
                for (i in 0..9) {
                    add(ShapeDef("arrow${dir}_$i", "سهم $dir-$i", "arrow", "➤", dir.toFloat(), 0.15f + i * 0.08f))
                }
            }
            // فقاعات: 5×12 = 60
            for (seed in 0..4) {
                for (i in 0..11) {
                    add(ShapeDef("blob${seed}_$i", "فقاعة $seed-$i", "blob", "◍", seed.toFloat(), i / 11f))
                }
            }
            // كبسولات: 10
            for (i in 0..9) {
                add(ShapeDef("capsule$i", "كبسولة $i", "capsule", "▬", 0.25f + i * 0.08f))
            }
            // معيّنات: 12
            for (i in 0..11) {
                add(ShapeDef("diamond$i", "معيّن $i", "diamond", "◆", i / 11f))
            }
            // قطرات: 15
            for (i in 0..14) {
                add(ShapeDef("teardrop$i", "قطرة $i", "teardrop", "💧", i / 14f))
            }
            // أمواج: 15
            for (i in 0..14) {
                add(ShapeDef("wave$i", "موجة $i", "wave", "〜", i / 14f))
            }
            // سحابات: 9×2 = 18
            for (b in 4..12) {
                for (v in 0..1) {
                    add(ShapeDef("cloud${b}_$v", "سحابة $b-$v", "cloud", "☁", b.toFloat(), v.toFloat()))
                }
            }
            // حلزونات: 20
            for (i in 0..19) {
                add(ShapeDef("spiral$i", "حلزون $i", "spiral", "◎", i / 19f))
            }
            // أهلة: 15
            for (i in 0..14) {
                add(ShapeDef("crescent$i", "هلال $i", "crescent", "☾", i / 14f))
            }
            // انفجارات: 20
            for (i in 0..19) {
                add(ShapeDef("burst$i", "انفجار $i", "burst", "✦", i / 19f))
            }
            // حلقات: 15
            for (i in 0..14) {
                add(ShapeDef("ring$i", "حلقة $i", "ring", "◯", i / 14f))
            }
            // مفاتيح: 10
            for (i in 0..9) {
                add(ShapeDef("key$i", "مفتاح $i", "key", "🗝", i / 9f))
            }
            // أوراق: 15
            for (i in 0..14) {
                add(ShapeDef("leaf$i", "ورقة $i", "leaf", "🍃", i / 14f))
            }

            // ═══ نسخ 3D للأشكال الـ50 الأولى ═══
            val first50 = take(50).toList()
            for (base in first50) {
                add(base.copy(id = "3d_${base.id}", name = "3D ${base.name}"))
            }
        }
    }

    /**
     * بناء Path من ShapeDef مع تطبيق sharpness.
     */
    private fun buildPath(def: ShapeDef, sharpness: Float): Path {
        val p = Path()
        val cx = CANVAS_SIZE / 2f
        val cy = CANVAS_SIZE / 2f
        val r = CANVAS_SIZE / 2f - 16f
        val s = sharpness.coerceIn(0f, 1f)

        when (def.family) {
            "polygon" -> regularPolygon(p, cx, cy, r, def.param1.toInt())
            "star", "star5" -> star(p, cx, cy, r, r * def.param2, def.param1.toInt())
            "rounded" -> {
                val cr = r * (0.02f + def.param1 * 0.98f)
                p.addRoundRect(RectF(16f, 16f, CANVAS_SIZE - 16f, CANVAS_SIZE - 16f), cr, cr, Path.Direction.CW)
            }
            "ellipse" -> {
                val rx = r * def.param1
                val ry = r / def.param1
                p.addOval(RectF(cx - rx, cy - ry, cx + rx, cy + ry), Path.Direction.CW)
            }
            "flower" -> flower(p, cx, cy, r, def.param1.toInt(), def.param2)
            "gear" -> gear(p, cx, cy, r, def.param1.toInt(), def.param2)
            "cross" -> cross(p, cx, cy, r, def.param1)
            "heart" -> heart(p, cx, cy, r, def.param1)
            "shield" -> shield(p, cx, cy, r, def.param1, def.param2)
            "arrow" -> arrow(p, cx, cy, r, def.param1.toInt(), def.param2)
            "blob" -> blob(p, cx, cy, r, def.param1.toInt(), def.param2)
            "capsule" -> {
                val h = r * def.param1
                p.addRoundRect(RectF(16f, cy - h, CANVAS_SIZE - 16f, cy + h), h, h, Path.Direction.CW)
            }
            "diamond" -> diamond(p, cx, cy, r, def.param1)
            "teardrop" -> teardrop(p, cx, cy, r, def.param1)
            "wave" -> wave(p, cx, cy, r, def.param1)
            "cloud" -> cloud(p, cx, cy, r, def.param1.toInt(), def.param2 > 0.5f)
            "spiral" -> spiral(p, cx, cy, r, def.param1)
            "crescent" -> crescent(p, cx, cy, r, def.param1)
            "burst" -> burst(p, cx, cy, r, def.param1)
            "ring" -> ring(p, cx, cy, r, def.param1)
            "key" -> keyShape(p, cx, cy, r, def.param1)
            "leaf" -> leaf(p, cx, cy, r, def.param1)
            else -> {
                val cr = r * (0.05f + s * 0.95f)
                p.addRoundRect(RectF(16f, 16f, CANVAS_SIZE - 16f, CANVAS_SIZE - 16f), cr, cr, Path.Direction.CW)
            }
        }
        return p
    }

    // ═══════════ الدوال المساعدة ═══════════

    private fun regularPolygon(p: Path, cx: Float, cy: Float, r: Float, sides: Int) {
        if (sides < 3) return
        for (i in 0 until sides) {
            val a = Math.toRadians(-90.0 + 360.0 * i / sides)
            val x = cx + r * cos(a).toFloat()
            val y = cy + r * sin(a).toFloat()
            if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
        }
        p.close()
    }

    private fun star(p: Path, cx: Float, cy: Float, ro: Float, ri: Float, pts: Int) {
        if (pts < 3) return
        val total = pts * 2
        for (i in 0 until total) {
            val radius = if (i % 2 == 0) ro else ri
            val a = Math.toRadians(-90.0 + 180.0 * i / pts)
            val x = cx + radius * cos(a).toFloat()
            val y = cy + radius * sin(a).toFloat()
            if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
        }
        p.close()
    }

    private fun flower(p: Path, cx: Float, cy: Float, r: Float, petals: Int, depth: Float) {
        if (petals < 3) return
        val steps = petals * 20
        for (i in 0 until steps) {
            val angle = 2.0 * Math.PI * i / steps
            val phase = cos(petals * angle).toFloat()
            val radius = r * (1f - depth + depth * phase)
            val x = cx + radius * cos(angle).toFloat()
            val y = cy + radius * sin(angle).toFloat()
            if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
        }
        p.close()
    }

    private fun gear(p: Path, cx: Float, cy: Float, r: Float, teeth: Int, depth: Float) {
        if (teeth < 3) return
        val steps = teeth * 4
        for (i in 0 until steps) {
            val angle = 2.0 * Math.PI * i / steps
            val phase = i % 4
            val radius = if (phase == 0 || phase == 1) r else r * (1f - depth)
            val x = cx + radius * cos(angle).toFloat()
            val y = cy + radius * sin(angle).toFloat()
            if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
        }
        p.close()
    }

    private fun cross(p: Path, cx: Float, cy: Float, r: Float, thickness: Float) {
        val w = r * thickness.coerceIn(0.1f, 0.9f)
        p.moveTo(cx - w, cy - r)
        p.lineTo(cx + w, cy - r)
        p.lineTo(cx + w, cy - w)
        p.lineTo(cx + r, cy - w)
        p.lineTo(cx + r, cy + w)
        p.lineTo(cx + w, cy + w)
        p.lineTo(cx + w, cy + r)
        p.lineTo(cx - w, cy + r)
        p.lineTo(cx - w, cy + w)
        p.lineTo(cx - r, cy + w)
        p.lineTo(cx - r, cy - w)
        p.lineTo(cx - w, cy - w)
        p.close()
    }

    private fun heart(p: Path, cx: Float, cy: Float, r: Float, sharpness: Float) {
        val dipY = cy - r * (0.3f - sharpness * 0.3f)
        val bottomY = cy + r
        p.moveTo(cx, dipY)
        p.cubicTo(cx - r * 1.2f, cy - r * 1.4f, cx - r * 1.2f, cy + r * 0.2f, cx, bottomY)
        p.cubicTo(cx + r * 1.2f, cy + r * 0.2f, cx + r * 1.2f, cy - r * 1.4f, cx, dipY)
        p.close()
    }

    private fun shield(p: Path, cx: Float, cy: Float, r: Float, width: Float, pointiness: Float) {
        val w = r * width.coerceIn(0.3f, 1f)
        val top = cy - r
        val bot = cy + r
        p.moveTo(cx - w, top)
        p.lineTo(cx + w, top)
        p.lineTo(cx + w, cy + r * (0.2f + pointiness * 0.3f))
        p.quadTo(cx + w, bot - (1f - pointiness) * r * 0.2f, cx, bot)
        p.quadTo(cx - w, bot - (1f - pointiness) * r * 0.2f, cx - w, cy + r * (0.2f + pointiness * 0.3f))
        p.close()
    }

    private fun arrow(p: Path, cx: Float, cy: Float, r: Float, dir: Int, thickness: Float) {
        val w = r * thickness.coerceIn(0.1f, 0.6f)
        when (dir) {
            0 -> {
                p.moveTo(cx - r, cy - w); p.lineTo(cx + r * 0.2f, cy - w)
                p.lineTo(cx + r * 0.2f, cy - w * 2f); p.lineTo(cx + r, cy)
                p.lineTo(cx + r * 0.2f, cy + w * 2f); p.lineTo(cx + r * 0.2f, cy + w)
                p.lineTo(cx - r, cy + w); p.close()
            }
            1 -> {
                p.moveTo(cx + r, cy - w); p.lineTo(cx - r * 0.2f, cy - w)
                p.lineTo(cx - r * 0.2f, cy - w * 2f); p.lineTo(cx - r, cy)
                p.lineTo(cx - r * 0.2f, cy + w * 2f); p.lineTo(cx - r * 0.2f, cy + w)
                p.lineTo(cx + r, cy + w); p.close()
            }
            2 -> {
                p.moveTo(cx - w, cy + r); p.lineTo(cx - w, cy - r * 0.2f)
                p.lineTo(cx - w * 2f, cy - r * 0.2f); p.lineTo(cx, cy - r)
                p.lineTo(cx + w * 2f, cy - r * 0.2f); p.lineTo(cx + w, cy - r * 0.2f)
                p.lineTo(cx + w, cy + r); p.close()
            }
            3 -> {
                p.moveTo(cx - w, cy - r); p.lineTo(cx - w, cy + r * 0.2f)
                p.lineTo(cx - w * 2f, cy + r * 0.2f); p.lineTo(cx, cy + r)
                p.lineTo(cx + w * 2f, cy + r * 0.2f); p.lineTo(cx + w, cy + r * 0.2f)
                p.lineTo(cx + w, cy - r); p.close()
            }
        }
    }

    private fun blob(p: Path, cx: Float, cy: Float, r: Float, seed: Int, amp: Float) {
        val steps = 64
        val phase1 = seed * 1.3f
        val phase2 = seed * 2.1f
        for (i in 0 until steps) {
            val a = 2.0 * Math.PI * i / steps
            val noise = sin(3 * a + phase1).toFloat() * 0.5f +
                    sin(5 * a + phase2).toFloat() * 0.3f +
                    sin(7 * a).toFloat() * 0.2f
            val radius = r * (1f + amp * noise)
            val x = cx + radius * cos(a).toFloat()
            val y = cy + radius * sin(a).toFloat()
            if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
        }
        p.close()
    }

    private fun diamond(p: Path, cx: Float, cy: Float, r: Float, roundness: Float) {
        val cr = r * roundness * 0.4f
        p.moveTo(cx, cy - r)
        p.lineTo(cx + r - cr, cy - cr); p.quadTo(cx + r, cy, cx + r, cy + cr)
        p.lineTo(cx + cr, cy + r - cr); p.quadTo(cx, cy + r, cx - cr, cy + r - cr)
        p.lineTo(cx - r + cr, cy + cr); p.quadTo(cx - r, cy, cx - r, cy - cr)
        p.lineTo(cx - cr, cy - r + cr); p.quadTo(cx, cy - r, cx, cy - r)
        p.close()
    }

    private fun teardrop(p: Path, cx: Float, cy: Float, r: Float, pointiness: Float) {
        val topY = cy - r - r * pointiness * 0.8f
        val botY = cy + r
        p.moveTo(cx, topY)
        p.cubicTo(cx + r * 0.9f, cy - r * 0.5f, cx + r, cy + r * 0.5f, cx, botY)
        p.cubicTo(cx - r, cy + r * 0.5f, cx - r * 0.9f, cy - r * 0.5f, cx, topY)
        p.close()
    }

    private fun wave(p: Path, cx: Float, cy: Float, r: Float, amp: Float) {
        val left = 16f
        val right = CANVAS_SIZE - 16f
        val top = cy - r * 0.6f
        val bot = cy + r * 0.8f
        p.moveTo(left, top)
        val steps = 40
        for (i in 0..steps) {
            val x = left + (right - left) * i / steps
            val y = top + amp * r * 0.4f * sin(4.0 * Math.PI * i / steps).toFloat()
            p.lineTo(x, y)
        }
        p.lineTo(right, bot); p.lineTo(left, bot); p.close()
    }

    private fun cloud(p: Path, cx: Float, cy: Float, r: Float, bumps: Int, variant: Boolean) {
        val baseline = cy + r * 0.4f
        val step = (2 * r) / bumps.coerceAtLeast(3)
        if (variant) {
            p.moveTo(cx - r, baseline)
            for (i in 0 until bumps) {
                val x = cx - r + step * (i + 0.5f)
                val rad = step * (0.6f + (i % 3) * 0.15f)
                p.addCircle(x, baseline - rad * 0.5f, rad, Path.Direction.CW)
            }
            p.addRect(cx - r, baseline - step * 0.3f, cx + r, baseline, Path.Direction.CW)
        } else {
            p.moveTo(cx - r, baseline)
            for (i in 0 until bumps) {
                val x = cx - r + step * i
                val rad = step * (0.7f + (i % 2) * 0.2f)
                p.addCircle(x, baseline - rad * 0.4f, rad, Path.Direction.CW)
            }
        }
    }

    private fun spiral(p: Path, cx: Float, cy: Float, r: Float, loops: Float) {
        val steps = 200
        val totalLoops = 2.0 + loops * 3.0
        for (i in 0 until steps) {
            val t = i.toDouble() / steps
            val angle = 2.0 * Math.PI * totalLoops * t
            val radius = r * t
            val x = cx + radius * cos(angle).toFloat()
            val y = cy + radius * sin(angle).toFloat()
            if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
        }
    }

    private fun crescent(p: Path, cx: Float, cy: Float, r: Float, depth: Float) {
        val outer = Path()
        outer.addCircle(cx, cy, r, Path.Direction.CW)
        val inner = Path()
        val offset = r * (0.3f + depth * 0.4f)
        inner.addCircle(cx + offset, cy, r, Path.Direction.CW)
        outer.op(inner, Path.Op.DIFFERENCE)
        p.addPath(outer)
    }

    private fun burst(p: Path, cx: Float, cy: Float, r: Float, spikiness: Float) {
        val spikes = 12 + (spikiness * 12).toInt()
        val inner = r * (0.6f - spikiness * 0.3f)
        star(p, cx, cy, r, inner, spikes)
    }

    private fun ring(p: Path, cx: Float, cy: Float, r: Float, thickness: Float) {
        val outer = Path()
        outer.addCircle(cx, cy, r, Path.Direction.CW)
        val innerR = r * (0.4f + thickness * 0.5f)
        val inner = Path()
        inner.addCircle(cx, cy, innerR, Path.Direction.CW)
        outer.op(inner, Path.Op.DIFFERENCE)
        p.addPath(outer)
    }

    private fun keyShape(p: Path, cx: Float, cy: Float, r: Float, roundness: Float) {
        val h = r * 0.9f
        val w = r * 0.35f
        p.addCircle(cx, cy - h * 0.6f, w, Path.Direction.CW)
        p.moveTo(cx - w * 0.3f, cy - h * 0.1f)
        p.lineTo(cx - w * 0.3f, cy + h)
        p.lineTo(cx + w * 0.3f, cy + h)
        p.lineTo(cx + w * 0.3f, cy - h * 0.1f)
        p.close()
        if (roundness > 0.5f) {
            p.addRoundRect(RectF(cx - w, cy + h * 0.2f, cx + w, cy + h * 0.4f), 4f, 4f, Path.Direction.CW)
        }
    }

    private fun leaf(p: Path, cx: Float, cy: Float, r: Float, sharpness: Float) {
        val topY = cy - r
        val botY = cy + r
        p.moveTo(cx, topY)
        p.cubicTo(cx + r * (0.8f + sharpness * 0.4f), cy - r * 0.3f, cx + r * (0.8f + sharpness * 0.4f), cy + r * 0.3f, cx, botY)
        p.cubicTo(cx - r * (0.8f + sharpness * 0.4f), cy + r * 0.3f, cx - r * (0.8f + sharpness * 0.4f), cy - r * 0.3f, cx, topY)
        p.close()
    }

    // ═══════════ توليد PNG ═══════════

    /**
     * توليد صورة PNG للشكل.
     *
     * @param shapeId معرّف الشكل
     * @param fillColor اللون الأساسي (ARGB)
     * @param rotation زاوية الدوران بالدرجات
     * @param sharpness حدّة الزوايا 0..1
     * @param tilt الميلان بالدرجات
     * @param size حجم الصورة (افتراضيًا 256)
     */
    fun generatePNG(
        shapeId: String,
        fillColor: Int,
        rotation: Float = 0f,
        sharpness: Float = 0.5f,
        tilt: Float = 0f,
        size: Int = CANVAS_SIZE,
        is3D: Boolean = false,
    ): ByteArray {
        val def = shapes.find { it.id == shapeId } ?: shapes.first()

        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.TRANSPARENT)

        // معايرة الحجم
        val scale = size.toFloat() / CANVAS_SIZE.toFloat()
        canvas.scale(scale, scale)

        val path = buildPath(def, sharpness)
        val bounds = RectF()
        path.computeBounds(bounds, true)

        // ═══ تطبيق الدوران والميل ═══
        canvas.save()
        if (rotation != 0f || tilt != 0f) {
            val m = android.graphics.Matrix()
            m.postTranslate(-CANVAS_SIZE / 2f, -CANVAS_SIZE / 2f)
            if (tilt != 0f) {
                val skewRad = Math.toRadians(tilt.toDouble().coerceIn(-45.0, 45.0))
                m.postSkew(Math.tan(skewRad).toFloat(), 0f)
            }
            if (rotation != 0f) m.postRotate(rotation)
            m.postTranslate(CANVAS_SIZE / 2f, CANVAS_SIZE / 2f)
            canvas.concat(m)
        }

        // ═══ 1. الظل الخارجي (3D فقط) ═══
        if (is3D) {
            val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.argb(90, 0, 0, 0)
                setShadowLayer(14f, 0f, 8f, Color.argb(90, 0, 0, 0))
            }
            canvas.drawPath(path, shadowPaint)
        }

        // ═══ 2. تعبئة متدرجة (Linear 3D) ═══
        val gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, 0f, CANVAS_SIZE.toFloat(),
                intArrayOf(lighten(fillColor, 0.35f), fillColor, darken(fillColor, 0.25f)),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawPath(path, gradientPaint)

        // ═══ 3. اللمعة العلوية (3D فقط) ═══
        if (is3D) {
            val hl = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                shader = LinearGradient(
                    0f, 0f, 0f, bounds.height() * 0.5f,
                    intArrayOf(Color.argb(120, 255, 255, 255), Color.TRANSPARENT),
                    null, Shader.TileMode.CLAMP
                )
            }
            canvas.save()
            canvas.clipPath(path)
            canvas.drawRect(0f, 0f, CANVAS_SIZE.toFloat(), bounds.height() * 0.5f, hl)
            canvas.restore()
        }

        // ═══ 4. الحدود ═══
        val outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = if (is3D) 5f else 3f
            color = darken(fillColor, 0.45f)
        }
        canvas.drawPath(path, outlinePaint)

        // ═══ 5. الحد الداخلي الفاتح (3D فقط) ═══
        if (is3D) {
            val innerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = 2f
                color = Color.argb(120, 255, 255, 255)
            }
            canvas.save()
            val inset = Path(path)
            val m = android.graphics.Matrix()
            m.setScale(0.92f, 0.92f, CANVAS_SIZE / 2f, CANVAS_SIZE / 2f)
            inset.transform(m)
            canvas.drawPath(inset, innerPaint)
            canvas.restore()
        }

        canvas.restore()

        val out = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
        bmp.recycle()
        return out.toByteArray()
    }

    private fun lighten(color: Int, factor: Float): Int {
        val r = (Color.red(color) + (255 - Color.red(color)) * factor).toInt().coerceIn(0, 255)
        val g = (Color.green(color) + (255 - Color.green(color)) * factor).toInt().coerceIn(0, 255)
        val b = (Color.blue(color) + (255 - Color.blue(color)) * factor).toInt().coerceIn(0, 255)
        return Color.argb(Color.alpha(color), r, g, b)
    }

    private fun darken(color: Int, factor: Float): Int {
        val r = (Color.red(color) * (1 - factor)).toInt().coerceIn(0, 255)
        val g = (Color.green(color) * (1 - factor)).toInt().coerceIn(0, 255)
        val b = (Color.blue(color) * (1 - factor)).toInt().coerceIn(0, 255)
        return Color.argb(Color.alpha(color), r, g, b)
    }
}

/**
 * ذاكرة تخزين مؤقت للصور المصغّرة — LRU بسيطة.
 * تحفظ آخر 120 صورة فقط لتقليل الذاكرة.
 */
object ShapeThumbnailCache {
    private const val MAX_SIZE = 120
    private const val THUMB_SIZE = 96

    private val cache = object : LinkedHashMap<String, Bitmap>(16, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, Bitmap>?): Boolean {
            return size > MAX_SIZE
        }
    }

    @Synchronized
    fun get(
        shapeId: String,
        fillColor: Int,
        rotation: Float,
        sharpness: Float,
        tilt: Float,
    ): Bitmap {
        val key = "$shapeId|$fillColor|$rotation|$sharpness|$tilt"
        cache[key]?.let { return it }

        val bytes = ShapesGenerator.generatePNG(
            shapeId = shapeId,
            fillColor = fillColor,
            rotation = rotation,
            sharpness = sharpness,
            tilt = tilt,
            size = THUMB_SIZE * 2,
        )

        val bmp = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            ?: return Bitmap.createBitmap(THUMB_SIZE, THUMB_SIZE, Bitmap.Config.ARGB_8888)

        val scaled = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true)
        if (scaled != bmp) bmp.recycle()

        cache[key] = scaled
        return scaled
    }

    @Synchronized
    fun clear() = cache.clear()
}
