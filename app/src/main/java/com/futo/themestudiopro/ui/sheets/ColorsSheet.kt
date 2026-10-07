package com.futo.themestudiopro.ui.sheets

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themestudiopro.data.ColorCatalog
import com.futo.themestudiopro.data.ColorImporter
import com.futo.themestudiopro.data.GradientPreset
import com.futo.themestudiopro.data.SolidPreset
import com.futo.themestudiopro.data.ThemeState

/**
 * واجهة الألوان — قسمان مستقلان.
 */
@Composable
fun ColorsSheet() {
    var selectedTab by remember { mutableStateOf(ColorTarget.BACKGROUND) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 12.dp),
    ) {
        Text(
            text = "🎨 الألوان",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Text(
            text = "${ColorCatalog.totalCount} لون جاهز — اختر قسمًا",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )
        Spacer(Modifier.height(12.dp))
        TabRow(selectedTabIndex = selectedTab.ordinal) {
            ColorTarget.entries.forEach { target ->
                Tab(
                    selected = selectedTab == target,
                    onClick = { selectedTab = target },
                    text = {
                        Text(
                            text = "${target.emoji}  ${target.label}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = if (selectedTab == target) FontWeight.Bold else FontWeight.Normal,
                        )
                    },
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        ImportButton(target = selectedTab)
        Spacer(Modifier.height(16.dp))
        SectionTitle("الألوان الصلبة (${ColorCatalog.solidCount})")
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth().height(240.dp),
        ) {
            items(ColorCatalog.ALL_SOLID, key = { it.name }) { preset ->
                SolidColorItem(preset = preset, onClick = { applySolid(preset, selectedTab) })
            }
        }
        Spacer(Modifier.height(16.dp))
        SectionTitle("التدرجات (${ColorCatalog.gradientCount})")
        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth().height(240.dp),
        ) {
            items(ColorCatalog.ALL_GRADIENT, key = { it.name }) { preset ->
                GradientColorItem(preset = preset, onClick = { applyGradient(preset, selectedTab) })
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun ImportButton(target: ColorTarget) {
    val context = LocalContext.current
    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri != null) {
            val colors = ColorImporter.importFromUri(context, uri)
            if (colors.isNotEmpty()) {
                val firstColor = colors.first()
                val theme = ThemeState.theme
                val newTheme = when (target) {
                    ColorTarget.BACKGROUND -> ColorCatalog.applyBackgroundColor(theme, firstColor)
                    ColorTarget.BUTTON -> ColorCatalog.applyButtonColor(theme, firstColor)
                }
                ThemeState.replace(newTheme)
            }
        }
    }
    FilledTonalButton(
        onClick = { picker.launch(arrayOf("text/*", "application/json", "*/*")) },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
    ) {
        Text("📥 استيراد من الجهاز — ${target.label}", fontWeight = FontWeight.Medium)
    }
}

private fun applySolid(preset: SolidPreset, target: ColorTarget) {
    val theme = ThemeState.theme
    val newTheme = when (target) {
        ColorTarget.BACKGROUND -> ColorCatalog.applyBackground(theme, preset)
        ColorTarget.BUTTON -> ColorCatalog.applyButton(theme, preset)
    }
    ThemeState.replace(newTheme)
}

private fun applyGradient(preset: GradientPreset, target: ColorTarget) {
    val theme = ThemeState.theme
    val newTheme = when (target) {
        ColorTarget.BACKGROUND -> theme.copy(
            background = preset.bg,
            keyboardSurface = preset.bg,
            surface = preset.bg,
        )
        ColorTarget.BUTTON -> theme.copy(
            primary = preset.color1,
            keyboardPress = preset.color2,
            primaryContainer = preset.bg,
        )
    }
    ThemeState.replace(newTheme)
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
private fun SolidColorItem(preset: SolidPreset, onClick: () -> Unit) {
    val color = remember(preset.accent) { ColorCatalog.parseColor(preset.accent) }
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .clickable { onClick() },
    )
}

@Composable
private fun GradientColorItem(preset: GradientPreset, onClick: () -> Unit) {
    val c1 = remember(preset.color1) { ColorCatalog.parseColor(preset.color1) }
    val c2 = remember(preset.color2) { ColorCatalog.parseColor(preset.color2) }
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(Brush.linearGradient(listOf(c1, c2)))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .clickable { onClick() },
    )
}

enum class ColorTarget(val emoji: String, val label: String) {
    BACKGROUND("🎨", "خلف الأزرار"),
    BUTTON("🖌️", "للأزرار"),
}
