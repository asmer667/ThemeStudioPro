package com.futo.themestudiopro.data

import kotlinx.serialization.Serializable

@Serializable
data class ThemeData(
    // ═══════════ معلومات أساسية ═══════════
    val name: String = "My Theme",
    val author: String = "Anonymous",
    val id: String = "custom.my.theme",
    val version: Int = 2,
    val description: String = "Created with Theme Studio Pro",

    // ═══════════ 44 لونًا (FUTO يقبل صيغة #RRGGBBAA) ═══════════
    // Primary Group
    val primary: String = "#FF6750A4",
    val onPrimary: String = "#FFFFFFFF",
    val primaryContainer: String = "#FFEADDFF",
    val onPrimaryContainer: String = "#FF21005D",
    val inversePrimary: String = "#FFD0BCFF",

    // Secondary Group
    val secondary: String = "#FF625B71",
    val onSecondary: String = "#FFFFFFFF",
    val secondaryContainer: String = "#FFE8DEF8",
    val onSecondaryContainer: String = "#FF1D192B",

    // Tertiary Group
    val tertiary: String = "#FF7D5260",
    val onTertiary: String = "#FFFFFFFF",
    val tertiaryContainer: String = "#FFFFD8E4",
    val onTertiaryContainer: String = "#FF31111D",

    // Background Group
    val background: String = "#FFFFFBFE",
    val onBackground: String = "#FF1C1B1F",

    // Surface Group
    val surface: String = "#FFFFFBFE",
    val onSurface: String = "#FF1C1B1F",
    val surfaceVariant: String = "#FFE7E0EC",
    val onSurfaceVariant: String = "#FF49454F",
    val surfaceTint: String = "#FF6750A4",
    val inverseSurface: String = "#FF313033",
    val inverseOnSurface: String = "#FFF4EFF4",

    // Error Group
    val error: String = "#FFB3261E",
    val onError: String = "#FFFFFFFF",
    val errorContainer: String = "#FFF9DEDC",
    val onErrorContainer: String = "#FF410E0B",

    // Outline Group
    val outline: String = "#FF79747E",
    val outlineVariant: String = "#FFCAC4D0",
    val scrim: String = "#FF000000",

    // Surface Containers
    val surfaceBright: String = "#FFFFFBFE",
    val surfaceDim: String = "#FFDED8E1",
    val surfaceContainer: String = "#FFF3EDF7",
    val surfaceContainerHigh: String = "#FFECE6F0",
    val surfaceContainerHighest: String = "#FFE6E0E9",
    val surfaceContainerLow: String = "#FFF7F2FA",
    val surfaceContainerLowest: String = "#FFFFFFFF",

    // Keyboard-specific
    val keyboardSurface: String = "#FFECE6F0",
    val keyboardSurfaceDim: String = "#FFDED8E1",
    val keyboardContainer: String = "#FFE7E0EC",
    val keyboardContainerVariant: String = "#FFD0BCFF",
    val onKeyboardContainer: String = "#FF1C1B1F",
    val keyboardPress: String = "#FF6750A4",
    val keyboardContainerPressed: String = "#FFB39DDB",
    val onKeyboardContainerPressed: String = "#FF1C1B1F",

    // ═══════════ إعدادات عامة ═══════════
    val autoBorders: Boolean = true,
    val centerHints: Boolean = false,
    val roundedness: Float = 0.5f,
    val scaleText: Float = 1.0f,
    val scaleHints: Float = 0.9f,
    val weightText: Float = 400.0f,
    val weightHints: Float = 500.0f,
    val backgroundOpacity: Float = 1.0f,
    val actionBarOpacity: Float = 0.26f,

    // ═══════════ الخطوط ═══════════
    val fontName: String? = null,
    val arabicFontName: String? = null,
    val englishFontName: String? = null,

    // ═══════════ الأشكال ═══════════
    val shapeId: String? = null,
    val shapeRotation: Float = 0f,
    val shapeSharpness: Float = 0.5f,
    val shapeTilt: Float = 0f,
    val keyWidthScale: Float = 1.0f,
    val keyHeightScale: Float = 1.0f,

    // ═══════════ الصور ═══════════
    val backgroundImage: String? = null,

    // ═══════════ الوضع الليلي ═══════════
    val darkMode: Boolean = false,
)

// ═══════════════════════════════════════════════════════
// دوال مساعدة
// ═══════════════════════════════════════════════════════

/**
 * تُرجع قائمة بكل الألوان بأسماء FUTO الرسمية.
 * ملاحظة: الأسماء بصيغة snake_case كما يتوقعها theme.txt.
 */
