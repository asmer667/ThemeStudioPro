package com.futo.themestudiopro.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.futo.themestudiopro.data.ShapeThumbnailCache
import com.futo.themestudiopro.data.ThemeData
import com.futo.themestudiopro.data.ThemeState
import com.futo.themestudiopro.utils.ColorUtils

/**
 * معاينة حية للكيبورد العربي + الإنجليزي.
 */
@Composable
fun KeyboardPreview(
    theme: ThemeData,
    scale: Float = 1.4f,
) {
    val kbSurface = remember(theme.keyboardSurface) { Color(ColorUtils.parseColor(theme.keyboardSurface)) }
    val kbContainer = remember(theme.keyboardContainer) { Color(ColorUtils.parseColor(theme.keyboardContainer)) }
    val onKb = remember(theme.onKeyboardContainer) { Color(ColorUtils.parseColor(theme.onKeyboardContainer)) }
    val primary = remember(theme.primary) { Color(ColorUtils.parseColor(theme.primary)) }
    val onPrimary = remember(theme.onPrimary) { Color(ColorUtils.parseColor(theme.onPrimary)) }
    val outline = remember(theme.outline) { Color(ColorUtils.parseColor(theme.outline)) }

    val keyW = 34.dp * scale
    val keyH = 44.dp * scale
    val wideKeyW = 52.dp * scale
    val rowGap = 7.dp * scale
    val keyRadius = (theme.roundedness * 16).dp

    val gradientBrush: Brush? = remember(
        theme.keyboardSurface, theme.keyboardContainer, theme.keyboardContainerVariant
    ) {
        // إن اختلف سطح عن حاوية → تدرّج
        if (theme.keyboardSurface != theme.keyboardContainer) {
            try {
                Brush.linearGradient(
                    colors = listOf(kbSurface, kbContainer),
                    start = Offset.Zero,
                    end = Offset(1500f, 1500f),
                )
            } catch (e: Exception) { null }
        } else null
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .then(
                if (gradientBrush != null) Modifier.background(gradientBrush)
                else Modifier.background(kbSurface)
            )
            .border(2.dp, outline.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp * scale, vertical = 8.dp * scale)
        ) {
            // ═══ صف 1: أرقام ═══
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                listOf("1","2","3","4","5","6","7","8","9","0").forEach { t ->
                    Key(text = t, bgColor = kbContainer, textColor = onKb, radius = keyRadius,
                        width = keyW, height = keyH, theme = theme)
                }
            }
            Spacer(Modifier.height(rowGap))

            // ═══ صف 2: QWERTY ═══
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                listOf("Q","W","E","R","T","Y","U","I","O","P").forEach { t ->
                    Key(text = t, bgColor = kbContainer, textColor = onKb, radius = keyRadius,
                        width = keyW, height = keyH, theme = theme)
                }
            }
            Spacer(Modifier.height(rowGap))

            // ═══ صف 3: ASDF ═══
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                listOf("A","S","D","F","G","H","J","K","L").forEach { t ->
                    Key(text = t, bgColor = kbContainer, textColor = onKb, radius = keyRadius,
                        width = keyW * 1.05f, height = keyH, theme = theme)
                }
            }
            Spacer(Modifier.height(rowGap))

            // ═══ صف 4: ⇧ ZXCVBNM ⌫ ═══
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Key(text = "⇧", bgColor = kbContainer, textColor = onKb, radius = keyRadius,
                    width = wideKeyW, height = keyH, theme = theme, fontSize = 16)
                Spacer(Modifier.width(3.dp))
                listOf("Z","X","C","V","B","N","M").forEach { t ->
                    Key(text = t, bgColor = kbContainer, textColor = onKb, radius = keyRadius,
                        width = keyW, height = keyH, theme = theme)
                }
                Spacer(Modifier.width(3.dp))
                Key(text = "⌫", bgColor = kbContainer, textColor = onKb, radius = keyRadius,
                    width = wideKeyW, height = keyH, theme = theme, fontSize = 16)
            }
            Spacer(Modifier.height(rowGap))

            // ═══ صف 5: ?123 ، 😊 مسافة . ↵ ═══
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Key(text = "?123", bgColor = kbContainer, textColor = onKb, radius = keyRadius,
                    width = wideKeyW * 0.9f, height = keyH, theme = theme, fontSize = 10)
                Spacer(Modifier.width(3.dp))
                Key(text = "😊", bgColor = kbContainer, textColor = onKb, radius = keyRadius,
                    width = keyW * 0.9f, height = keyH, theme = theme, fontSize = 15)
                Spacer(Modifier.width(3.dp))
                Key(text = "مسافة", bgColor = kbContainer, textColor = onKb, radius = keyRadius,
                    width = keyW * 4.5f, height = keyH, theme = theme, fontSize = 11)
                Spacer(Modifier.width(3.dp))
                Key(text = ".", bgColor = kbContainer, textColor = onKb, radius = keyRadius,
                    width = keyW * 0.9f, height = keyH, theme = theme)
                Spacer(Modifier.width(3.dp))
                Key(text = "↵", bgColor = primary, textColor = onPrimary, radius = keyRadius,
                    width = wideKeyW * 0.9f, height = keyH, theme = theme, fontSize = 16)
            }
        }
    }
}

@Composable
private fun Key(
    text: String,
    bgColor: Color,
    textColor: Color,
    radius: androidx.compose.ui.unit.Dp,
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp,
    theme: ThemeData,
    fontSize: Int = 14,
) {
    val outline = remember(theme.outline) { Color(ColorUtils.parseColor(theme.outline)) }
    val bgArgb = bgColor.toArgb()

    val shapeBitmap = theme.shapeId?.let { shapeId ->
        remember(shapeId, bgArgb) {
            try {
                ShapeThumbnailCache.get(
                    shapeId = shapeId,
                    fillColor = bgArgb,
                    rotation = theme.shapeRotation,
                    sharpness = theme.shapeSharpness,
                    tilt = theme.shapeTilt,
                )
            } catch (e: Exception) { null }
        }
    }

    val effW = width * theme.keyWidthScale
    val effH = height * theme.keyHeightScale

    Box(
        Modifier
            .padding(horizontal = 1.dp)
            .width(effW)
            .height(effH),
        contentAlignment = Alignment.Center,
    ) {
        if (shapeBitmap != null) {
            Image(
                bitmap = shapeBitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(radius))
                    .background(bgColor)
                    .then(
                        if (theme.autoBorders)
                            Modifier.border(1.dp, outline.copy(alpha = 0.3f), RoundedCornerShape(radius))
                        else Modifier
                    )
            )
        }

        Text(
            text,
            fontSize = (fontSize * theme.scaleText).sp,
            fontWeight = FontWeight(theme.weightText.toInt().coerceIn(100, 900)),
            color = textColor,
        )
    }
}
