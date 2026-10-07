package com.futo.themestudiopro.data

import android.content.Context
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * المنسّق المركزي لتصدير الثيم.
 * يجمع:
 *   - الخطوط المحمّلة (عربي + إنجليزي) من FontsState
 *   - صور الأزرار من ThemeData.keyImages
 *   - صورة الخلفية من ThemeData.backgroundImage
 *   - شكل الزر الرئيسي من theme.shapeId
 * ثم يستدعي ZipPacker.pack()
 */
object ThemeExporter {

    /**
     * يُنتج ملف ZIP نهائيًا.
     *
     * @return الملف الناتج، أو null عند الفشل.
     */
    suspend fun export(
        context: Context,
        theme: ThemeData,
        outputFile: File,
    ): File? {
        return try {
            // 1. التحقق من صحة البيانات
            val error = theme.validate()
            if (error != null) {
                return null
            }

            // 2. جمع الخطوط
            val fontFiles = collectFonts(context, theme)

            // 3. جمع صور الأزرار
            val keyImageFiles = collectKeyImages(context, theme)

            // 4. جمع صورة الخلفية
            val backgroundBytes = collectBackground(context, theme)

            // 5. جمع شكل الزر الرئيسي
            val shapeBytes = collectShape(theme)

            // 6. التعبئة
            ZipPacker.pack(
                theme = theme,
                fontFiles = fontFiles,
                backgroundBytes = backgroundBytes,
                keyImageFiles = keyImageFiles,
                shapeBytes = shapeBytes,
                outputFile = outputFile,
            )

            outputFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // ═══════════════════════════════════════════════════════
    //  جمع البيانات
    // ═══════════════════════════════════════════════════════

    private fun collectFonts(context: Context, theme: ThemeData): Map<String, ByteArray> {
        val result = mutableMapOf<String, ByteArray>()

        theme.fontArabic?.let { fileName ->
            val bytes = readLocalFile(context, "fonts", fileName)
            if (bytes != null) result[fileName] = bytes
        }

        theme.fontEnglish?.let { fileName ->
            val bytes = readLocalFile(context, "fonts", fileName)
            if (bytes != null) result[fileName] = bytes
        }

        // دعم الخط القديم (fontName)
        if (result.isEmpty()) {
            theme.fontName?.let { fileName ->
                val bytes = readLocalFile(context, "fonts", fileName)
                if (bytes != null) result[fileName] = bytes
            }
        }

        return result
    }

    private fun collectKeyImages(context: Context, theme: ThemeData): Map<String, ByteArray> {
        val result = mutableMapOf<String, ByteArray>()

        theme.keyImages.forEach { (key, fileName) ->
            val bytes = readLocalFile(context, "key_images", fileName)
            if (bytes != null) {
                result[key] = bytes
            }
        }

        return result
    }

    private fun collectBackground(context: Context, theme: ThemeData): ByteArray? {
        val fileName = theme.backgroundImage ?: return null
        return readLocalFile(context, "key_images", fileName)
    }

    private fun collectShape(theme: ThemeData): ByteArray? {
        val shapeId = theme.shapeId ?: return null
        return try {
            val colorInt = try {
                com.futo.themestudiopro.utils.ColorUtils.parseColor(theme.keyboardContainer)
            } catch (e: Exception) {
                0xFF6750A4.toInt()
            }

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
    }

    // ═══════════════════════════════════════════════════════
    //  أدوات مساعدة
    // ═══════════════════════════════════════════════════════

    private fun readLocalFile(context: Context, dirName: String, fileName: String): ByteArray? {
        return try {
            val f = File(File(context.filesDir, dirName), fileName)
            if (f.exists()) f.readBytes() else null
        } catch (e: Exception) {
            null
        }
    }

    /**
     * يحوّل Bitmap إلى PNG bytes.
     */
    private fun bitmapToPng(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}
