package com.futo.themestudiopro.ui.sheets

import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.futo.themestudiopro.data.ThemeExporter
import com.futo.themestudiopro.data.ThemeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * واجهة التصدير — تحفظ ZIP جاهز لـ FUTO Keyboard.
 */
@Composable
fun ExportSheet() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val theme = ThemeState.theme

    var isExporting by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }
    var lastFile by remember { mutableStateOf<File?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 12.dp),
    ) {
        Text(
            text = "📤 التصدير",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Text(
            text = "صدّر ZIP جاهزًا لـ FUTO Keyboard",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )

        Spacer(Modifier.height(16.dp))

        // ملخص الثيم
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "ملخص الثيم",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(8.dp))
                SummaryRow("الاسم", theme.name)
                SummaryRow("المعرّف", theme.id)
                SummaryRow("المؤلف", theme.author)
                SummaryRow("الخط العربي", theme.fontArabic ?: "—")
                SummaryRow("الخط الإنجليزي", theme.fontEnglish ?: "—")
                SummaryRow("صور الأزرار", "${theme.keyImages.size} صورة")
                SummaryRow("الخلفية", if (theme.backgroundImage != null) "مضافة ✓" else "—")
                SummaryRow("الشكل", theme.shapeId ?: "—")
            }
        }

        Spacer(Modifier.height(16.dp))

        // زر التصدير
        Button(
            onClick = {
                isExporting = true
                status = ""
                errorMsg = ""
                lastFile = null

                scope.launch {
                    val result = withContext(Dispatchers.IO) {
                        exportTheme(context, theme)
                    }
                    isExporting = false
                    if (result != null) {
                        lastFile = result
                        status = "✅ تم التصدير: ${result.name}\n📁 ${result.parentFile?.absolutePath}"
                    } else {
                        errorMsg = "⚠️ فشل التصدير — تحقق من اسم الثيم والمعرّف"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(52.dp),
            enabled = !isExporting,
        ) {
            if (isExporting) {
                CircularProgressIndicator(
                    modifier = Modifier.height(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                Icon(Icons.Default.Archive, contentDescription = null)
                Spacer(Modifier.height(8.dp))
                Text("📦 تصدير الثيم كـ ZIP", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(16.dp))

        // الحالة
        if (status.isNotBlank()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            ) {
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                )
            }
            Spacer(Modifier.height(8.dp))

            // زر مشاركة
            lastFile?.let { file ->
                OutlinedButton(
                    onClick = { shareFile(context, file) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(Modifier.height(8.dp))
                    Text("📤 مشاركة ZIP")
                }
            }
        }

        if (errorMsg.isNotBlank()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                ),
            ) {
                Text(
                    text = errorMsg,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
        )
    }
}

/**
 * يُصدّر الثيم إلى مجلد التنزيلات.
 */
private suspend fun exportTheme(
    context: android.content.Context,
    theme: com.futo.themestudiopro.data.ThemeData,
): File? {
    return try {
        val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val appDir = File(downloads, "ThemeStudioPro")
        if (!appDir.exists()) appDir.mkdirs()

        val safeName = theme.name
            .replace("[^a-zA-Z0-9_\\-]".toRegex(), "_")
            .take(40)
            .ifBlank { "theme" }

        val output = File(appDir, "$safeName.zip")

        // حذف النسخة القديمة
        if (output.exists()) output.delete()

        ThemeExporter.export(
            context = context,
            theme = theme,
            outputFile = output,
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * يشارك ملف ZIP.
 */
private fun shareFile(context: android.content.Context, file: File) {
    try {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file,
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/zip"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "مشاركة الثيم"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
