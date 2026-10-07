package com.futo.themestudiopro.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Slider رفيع بكرات صغيرة — كما في التصميم المرجعي.
 * - مسار رمادي غير نشط + أزرق نشط
 * - كرة صغيرة بيضاء بحدود زرقاء
 * - تفاعل سحب أفقي مباشر
 */
@Composable
fun CompactSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float> = 0f..1f,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val progress = ((value - range.start) / (range.endInclusive - range.start)).coerceIn(0f, 1f)

    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val activeColor = MaterialTheme.colorScheme.primary
    val thumbColor = MaterialTheme.colorScheme.primary
    val thumbBorder = MaterialTheme.colorScheme.surface

    BoxWithConstraints(modifier.fillMaxWidth()) {
        val trackHeight = 4.dp
        val thumbSize = 14.dp

        Canvas(
            Modifier
                .fillMaxWidth()
                .height(thumbSize + 8.dp)
                .pointerInput(enabled, range) {
                    if (!enabled) return@pointerInput
                    detectHorizontalDragGestures { change, _ ->
                        val newProgress = (change.position.x / size.width).coerceIn(0f, 1f)
                        onValueChange(range.start + newProgress * (range.endInclusive - range.start))
                    }
                }
        ) {
            val w = size.width
            val h = size.height
            val cy = h / 2f
            val thumbR = thumbSize.toPx() / 2f

            drawLine(
                color = trackColor,
                start = Offset(0f, cy),
                end = Offset(w, cy),
                strokeWidth = trackHeight.toPx()
            )

            val activeEnd = w * progress
            drawLine(
                color = activeColor,
                start = Offset(0f, cy),
                end = Offset(activeEnd, cy),
                strokeWidth = trackHeight.toPx()
            )

            val thumbX = (w * progress).coerceIn(thumbR, w - thumbR)
            drawCircle(color = thumbBorder, radius = thumbR + 1.5f, center = Offset(thumbX, cy))
            drawCircle(color = thumbColor, radius = thumbR, center = Offset(thumbX, cy))
        }
    }
}

/**
 * صف slider مع تسمية وقيمة.
 */
@Composable
fun CompactSliderRow(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>,
    valueFormatter: (Float) -> String = { "%.2f".format(it) },
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.weight(1f),
            )
            Text(
                valueFormatter(value),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 11.sp,
            )
        }
        Spacer(Modifier.height(2.dp))
        CompactSlider(value = value, onValueChange = onValueChange, range = range)
    }
}
