package com.futo.themestudiopro.data.renderers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * أدوات مساعدة للرسم داخل Canvas.
 * كل الدوال هنا stateless ولا تعتمد على أي حالة خارجية.
 */
/**
 * يرسم ظلًا دائريًا ناعمًا تحت الشكل.
 */
fun DrawScope.drawSoftShadow(
    center: Offset,
    radius: Float,
    color: Color = Color.Black.copy(alpha = 0.25f),
    blur: Float = 0.15f,
) {
    val shadowRadius = radius * (1f + blur)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(color, Color.Transparent),
            center = center + Offset(0f, radius * 0.08f),
            radius = shadowRadius,
        ),
        radius = shadowRadius,
        center = center + Offset(0f, radius * 0.08f),
    )
}

/**
 * يرسم لمعة (highlight) في الأعلى-اليسار.
 */
fun DrawScope.drawGloss(
    center: Offset,
    radius: Float,
    color: Color = Color.White.copy(alpha = 0.55f),
) {
    val glossCenter = center + Offset(-radius * 0.3f, -radius * 0.4f)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(color, Color.Transparent),
            center = glossCenter,
            radius = radius * 0.7f,
        ),
        radius = radius * 0.7f,
        center = glossCenter,
    )
}

/**
 * يرسم تدرّجًا خطيًا داخليًا يعطي إحساس العمق.
 */
fun DrawScope.drawDepthGradient(
    center: Offset,
    radius: Float,
    dark: Color,
    light: Color,
) {
    drawCircle(
        brush = Brush.linearGradient(
            colors = listOf(light, dark),
            start = center + Offset(-radius, -radius),
            end = center + Offset(radius, radius),
        ),
        radius = radius,
        center = center,
    )
}

/**
 * يرسم دائرة كاملة بأسلوب زجاجي — الطبقات الأربع.
 */
fun DrawScope.drawGlassCircle(
    center: Offset,
    radius: Float,
    dark: Color,
    base: Color,
    light: Color,
    gloss: Float = 0.55f,
    shadowAlpha: Float = 0.25f,
) {
    // 1. ظل خارجي
    drawSoftShadow(
        center = center,
        radius = radius,
        color = Color.Black.copy(alpha = shadowAlpha),
    )

    // 2. التدرّج الأساسي (depth)
    drawDepthGradient(
        center = center,
        radius = radius,
        dark = dark,
        light = light,
    )

    // 3. حد رقيق داخلي
    drawCircle(
        color = light.copy(alpha = 0.6f),
        radius = radius,
        center = center,
        style = Stroke(width = radius * 0.06f),
    )

    // 4. اللمعة العلوية
    drawGloss(
        center = center,
        radius = radius,
        color = Color.White.copy(alpha = gloss),
    )
}

/**
 * يرسم مربعًا بزوايا دائرية بأسلوب زجاجي.
 */
fun DrawScope.drawGlassRoundedSquare(
    topLeft: Offset,
    size: Size,
    cornerRadius: Float,
    dark: Color,
    base: Color,
    light: Color,
    gloss: Float = 0.5f,
    shadowAlpha: Float = 0.25f,
) {
    val radius = min(size.width, size.height) / 2f
    val center = Offset(topLeft.x + size.width / 2f, topLeft.y + size.height / 2f)

    // ظل خارجي
    drawSoftShadow(
        center = center,
        radius = radius,
        color = Color.Black.copy(alpha = shadowAlpha),
    )

    // القاعدة
    val path = roundedRectPath(topLeft, size, cornerRadius)
    drawPath(
        path = path,
        brush = Brush.linearGradient(
            colors = listOf(light, dark),
            start = topLeft,
            end = Offset(topLeft.x + size.width, topLeft.y + size.height),
        ),
    )

    // حد داخلي
    drawPath(
        path = path,
        color = light.copy(alpha = 0.6f),
        style = Stroke(width = cornerRadius * 0.3f),
    )

    // لمعة علوية (نصف الدائرة العلوية فقط)
    val glossCenter = Offset(center.x, topLeft.y + size.height * 0.3f)
    drawOval(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.White.copy(alpha = gloss),
                Color.Transparent,
            ),
            center = glossCenter,
            radius = size.width * 0.5f,
        ),
        topLeft = Offset(topLeft.x + size.width * 0.1f, topLeft.y + size.height * 0.05f),
        size = Size(size.width * 0.8f, size.height * 0.45f),
    )
}

/**
 * ينشئ Path لمستطيل بزوايا دائرية.
 */
fun roundedRectPath(topLeft: Offset, size: Size, cornerRadius: Float): Path {
    val r = cornerRadius.coerceAtMost(min(size.width, size.height) / 2f)
    val path = Path()
    path.moveTo(topLeft.x + r, topLeft.y)
    path.lineTo(topLeft.x + size.width - r, topLeft.y)
    path.quadraticBezierTo(topLeft.x + size.width, topLeft.y, topLeft.x + size.width, topLeft.y + r)
    path.lineTo(topLeft.x + size.width, topLeft.y + size.height - r)
    path.quadraticBezierTo(
        topLeft.x + size.width, topLeft.y + size.height,
        topLeft.x + size.width - r, topLeft.y + size.height,
    )
    path.lineTo(topLeft.x + r, topLeft.y + size.height)
    path.quadraticBezierTo(
        topLeft.x, topLeft.y + size.height,
        topLeft.x, topLeft.y + size.height - r,
    )
    path.lineTo(topLeft.x, topLeft.y + r)
    path.quadraticBezierTo(topLeft.x, topLeft.y, topLeft.x + r, topLeft.y)
    path.close()
    return path
}

/**
 * ينشئ Path لمضلّع منتظم بعدد أضلاع.
 */
fun polygonPath(center: Offset, radius: Float, sides: Int, rotationDeg: Float = 0f): Path {
    val path = Path()
    val rotationRad = Math.toRadians(rotationDeg.toDouble())
    for (i in 0 until sides) {
        val angle = 2.0 * Math.PI * i / sides - Math.PI / 2 + rotationRad
        val x = center.x + (radius * cos(angle)).toFloat()
        val y = center.y + (radius * sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

/**
 * ينشئ Path لنجمة بعدد رؤوس.
 */
fun starPath(
    center: Offset,
    outerRadius: Float,
    innerRadius: Float,
    points: Int,
    rotationDeg: Float = 0f,
): Path {
    val path = Path()
    val rotationRad = Math.toRadians(rotationDeg.toDouble())
    val step = Math.PI / points
    for (i in 0 until points * 2) {
        val r = if (i % 2 == 0) outerRadius else innerRadius
        val angle = step * i - Math.PI / 2 + rotationRad
        val x = center.x + (r * cos(angle)).toFloat()
        val y = center.y + (r * sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}
