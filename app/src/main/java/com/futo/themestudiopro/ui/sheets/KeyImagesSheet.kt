package com.futo.themestudiopro.ui.sheets

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
 * واجهة صور الأزرار — 56 زر يمكن إضافة صورة لكل منها.
 */
@Composable
fun KeyImagesSheet() {
    val context = LocalContext.current
    val theme = ThemeState.theme

    var pendingKeyId by remember { mutableStateOf<String?>(null) }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri: Uri? ->
        if (uri != null && pendingKeyId != null) {
            val fileName = KeyImageManager.copyToLocal(context, uri, pendingKeyId!!)
            if (fileName != null) {
                val newMap = theme.keyImages.toMutableMap()
                newMap[pendingKeyId!!] = fileName
                ThemeState.replace(theme.copy(keyImages = newMap))
            }
        }
        pendingKeyId = null
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 12.dp),
    ) {
        Text(
            text = "🖼️ صور الأزرار",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Text(
            text = "أضف صورة لكل زر — ${KeyImageManager.ALL_KEYS.size} زر متاح",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
        ) {
            items(KeyImageManager.ALL_KEYS, key = { it.first }) { (keyId, label) ->
                KeyImageItem(
                    keyId = keyId,
                    label = label,
                    imageFileName = theme.keyImages[keyId],
                    onClick = {
                        pendingKeyId = keyId
                        picker.launch(arrayOf("image/*"))
                    },
                    onLongClick = {
                        // حذف الصورة
                        val existing = theme.keyImages[keyId]
                        if (existing != null) {
                            KeyImageManager.deleteLocal(context, existing)
                            val newMap = theme.keyImages.toMutableMap()
                            newMap.remove(keyId)
                            ThemeState.replace(theme.copy(keyImages = newMap))
                        }
                    },
                )
            }
        }

        Text(
            text = "💡 ضغطة مطوّلة على أي زر لحذف صورته",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
private fun KeyImageItem(
    keyId: String,
    label: String,
    imageFileName: String?,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val context = LocalContext.current
    val bitmap = remember(imageFileName) {
        if (imageFileName != null) {
            try {
                val f = KeyImageManager.localFile(context, imageFileName)
                if (f.exists()) BitmapFactory.decodeFile(f.absolutePath) else null
            } catch (e: Exception) {
                null
            }
        } else null
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                RoundedCornerShape(10.dp),
            )
            .clickable(onClick = onClick)
            .then(
                if (imageFileName != null) {
                    Modifier
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = label,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