fun ThemeData.allColorsForExport(): List<Pair<String, String>> = listOf(
    "primary" to primary,
    "on_primary" to onPrimary,
    "primary_container" to primaryContainer,
    "on_primary_container" to onPrimaryContainer,
    "inverse_primary" to inversePrimary,
    "secondary" to secondary,
    "on_secondary" to onSecondary,
    "secondary_container" to secondaryContainer,
    "on_secondary_container" to onSecondaryContainer,
    "tertiary" to tertiary,
    "on_tertiary" to onTertiary,
    "tertiary_container" to tertiaryContainer,
    "on_tertiary_container" to onTertiaryContainer,
    "background" to background,
    "on_background" to onBackground,
    "surface" to surface,
    "on_surface" to onSurface,
    "surface_variant" to surfaceVariant,
    "on_surface_variant" to onSurfaceVariant,
    "surface_tint" to surfaceTint,
    "inverse_surface" to inverseSurface,
    "inverse_on_surface" to inverseOnSurface,
    "error" to error,
    "on_error" to onError,
    "error_container" to errorContainer,
    "on_error_container" to onErrorContainer,
    "outline" to outline,
    "outline_variant" to outlineVariant,
    "scrim" to scrim,
    "surface_bright" to surfaceBright,
    "surface_dim" to surfaceDim,
    "surface_container" to surfaceContainer,
    "surface_container_high" to surfaceContainerHigh,
    "surface_container_highest" to surfaceContainerHighest,
    "surface_container_low" to surfaceContainerLow,
    "surface_container_lowest" to surfaceContainerLowest,
    "keyboard_surface" to keyboardSurface,
    "keyboard_surface_dim" to keyboardSurfaceDim,
    "keyboard_container" to keyboardContainer,
    "keyboard_container_variant" to keyboardContainerVariant,
    "on_keyboard_container" to onKeyboardContainer,
    "keyboard_press" to keyboardPress,
    "keyboard_container_pressed" to keyboardContainerPressed,
    "on_keyboard_container_pressed" to onKeyboardContainerPressed,
)

/**
 * تُحدّث لونًا واحدًا بالاسم.
 */
fun ThemeData.withColor(key: String, value: String): ThemeData = when (key) {
    "primary" -> copy(primary = value)
    "on_primary" -> copy(onPrimary = value)
    "primary_container" -> copy(primaryContainer = value)
    "on_primary_container" -> copy(onPrimaryContainer = value)
    "inverse_primary" -> copy(inversePrimary = value)
    "secondary" -> copy(secondary = value)
    "on_secondary" -> copy(onSecondary = value)
    "secondary_container" -> copy(secondaryContainer = value)
    "on_secondary_container" -> copy(onSecondaryContainer = value)
    "tertiary" -> copy(tertiary = value)
    "on_tertiary" -> copy(onTertiary = value)
    "tertiary_container" -> copy(tertiaryContainer = value)
    "on_tertiary_container" -> copy(onTertiaryContainer = value)
    "background" -> copy(background = value)
    "on_background" -> copy(onBackground = value)
    "surface" -> copy(surface = value)
    "on_surface" -> copy(onSurface = value)
    "surface_variant" -> copy(surfaceVariant = value)
    "on_surface_variant" -> copy(onSurfaceVariant = value)
    "surface_tint" -> copy(surfaceTint = value)
    "inverse_surface" -> copy(inverseSurface = value)
    "inverse_on_surface" -> copy(inverseOnSurface = value)
    "error" -> copy(error = value)
    "on_error" -> copy(onError = value)
    "error_container" -> copy(errorContainer = value)
    "on_error_container" -> copy(onErrorContainer = value)
    "outline" -> copy(outline = value)
    "outline_variant" -> copy(outlineVariant = value)
    "scrim" -> copy(scrim = value)
    "surface_bright" -> copy(surfaceBright = value)
    "surface_dim" -> copy(surfaceDim = value)
    "surface_container" -> copy(surfaceContainer = value)
    "surface_container_high" -> copy(surfaceContainerHigh = value)
    "surface_container_highest" -> copy(surfaceContainerHighest = value)
    "surface_container_low" -> copy(surfaceContainerLow = value)
    "surface_container_lowest" -> copy(surfaceContainerLowest = value)
    "keyboard_surface" -> copy(keyboardSurface = value)
    "keyboard_surface_dim" -> copy(keyboardSurfaceDim = value)
    "keyboard_container" -> copy(keyboardContainer = value)
    "keyboard_container_variant" -> copy(keyboardContainerVariant = value)
    "on_keyboard_container" -> copy(onKeyboardContainer = value)
    "keyboard_press" -> copy(keyboardPress = value)
    "keyboard_container_pressed" -> copy(keyboardContainerPressed = value)
    "on_keyboard_container_pressed" -> copy(onKeyboardContainerPressed = value)
    else -> this
}

/**
 * تتحقق من صحة البيانات قبل التصدير.
 * تُرجع null إن كانت صالحة، أو رسالة الخطأ.
 */
fun ThemeData.validate(): String? {
    if (name.isBlank()) return "اسم الثيم فارغ"
    if (id.isBlank()) return "معرّف الثيم (ID) فارغ"
    if (id.contains(" ")) return "المعرّف لا يمكن أن يحتوي على مسافات"
    return null
}
