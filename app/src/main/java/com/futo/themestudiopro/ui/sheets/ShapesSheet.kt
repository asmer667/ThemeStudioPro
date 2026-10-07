package com.futo.themestudiopro.ui.sheets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themestudiopro.data.ShapesGenerator
import com.futo.themestudiopro.data.ThemeState
import com.futo.themestudiopro.data.renderers.ShapeRenderer
import com.futo.themestudiopro.data.renderers.ShapeRenderer.drawShape
import com.futo.themestudiopro.data.shapes.ShapeFamilies
import com.futo.themestudiopro.data.shapes.ShapePalette
import com.futo.themestudiopro.data.shapes.ShapeSpec
import com.futo.themestudiopro.utils.ColorUtils

/**
 * واجهة الأشكال الكاملة:
 *  - اختيار عائلة من 16 عائلة
 *  - شبكة تعرض كل أشكال العائلة
 *  - اختيار شكل يطبّقه على المعاينة الحية
 */
@Composable
fun ShapesSheet() {
    val theme = ThemeState.theme
    val allShapes = remember { ShapesGenerator.shapes }

    // العائلة المختارة حاليًا
    var selectedFamilyId by remember { mutableStateOf(ShapeFamilies.ALL.first().id) }
    val selectedFamily = remember(selectedFamilyId) {
        ShapeFamilies.findById(selectedFamilyId) ?: ShapeFamilies.ALL.first()
    }

    // الشكل المختار
    var selectedShapeId by remember { mutableStateOf<String?>(null) }

    // الفلترة حسب العائلة
    val familyShapes = remember(selectedFamilyId, allShapes) {
        allShapes.filter { it.familyId == selectedFamilyId }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 12.dp),
    ) {
        // ═══ العنوان والإحصاءات ═══
        Text(
            text = "🔷 الأشكال الهندسية",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Text(
            text = "${allShapes.size} شكل هندسي متاح في 16 عائلة",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )

        Spacer(Modifier.height(12.dp))

        // ═══ شريط العائلات الـ16 ═══
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(ShapeFamilies.ALL) { family ->
                val selected = family.id == selectedFamilyId
                FilterChip(
                    selected = selected,
                    onClick = { selectedFamilyId = family.id },
                    label = {
                        Text(
                            text = "${family.emoji} ${family.displayName}",
                            style = MaterialTheme.typography.labelMedium,
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // ═══ وصف العائلة ═══
        Text(
            text = "${selectedFamily.emoji}  ${selectedFamily.description}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(Modifier.height(12.dp))

        // ═══ شبكة الأشكال ═══
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
        ) {
            items(familyShapes, key = { it.id }) { shape ->
                ShapeGridItem(
                    shape = shape,
                    isSelected = shape.id == selectedShapeId,
                    onClick = { selectedShapeId = shape.id },
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // ═══ أدوات التحكم ═══
        ShapeControlPanel()

        Spacer(Modifier.height(20.dp))

        // ═══ ملخص الاختيار ═══
        if (selectedShapeId != null) {
            Text(
                text = "✅ الشكل المختار: $selectedShapeId",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        } else {
            Text(
                text = "اختر شكلًا من الشبكة أعلاه.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

/**
 * عنصر شكل واحد داخل الشبكة.
 */
@Composable
private fun ShapeGridItem(
    shape: ShapeSpec,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val family = remember(shape.familyId) {
        ShapeFamilies.findById(shape.familyId) ?: ShapeFamilies.ALL.first()
    }
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
    }
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .border(borderWidth, borderColor, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension * 0.42f

            val triple = ShapePalette.TRIPLES.firstOrNull { it.base == shape.primaryColor }
                ?: ShapePalette.TRIPLES.first()

            drawShape(
                style = family.renderStyle,
                center = center,
                radius = radius,
                triple = triple,
                variant = shape.variant,
                params = shape.params,
            )
        }
    }
}

/**
 * أدوات التحكم في الشكل — Sliders للطول والعرض والحدة والانحناء.
 */
@Composable
private fun ShapeControlPanel() {
    val theme = ThemeState.theme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = "🎛️ أدوات التحكم",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(8.dp))

        ControlSlider(
            label = "العرض",
            value = theme.keyWidthScale,
            range = 0.5f..1.5f,
            onValueChange = { ThemeState.updateNumber("key_width_scale", it) },
        )
        ControlSlider(
            label = "الارتفاع",
            value = theme.keyHeightScale,
            range = 0.5f..1.5f,
            onValueChange = { ThemeState.updateNumber("key_height_scale", it) },
        )
        ControlSlider(
            label = "الانحناء",
            value = theme.roundedness,
            range = 0f..1f,
            onValueChange = { ThemeState.updateNumber("roundedness", it) },
        )
        ControlSlider(
            label = "الحِدّة",
            value = theme.shapeSharpness,
            range = 0f..2f,
            onValueChange = { ThemeState.updateNumber("shape_sharpness", it) },
        )
        ControlSlider(
            label = "الميلان",
            value = theme.shapeTilt,
            range = -45f..45f,
            onValueChange = { ThemeState.updateNumber("shape_tilt", it) },
        )
    }
}

/**
 * Slider موحّد مع عنوان وقيمة.
 */
@Composable
private fun ControlSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
) {
    Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium)
            Text(
                text = "%.2f".format(value),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }
}
