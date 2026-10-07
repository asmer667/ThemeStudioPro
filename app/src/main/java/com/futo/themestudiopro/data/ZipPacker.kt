package com.futo.themestudiopro.data

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Date
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * يحزم الثيم في ZIP بصيغة FUTO Keyboard الرسمية.
 *
 * البنية الناتجة:
 *   theme.txt                  ← الإعدادات
 *   <font>.ttf                 ← الخط
 *   background.png             ← صورة الخلفية
 *   Shapes/*.png               ← الأشكال
 *   Key-*.png                  ← صور الأزرار
 *   Icon-*.png                 ← الأيقونات
 *   FUTOKeyboardTheme_Version  ← ملف الإصدار
 */
object ZipPacker {

    private const val VERSION_FILE = "FUTOKeyboardTheme_Version"
    private const val THEME_FILE = "theme.txt"
    private const val CURRENT_VERSION: Byte = 1

    /**
     * يحزم الثيم في ملف ZIP.
     *
     * @param context سياق التطبيق
     * @param theme بيانات الثيم
     * @param fontBytes بايتات الخط (أو null)
     * @param backgroundBytes بايتات صورة الخلفية (أو null)
     * @param outputFile الملف الناتج
     */
    fun pack(
        context: Context,
        theme: ThemeData,
        fontBytes: ByteArray?,
        backgroundBytes: ByteArray?,
        outputFile: File,
    ): File {
        outputFile.parentFile?.mkdirs()

        // ═══ توليد theme.txt ═══
        val fontSet = if (fontBytes != null) {
            val name = theme.fontName ?: theme.arabicFontName ?: "CustomFont.ttf"
            setOf(if (name.endsWith(".ttf") || name.endsWith(".otf")) name else "$name.ttf")
        } else emptySet()

        val shapesList = buildShapeAssets(theme)
        val themeTxt = TomlGenerator.generate(
            theme = theme,
            fontsList = fontSet,
            hasBackground = backgroundBytes != null,
            shapesList = shapesList.keys,
        )

        // ═══ فتح ZIP ═══
        ZipOutputStream(FileOutputStream(outputFile)).use { zos ->
            zos.setLevel(9)

            // ملف الإصدار
            zos.putNextEntry(ZipEntry(VERSION_FILE))
            zos.write(ByteBuffer.allocate(9).apply {
                order(ByteOrder.LITTLE_ENDIAN)
                put(CURRENT_VERSION)
                putLong(Date().time)
            }.array())
            zos.closeEntry()

            // theme.txt
            zos.putNextEntry(ZipEntry(THEME_FILE))
            zos.write(themeTxt.toByteArray(Charsets.UTF_8))
            zos.closeEntry()

            // الخط
            if (fontBytes != null) {
                val name = theme.fontName ?: theme.arabicFontName ?: "CustomFont.ttf"
                val actualName = if (name.endsWith(".ttf") || name.endsWith(".otf")) name else "$name.ttf"
                zos.putNextEntry(ZipEntry(actualName))
                zos.write(fontBytes)
                zos.closeEntry()
            }

            // صورة الخلفية
            if (backgroundBytes != null) {
                zos.putNextEntry(ZipEntry("background.png"))
                zos.write(backgroundBytes)
                zos.closeEntry()
            }

            // الأشكال
            shapesList.forEach { (name, bytes) ->
                zos.putNextEntry(ZipEntry(name))
                zos.write(bytes)
                zos.closeEntry()
            }
        }

        return outputFile
    }

    /**
     * توليد صور الأشكال المُختارة في الثيم.
     */
    private fun buildShapeAssets(theme: ThemeData): Map<String, ByteArray> {
        val result = mutableMapOf<String, ByteArray>()
        val colorInt = try {
            com.futo.themestudiopro.utils.ColorUtils.parseColor(theme.keyboardContainer)
        } catch (e: Exception) {
            0xFF6750A4.toInt()
        }

        // شكل عام
        theme.shapeId?.let { shapeId ->
            val bytes = try {
                ShapesGenerator.generatePNG(
                    shapeId = shapeId,
                    fillColor = colorInt,
                    rotation = theme.shapeRotation,
                    sharpness = theme.shapeSharpness,
                    tilt = theme.shapeTilt,
                    size = 256,
                    is3D = shapeId.startsWith("3d_"),
                )
            } catch (e: Exception) {
                null
            }
            if (bytes != null) {
                result["Shapes/button.png"] = bytes
            }
        }

        return result
    }
}
