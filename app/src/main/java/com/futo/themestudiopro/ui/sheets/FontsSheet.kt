package com.futo.themestudiopro.ui.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.futo.themestudiopro.data.FontCatalog
import com.futo.themestudiopro.data.FontDownloader
import com.futo.themestudiopro.data.FontInfo
import com.futo.themestudiopro.data.FontLanguage
import com.futo.themestudiopro.data.FontsState
import com.futo.themestudiopro.data.ThemeState
import kotlinx.coroutines.launch

/**
 * واجهة الخطوط — قسمان مستقلان:
 *  🅰️ خط عربي  |  🅱️ خط إنجليزي
 */
@Composable
fun FontsSheet() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableIntStateOf(0) }

    val fonts = if (selectedTab == 0) FontCatalog.ARABIC else FontCatalog.ENGLISH

    LaunchedEffect(Unit) {
        FontsState.loadArabicFamily(context, ThemeState.theme.fontArabic)
        FontsState.loadEnglishFamily(context, ThemeState.theme.fontEnglish)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        Text(
            text = "🔤 الخطوط",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Text(
            text = "20 خط عربي + 20 خط إنجليزي — التطبيق مستقل لكل لغة",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )

        Spacer(Modifier.height(12.dp))

        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("🅰️ عربي") },
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("🅱️ إنجليزي") },
            )
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(fonts, key = { it.id }) { font ->
                val isArabic = font.language == FontLanguage.ARABIC
                val currentSelection = if (isArabic) {
                    ThemeState.theme.fontArabic
                } else {
                    ThemeState.theme.fontEnglish
                }
                val isSelected = currentSelection == font.fileName
                val downloaded = FontDownloader.isDownloaded(context, font.fileName)
                val state = FontsState.downloadStates[font.fileName] ?: if (downloaded) 2 else 0

                FontCard(
                    font = font,
                    isSelected = isSelected,
                    downloadState = state,
                    fontFamily = if (isArabic) {
                        FontsState.arabicFontFamily.value
                    } else {
                        FontsState.englishFontFamily.value
                    },
                    onClick = {
                        scope.launch {
                            if (state != 2) {
                                FontsState.downloadAndLoad(context, font)
                            }
                            val theme = ThemeState.theme
                            if (isArabic) {
                                ThemeState.replace(theme.copy(fontArabic = font.fileName))
                                FontsState.loadArabicFamily(context, font.fileName)
                            } else {
                                ThemeState.replace(theme.copy(fontEnglish = font.fileName))
                                FontsState.loadEnglishFamily(context, font.fileName)
                            }
                        }
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
    downloadState: Int,
    fontFamily: FontFamily?,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            },
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = font.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "${font.category.display} · ${font.fileName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(Modifier.height(4.dp))

                val previewFamily = if (isSelected) fontFamily else null
                if (previewFamily != null) {
                    Text(text = "أبجد هوّز ١٢٣", fontSize = 18.sp, fontFamily = previewFamily)
                    Text(text = "Aa Bb Cc 123", fontSize = 18.sp, fontFamily = previewFamily)
                } else {
                    Text(
                        text = "أبجد هوّز ١٢٣",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "Aa Bb Cc 123",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.size(8.dp))

            when {
                downloadState == 1 -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                    )
                }
                isSelected -> {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "مختار",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp),
                    )
                }
                downloadState == 0 -> {
                    IconButton(onClick = onClick) {
                        Icon(
                            Icons.Default.CloudDownload,
                            contentDescription = "تحميل",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
                else -> Box(Modifier.size(28.dp))
            }
        }
    }
}
