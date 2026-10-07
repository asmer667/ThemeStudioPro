package com.futo.themestudiopro.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

/**
 * يُنزّل الخطوط من Google Fonts ويُخزّنها محليًا.
 */
object FontDownloader {

    fun fontsDir(context: Context): File {
        val dir = File(context.filesDir, "fonts")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun localFile(context: Context, fileName: String): File {
        return File(fontsDir(context), fileName)
    }

    fun isDownloaded(context: Context, fileName: String): Boolean {
        val f = localFile(context, fileName)
        return f.exists() && f.length() > 100
    }

    suspend fun download(context: Context, font: FontInfo): File? = withContext(Dispatchers.IO) {
        val target = localFile(context, font.fileName)
        if (target.exists() && target.length() > 100) return@withContext target

        return@withContext try {
            val url = URL(font.url)
            url.openStream().use { input ->
                target.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            if (target.length() > 100) target else null
        } catch (e: Exception) {
            target.delete()
            null
        }
    }

    suspend fun clearAll(context: Context) = withContext(Dispatchers.IO) {
        fontsDir(context).listFiles()?.forEach { it.delete() }
        Unit
    }

    fun sizeMb(context: Context, fileName: String): Float {
        val f = localFile(context, fileName)
        return if (f.exists()) f.length() / 1024f / 1024f else 0f
    }
}
