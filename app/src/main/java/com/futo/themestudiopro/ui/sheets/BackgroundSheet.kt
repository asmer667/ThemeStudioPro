package com.futo.themestudiopro.ui.sheets

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themestudiopro.data.KeyImageManager
import com.futo.themestudiopro.data.ThemeState
import android.graphics.BitmapFactory

/**
 * واجهة صورة الخلفية.
 */
@Composable
fun BackgroundSheet() {
    val context = LocalContext.current
    val theme = ThemeState.theme

    var pendingUri by remember { mutableStateOf<Uri?>(null) }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = KeyImageManager.copyToLocal(context, uri, "background")
            if (fileName != null) {
                ThemeState.replace(theme.copy(backgroundImage = fileName))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 12.dp),
    ) {
        Text(
            text = "🌅 الخلفية",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Text(
            text = "اختر صورة خلفية للكيبورد",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )

        Spacer(Modifier.height(16.dp))

        // معاينة
        val bitmap = remember(theme.backgroundImage) {
            val name = theme.backgroundImage
            if (name != null) {
                try {
                    val f = KeyImageManager.localFile(context, name)
                    if (f.exists()) BitmapFactory.decodeFile(f.absolutePath) else null
                } catch (e: Exception) { null }
            } else null
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .aspectRatio(1.8f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
            contentAlignment = Alignment.Center,
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "الخلفية",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Text(
                    text = "لا توجد صورة",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Button(
                onClick = { picker.launch(arrayOf("image/*")) },
                modifier = Modifier.weight(1f),
            ) {
                Text(if (theme.backgroundImage == null) "📥 اختر صورة" else "🔄 تغيير")
            }
            if (theme.backgroundImage != null) {
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        val old = theme.backgroundImage
                        if (old != null) KeyImageManager.deleteLocal(context, old)
                        ThemeState.replace(theme.copy(backgroundImage = null))
                    },
                    modifier = Modifier.padding(start = 8.dp),
                ) {
                    Text("حذف")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // شفافية الخلفية
        Text(
            text = "الشفافية: %.0f%%".format(theme.backgroundOpacity * 100),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Slider(
            value = theme.backgroundOpacity,
            onValueChange = { ThemeState.replace(theme.copy(backgroundOpacity = it)) },
            valueRange = 0.1f..1.0f,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}
