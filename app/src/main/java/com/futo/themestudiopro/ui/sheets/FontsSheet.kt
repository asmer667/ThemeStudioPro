package com.futo.themestudiopro.ui.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.futo.themestudiopro.data.FontCatalog
import com.futo.themestudiopro.data.FontInfo
import com.futo.themestudiopro.data.FontLanguage
import com.futo.themestudiopro.data.ThemeState

/**
 * تبويب الخطوط — 40 خط (20 عربي + 20 إنجليزي).
 */
@Composable
fun FontsSheet() {
    var tab by remember { mutableIntStateOf(0) }
    val tabs = listOf("عربي", "إنجليزي")
    val fonts = if (tab == 0) FontCatalog.ARABIC else FontCatalog.ENGLISH

    val selected = ThemeState.theme.fontName

    Column(Modifier.fillMaxWidth()) {
        Text(
            "🔤 الخطوط",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
        )

        Text(
            "اختر خطًا للكيبورد. الخط سيُضمَّن في ZIP عند التصدير.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(Modifier.height(12.dp))

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

        LazyColumn(
            modifier = Modifier.fillMaxWidth().heightIn(max = 480.dp),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(fonts, key = { it.id }) { font ->
                FontCard(
                    font = font,
                    isSelected = selected == font.fileName,
                    onSelect = {
                        ThemeState.replace(ThemeState.theme.copy(fontName = font.fileName))
                    },
                )
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun FontCard(
    font: FontInfo,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        Row(
            Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    font.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "${font.category.display} · ${font.fileName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                // معاينة بخط النظام
                Text(
                    if (font.language == FontLanguage.ARABIC)
                        "أبجد هوّز 123"
                    else
                        "Aa Bb Cc 123",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "مختار",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}
