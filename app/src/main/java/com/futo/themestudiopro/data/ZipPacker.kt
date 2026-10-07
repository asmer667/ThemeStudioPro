package com.futo.themestudiopro.data

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
 *   <font>.ttf                 ← الخطوط (عربي + إنجليزي)
 *   background.png             ← صورة الخلفية
 *   Shapes/button.png          ← شكل الزر الرئيسي
 *   Key-<id>.png               ← صور الأزرار المخصّصة
 *   FUTOKeyboardTheme_Version  ← ملف الإصدار
 */
object ZipPacker {

    private const val VERSION_FILE = "FUTOKeyboardTheme_Version"
    private const val THEME_FILE = "theme.txt"
    private const val CURRENT_VERSION: Byte = 1

    /**
     * يحزم الثيم في ملف ZIP.
     */
    fun pack(
        theme: ThemeData,
        fontFiles: Map<String, ByteArray> = emptyMap(),
        backgroundBytes: ByteArray? = null,
        keyImageFiles: Map<String, ByteArray> = emptyMap(),
        shapeBytes: ByteArray? = null,
        outputFile: File,
    ): File {
        outputFile.parentFile?.mkdirs()

        // أسماء الخطوط للـ theme.txt
        val fontNames = fontFiles.keys.toSet()

        // أسماء الأشكال والصور للـ theme.txt
        val shapesList = mutableSetOf<String>()
        if (shapeBytes != null) shapesList.add("Shapes/button.png")
        keyImageFiles.keys.forEach { key -> shapesList.add("Shapes/$key.png") }

        val themeTxt = TomlGenerator.generate(
            theme = theme,
            fontsList = fontNames,
            hasBackground = backgroundBytes != null,
            shapesList = shapesList,
        )

        // ═══ فتح ZIP ═══
        ZipOutputStream(FileOutputStream(outputFile)).use { zos ->
            zos.setLevel(9)

            // ملف الإصدار
            zos.putNextEntry(ZipEntry(VERSION_FILE))
            zos.write(
                ByteBuffer.allocate(9).apply {
                    order(ByteOrder.LITTLE_ENDIAN)
                    put(CURRENT_VERSION)
                    putLong(Date().time)
                }.array()
            )
            zos.closeEntry()

            // theme.txt
            zos.putNextEntry(ZipEntry(THEME_FILE))
            zos.write(themeTxt.toByteArray(Charsets.UTF_8))
            zos.closeEntry()

            // الخطوط
            fontFiles.forEach { (name, bytes) ->
                zos.putNextEntry(ZipEntry(name))
                zos.write(bytes)
                zos.closeEntry()
            }

            // الخلفية
            if (backgroundBytes != null) {
                zos.putNextEntry(ZipEntry("background.png"))
                zos.write(backgroundBytes)
                zos.closeEntry()
            }

            // الشكل الرئيسي
            if (shapeBytes != null) {
                zos.putNextEntry(ZipEntry("Shapes/button.png"))
                zos.write(shapeBytes)
                zos.closeEntry()
            }

            // صور الأزرار الفردية
            keyImageFiles.forEach { (key, bytes) ->
                zos.putNextEntry(ZipEntry("Shapes/$key.png"))
                zos.write(bytes)
                zos.closeEntry()
            }
        }

        return outputFile
    }
}
