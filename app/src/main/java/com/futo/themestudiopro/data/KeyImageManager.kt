package com.futo.themestudiopro.data

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

/**
 * إدارة صور الأزرار — نسخ من الجهاز إلى filesDir/key_images/
 */
object KeyImageManager {

    /**
     * قائمة الأزرار المدعومة (56 زر).
     * كل عنصر: (key: معرّف فريد، label: اسم العرض).
     */
    val ALL_KEYS: List<Pair<String, String>> = buildList {
        // الأرقام
        for (i in 0..9) add("digit_$i" to i.toString())
        // الحروف الإنجليزية
        ('a'..'z').forEach { add("letter_$it" to it.uppercaseChar().toString()) }
        // الرموز الشائعة
        addAll(
            listOf(
                "backspace" to "⌫",
                "enter" to "↵",
                "shift" to "⇧",
                "space" to "مسافة",
                "comma" to ",",
                "period" to ".",
                "question" to "?",
                "exclamation" to "!",
                "emoji" to "😊",
                "symbols" to "؟١٢٣",
                "dash" to "-",
                "quote" to "'",
                "slash" to "/",
                "at" to "@",
                "hash" to "#",
                "tab" to "⇥",
                "caps" to "⇪",
                "ctrl" to "Ctrl",
                "alt" to "Alt",
                "settings" to "⚙",
            )
        )
    }

    fun imagesDir(context: Context): File {
        val dir = File(context.filesDir, "key_images")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    /**
     * ينسخ صورة من URI إلى المجلد المحلي.
     * يُعيد اسم الملف الجديد أو null.
     */
    fun copyToLocal(context: Context, uri: Uri, keyId: String): String? {
        return try {
            val ext = context.contentResolver.getType(uri)?.substringAfterLast("/") ?: "png"
            val fileName = "${keyId}_${UUID.randomUUID().toString().take(8)}.$ext"
            val target = File(imagesDir(context), fileName)
            context.contentResolver.openInputStream(uri)?.use { input ->
                target.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            if (target.exists() && target.length() > 0) fileName else null
        } catch (e: Exception) {
            null
        }
    }

    /**
     * يحذف صورة زر من المجلد المحلي.
     */
    fun deleteLocal(context: Context, fileName: String) {
        try {
            File(imagesDir(context), fileName).delete()
        } catch (_: Exception) {}
    }

    /**
     * مسار ملف صورة.
     */
    fun localFile(context: Context, fileName: String): File {
        return File(imagesDir(context), fileName)
    }
}
