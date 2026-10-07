package com.futo.themestudiopro.ui.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themestudiopro.data.*
import com.futo.themestudiopro.utils.ColorUtils

/**
 * تبويب الألوان — 700 لون جاهز.
 */
@Composable
fun ColorsSheet() {
    var tab by remember { mutableIntStateOf(0) }
    val tabs = listOf("داكن", "فاتح", "تدرّج")

    val current = when (tab) {
        0 -> PresetColors.solidDark
        1 -> PresetColors.solidLight
        else -> PresetColors.gradientDark + PresetColors.gradientLight
    }

    Column(Modifier.fillMaxWidth()) {
        Text(
            "🎨 الألوان (${current.size})",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
        )

        TabRow(selectedTabIndex = tab) {
            tabs.forEachIndexed { i, label ->
                Tab(
                    selected = tab == i,
                    onClick = { tab = i },
                    text = { Text(label) },
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth().heightIn(max = 480.dp),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(current, key = { it.toString() }) { item ->
                when (item) {
                    is SolidPreset -> SolidPresetCard(item) {
                        applySolidPreset(item)
                    }
                    is GradientPreset -> GradientPresetCard(item) {
                        applyGradientPreset(item)
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun SolidPresetCard(preset: SolidPreset, onClick: () -> Unit) {
    val accentColor = remember(preset.accent) { Color(ColorUtils.parseColor(preset.accent)) }
    val bgColor = remember(preset.bg) { Color(ColorUtils.parseColor(preset.bg)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = bgColor),
    ) {
        Column(
            Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(accentColor)
                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(6.dp)),
            )
            Text(
                preset.name,
                style = MaterialTheme.typography.labelSmall,
                color = Color(ColorUtils.contrastText(bgColor)),
                maxLines = 2,
            )
        }
    }
}

@Composable
private fun GradientPresetCard(preset: GradientPreset, onClick: () -> Unit) {
    val c1 = remember(preset.color1) { Color(ColorUtils.parseColor(preset.color1)) }
    val c2 = remember(preset.color2) { Color(ColorUtils.parseColor(preset.color2)) }
    val bg = remember(preset.bg) { Color(ColorUtils.parseColor(preset.bg)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = bg),
    ) {
        Column(
            Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(c1, c2))
                    ),
            )
            Text(
                preset.name,
                style = MaterialTheme.typography.labelSmall,
                color = Color(ColorUtils.contrastText(bg)),
                maxLines = 2,
            )
        }
    }
}

private fun applySolidPreset(preset: SolidPreset) {
    val t = ThemeState.theme
    ThemeState.replace(
        t.copy(
            primary = preset.accent,
            onPrimary = "#FFFFFFFF",
            primaryContainer = preset.container,
            background = preset.bg,
            onBackground = "#FFFFFFFF",
            surface = preset.bg,
            surfaceVariant = preset.surface,
            keyboardSurface = preset.bg,
            keyboardContainer = preset.surface,
            keyboardContainerVariant = preset.container,
            keyboardPress = preset.accent,
        )
    )
}

private fun applyGradientPreset(preset: GradientPreset) {
    val t = ThemeState.theme
    ThemeState.replace(
        t.copy(
            primary = preset.color1,
            onPrimary = "#FFFFFFFF",
            primaryContainer = preset.color2,
            background = preset.bg,
            onBackground = "#FFFFFFFF",
            surface = preset.bg,
            surfaceVariant = preset.color1 + "22",
            keyboardSurface = preset.bg,
            keyboardContainer = preset.color1 + "22",
            keyboardContainerVariant = preset.color2,
            keyboardPress = preset.color1,
        )
    )
}
