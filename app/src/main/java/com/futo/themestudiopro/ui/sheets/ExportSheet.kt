package com.futo.themestudiopro.ui.sheets

import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themestudiopro.data.ShapesGenerator
import com.futo.themestudiopro.data.ThemeState
import com.futo.themestudiopro.data.ZipPacker
import java.io.File

/**
 * تبويب التصدير — يحفظ ZIP جاهز لـ FUTO Keyboard.
 */
@Composable
fun ExportSheet() {
    val context = LocalContext.current
    val theme = ThemeState.theme
    var status by remember { mutableStateOf("") }
    var lastFile by remember { mutableStateOf<File?>(null) }

    val saver = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/zip")
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        try {
            // استخدام ZipPacker لحفظ الثيم في ملف مؤقت، ثم نسخه إلى URI
            val temp = File(context.cacheDir, "${safeFileName(theme.name)}.zip")
            ZipPacker.pack(
                context = context,
                theme = theme,
                fontBytes = null,         // TODO: يُملأ من FontDownloader لاحقًا
                backgroundBytes = null,   // TODO: يُملأ من BackgroundSheet لاحقًا
                outputFile = temp,
            )
            // نسخ إلى URI المحدد
            context.contentResolver.openOutputStream(uri)?.use { out ->
                temp.inputStream().use { it.copyTo(out) }
            }
            status = "✅ تم التصدير بنجاح"
            lastFile = temp
        } catch (e: Exception) {
            status = "❌ فشل التصدير: ${e.message}"
        }
    }

    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            "📤 التصدير",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "سيتم إنشاء ZIP يحتوي على theme.txt + الصور + الخط.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(12.dp))

        // ملخص
        Card(
            Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
        ) {
            Column(Modifier.padding(12.dp)) {
                SummaryRow("الاسم", theme.name)
                SummaryRow("المؤلف", theme.author.ifBlank { "—" })
                SummaryRow("المعرّف", theme.id)
                SummaryRow("الشكل", theme.shapeId ?: "افتراضي")
                SummaryRow("الخط", theme.fontName ?: theme.arabicFontName ?: "افتراضي")
                SummaryRow("الأشكال المتاحة", "${ShapesGenerator.shapes.size}")
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                saver.launch("${safeFileName(theme.name)}.zip")
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
        ) {
            Icon(Icons.Default.Archive, null)
            Spacer(Modifier.width(8.dp))
            Text("تصدير ZIP", fontWeight = FontWeight.Bold)
        }

        if (status.isNotBlank()) {
            Spacer(Modifier.height(12.dp))
            Card(Modifier.fillMaxWidth()) {
                Text(
                    status,
                    Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        // خيار المشاركة
        lastFile?.let { file ->
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/zip"
                            putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                        }
                        context.startActivity(Intent.createChooser(intent, "شارك الثيم"))
                    } catch (e: Exception) {
                        status = "❌ ${e.message}"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Default.Share, null)
                Spacer(Modifier.width(8.dp))
                Text("مشاركة")
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold, maxLines = 1)
    }
}

private fun safeFileName(name: String): String {
    return name
        .map { if (it.isLetterOrDigit() || it == '_' || it == '-') it else '_' }
        .joinToString("")
        .ifBlank { "futo-theme" }
}
