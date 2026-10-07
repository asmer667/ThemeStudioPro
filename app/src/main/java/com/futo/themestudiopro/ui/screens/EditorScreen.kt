package com.futo.themestudiopro.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.futo.themestudiopro.data.ThemeState
import com.futo.themestudiopro.ui.components.KeyboardPreview
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.Surface

/**
 * التبويبات الرئيسية الثمانية.
 */
enum class EditorTab(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    THEME("المظهر", Icons.Default.Palette),
    COLORS("الألوان", Icons.Default.ColorLens),
    SHAPES("الأشكال", Icons.Default.Category),
    FONTS("الخطوط", Icons.Default.TextFields),
    BUTTONS("الصور", Icons.Default.Image),
    BACKGROUND("الخلفية", Icons.Default.Wallpaper),
    TEXT("النص", Icons.Default.FormatSize),
    EXPORT("التصدير", Icons.Default.Archive),
}

/**
 * الشاشة الرئيسية للمحرر.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun EditorScreen() {
    var currentTab by remember { mutableStateOf(EditorTab.THEME) }
    var previewScale by remember { mutableStateOf(1.4f) }

    val theme = ThemeState.theme

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            theme.name,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                        )
                        Text(
                            "Theme Studio Pro",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { ThemeState.toggleDarkMode() }) {
                        Icon(
                            if (ThemeState.darkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "الوضع الليلي",
                        )
                    }
                    IconButton(onClick = { ThemeState.reset() }) {
                        Icon(Icons.Default.Refresh, "إعادة تعيين")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                )
            )
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {

            // ═══ شريط التبويبات (Pill) ═══
            PillTabRow(
                items = EditorTab.values().toList(),
                selected = currentTab,
                onSelect = { currentTab = it },
            )

            // ═══ المعاينة الحية ═══
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(12.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        "معاينة حية",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    KeyboardPreview(theme = theme, scale = previewScale)
                }
            }

            // ═══ شريط السلايدر (للمعاينة) ═══
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("حجم المعاينة", style = MaterialTheme.typography.labelSmall)
                Spacer(Modifier.width(12.dp))
                Slider(
                    value = previewScale,
                    onValueChange = { previewScale = it },
                    valueRange = 0.8f..2.0f,
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "${(previewScale * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                )
            }

            // ═══ محتوى التبويب النشط ═══
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .padding(8.dp),
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = {
                        fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                    },
                    label = "tab-content",
                ) { tab ->
                    TabContent(tab)
                }
            }
        }
    }
}

/**
 * شريط التبويبات (Pill).
 */
@Composable
private fun PillTabRow(
    items: List<EditorTab>,
    selected: EditorTab,
    onSelect: (EditorTab) -> Unit,
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(selected) {
        val idx = items.indexOf(selected)
        if (idx >= 0) {
            try {
                scrollState.animateScrollTo((idx * 110).coerceAtLeast(0))
            } catch (e: Exception) { /* ignore */ }
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { item ->
                val isSelected = item == selected
                Surface(
                    onClick = { onSelect(item) },
                    shape = RoundedCornerShape(50),
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceContainerHigh,
                    modifier = Modifier.height(38.dp),
                ) {
                    Row(
                        Modifier.padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            item.icon,
                            contentDescription = null,
                            tint = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                   else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            item.title,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}

/**
 * محتوى التبويب النشط.
 */
@Composable
private fun TabContent(tab: EditorTab) {
    when (tab) {
        EditorTab.THEME -> ThemeTabContent()
        EditorTab.COLORS -> ColorsTabContent()
        EditorTab.SHAPES -> ShapesTabContent()
        EditorTab.FONTS -> FontsTabContent()
        EditorTab.BUTTONS -> ButtonsTabContent()
        EditorTab.BACKGROUND -> BackgroundTabContent()
        EditorTab.TEXT -> TextTabContent()
        EditorTab.EXPORT -> ExportTabContent()
    }
}

@Composable
private fun ThemeTabContent() {
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("🎨 المظهر", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("اختر اسم الثيم والكتابة والمؤلف من الأعلى.", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(8.dp))
        val theme = ThemeState.theme
        OutlinedTextField(
            value = theme.name,
            onValueChange = { ThemeState.update("name", it) },
            label = { Text("اسم الثيم") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = theme.author,
            onValueChange = { ThemeState.update("author", it) },
            label = { Text("المؤلف") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ColorsTabContent() {
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("🎨 الألوان", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("اختر من 700 لون جاهز أو خصّص كل لون.", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(8.dp))
        Text("(التفاصيل في Sheet الألوان)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ShapesTabContent() {
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("🔷 الأشكال", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("${com.futo.themestudiopro.data.ShapesGenerator.shapes.size} شكل هندسي متاح", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(8.dp))
        Text("اختر شكلًا من الشبكة.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun FontsTabContent() {
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("🔤 الخطوط", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("20 خط عربي + 20 خط إنجليزي من Google Fonts.", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(8.dp))
        val theme = ThemeState.theme
        Text(
            "الخط الحالي: ${theme.fontName ?: theme.arabicFontName ?: "لا يوجد"}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun ButtonsTabContent() {
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("🖼️ صور الأزرار", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("خصّص صورة لكل زر في الكيبورد.", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun BackgroundTabContent() {
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("🌄 الخلفية", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("اختر صورة خلفية للكيبورد.", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun TextTabContent() {
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("📝 النص", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("تحكم بحجم ووزن النص.", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(8.dp))
        val theme = ThemeState.theme
        Text("حجم النص: ${"%.2f".format(theme.scaleText)}×", style = MaterialTheme.typography.labelSmall)
        Text("وزن النص: ${theme.weightText.toInt()}", style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun ExportTabContent() {
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("📤 التصدير", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("صدّر الثيم كـ ZIP جاهز لـ FUTO Keyboard.", style = MaterialTheme.typography.bodySmall)
    }
}
