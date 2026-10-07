package com.futo.themestudiopro.ui.sheets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themestudiopro.data.*
import com.futo.themestudiopro.utils.ColorUtils

@Composable
fun ShapesSheet() {
    var only3D by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }

    val allShapes = remember { ShapesGenerator.shapes }
    val filtered = remember(only3D, search) {
        allShapes.asSequence()
            .filter { !only3D || it.id.startsWith("3d_") }
            .filter { search.isBlank() || it.name.contains(search, true) }
            .toList()
    }

    val theme = ThemeState.theme
    val previewColor = remember(theme.keyboardContainer) {
        ColorUtils.parseColor(theme.keyboardContainer)
    }

    Column(Modifier.fillMaxWidth()) {
        Text(
            "🔷 الأشكال (${filtered.size} من ${allShapes.size})",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
        )

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FilterChip(
                selected = only3D,
                onClick = { only3D = !only3D },
                label = { Text("3D فقط") },
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "${allShapes.size} شكل متاح",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("بحث") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        )

        Spacer(Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(80.dp),
            modifier = Modifier.fillMaxWidth().heightIn(max = 480.dp),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(filtered, key = { it.id }) { shape ->
                ShapeTile(
                    shape = shape,
                    fillColor = previewColor,
                    isSelected = theme.shapeId == shape.id,
                    onClick = {
                        ThemeState.replace(theme.copy(shapeId = shape.id))
                    },
                )
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ShapeTile(
    shape: ShapeDef,
    fillColor: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bitmap = remember(shape.id, fillColor) {
        try {
            ShapeThumbnailCache.get(
                shapeId = shape.id,
                fillColor = fillColor,
                rotation = 0f,
                sharpness = 0.5f,
                tilt = 0f,
            )
        } catch (e: Exception) { null }
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceContainerHigh
            )
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = shape.name,
                modifier = Modifier.fillMaxSize().padding(6.dp),
            )
        } else {
            Text(shape.emoji, style = MaterialTheme.typography.titleLarge)
        }

        if (isSelected) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).size(16.dp),
            )
        }
    }
}
