package com.futo.themestudiopro.data

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

/**
 * حالة الخطوط المحمّلة + إدارة FontFamily.
 */
object FontsState {

    val arabicFontFamily = mutableStateOf<FontFamily?>(null)
    val englishFontFamily = mutableStateOf<FontFamily?>(null)

    /** خريطة: fileName → 0=لم يُحمَّل، 1=جاري، 2=جاهز */
    val downloadStates = mutableStateMapOf<String, Int>()

    suspend fun downloadAndLoad(context: Context, font: FontInfo): Boolean {
        if (downloadStates[font.fileName] == 1) return false
        if (FontDownloader.isDownloaded(context, font.fileName)) {
            downloadStates[font.fileName] = 2
            return true
        }

        downloadStates[font.fileName] = 1
        val file = FontDownloader.download(context, font)
        if (file != null) {
            downloadStates[font.fileName] = 2
            return true
        } else {
            downloadStates[font.fileName] = 0
            return false
        }
    }

    fun loadArabicFamily(context: Context, fileName: String?) {
        if (fileName.isNullOrBlank()) {
            arabicFontFamily.value = null
            return
        }
        val file = FontDownloader.localFile(context, fileName)
        arabicFontFamily.value = try {
            if (file.exists() && file.length() > 100) FontFamily(Font(file)) else null
        } catch (e: Exception) {
            null
        }
    }

    fun loadEnglishFamily(context: Context, fileName: String?) {
        if (fileName.isNullOrBlank()) {
            englishFontFamily.value = null
            return
        }
        val file = FontDownloader.localFile(context, fileName)
        englishFontFamily.value = try {
            if (file.exists() && file.length() > 100) FontFamily(Font(file)) else null
        } catch (e: Exception) {
            null
        }
    }

    fun reset() {
        arabicFontFamily.value = null
        englishFontFamily.value = null
        downloadStates.clear()
    }
}
