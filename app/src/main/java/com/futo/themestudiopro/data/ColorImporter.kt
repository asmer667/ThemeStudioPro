package com.futo.themestudiopro.data

import android.content.Context
import android.net.Uri

/**
 * يستورد ألوانًا من ملف على الجهاز.
 * يدعم تلقائيًا: JSON, TXT, CSV.
 */
object ColorImporter {

    fun importFromUri(context: Context, uri: Uri): List<String> {
        val text = try {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                stream.bufferedReader().readText()
            } ?: return emptyList()
        } catch (e: Exception) {
            return emptyList()
        }
        return parseColors(text)
    }

    fun parseColors(text: String): List<String> {
        val colors = mutableListOf<String>()
        val regex = Regex("#[0-9A-Fa-f]{6}(?:[0-9A-Fa-f]{2})?")
        val matches = regex.findAll(text)
        for (match in matches) {
            val hex = match.value.uppercase()
            val normalized = normalizeHex(hex)
            if (normalized != null && !colors.contains(normalized)) {
                colors.add(normalized)
            }
        }
        return colors
    }

    private fun normalizeHex(hex: String): String? {
        val raw = hex.removePrefix("#")
        return when (raw.length) {
            6 -> "#FF$raw"
            8 -> "#$raw"
            else -> null
        }
    }
}
