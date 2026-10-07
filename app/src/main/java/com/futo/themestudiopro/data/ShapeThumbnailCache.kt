package com.futo.themestudiopro.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache

/**
 * ذاكرة تخزين مؤقت لصور الأشكال داخل Compose.
 * تُحوّل ByteArray من ShapesGenerator إلى Bitmap مرة واحدة ثم تُخزّنها.
 */
object ShapeThumbnailCache {

    private const val MAX_ENTRIES = 200

    private val cache = object : LruCache<String, Bitmap>(MAX_ENTRIES) {
        override fun sizeOf(key: String, value: Bitmap): Int = 1
    }

    /**
     * يُعيد Bitmap للشكل — من الذاكرة إن وُجد، أو يُنتجه ويُخزّنه.
     */
    fun get(
        shapeId: String,
        fillColor: Int,
        rotation: Float = 0f,
        sharpness: Float = 0.5f,
        tilt: Float = 0f,
        size: Int = 128,
    ): Bitmap? {
        val key = "${shapeId}_${fillColor}_${rotation}_${sharpness}_${tilt}_$size"
        cache.get(key)?.let { return it }

        return try {
            val bytes = ShapesGenerator.generatePNG(
                shapeId = shapeId,
                fillColor = fillColor,
                rotation = rotation,
                sharpness = sharpness,
                tilt = tilt,
                size = size,
                is3D = shapeId.startsWith("3d_"),
            ) ?: return null

            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return null
            cache.put(key, bitmap)
            bitmap
        } catch (e: Exception) {
            null
        }
    }

    /** يمسح الذاكرة المؤقتة. */
    fun clear() {
        cache.evictAll()
    }
}
