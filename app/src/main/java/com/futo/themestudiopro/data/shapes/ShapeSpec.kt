package com.futo.themestudiopro.data.shapes

import androidx.compose.ui.graphics.Color

/**
 * مواصفة شكل واحد — كل ما يحتاجه Renderer لرسمه.
 */
data class ShapeSpec(
    val id: String,
    val familyId: String,
    val variantName: String,
    val primaryColor: Color,
    val secondaryColor: Color = primaryColor,
    val variant: Int = 0,
    val params: ShapeParams = ShapeParams(),
)

/**
 * إعدادات إضافية للشكل — تُستخدم بواسطة أدوات التحكم.
 */
data class ShapeParams(
    val widthScale: Float = 1.0f,
    val heightScale: Float = 1.0f,
    val cornerRadius: Float = 0.25f,
    val sharpness: Float = 1.0f,
    val tilt: Float = 0.0f,
    val depth: Float = 0.3f,
    val gloss: Float = 0.5f,
    val borderWidth: Float = 0.04f,
)
