package com.futo.themestudiopro.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * حالة عالمية للثيم.
 * أي تعديل هنا يُعيد رسم الواجهة تلقائيًا.
 */
object ThemeState {

    var theme: ThemeData by mutableStateOf(ThemeData())
        private set

    var darkMode: Boolean by mutableStateOf(false)

    var fontsVersion: Int by mutableStateOf(0)

    fun replace(newTheme: ThemeData) {
        theme = newTheme
    }

    fun updateColor(key: String, value: String) {
        theme = theme.withColor(key, value)
    }

    fun update(field: String, value: String) {
        theme = when (field) {
            "name" -> theme.copy(name = value)
            "author" -> theme.copy(author = value)
            "id" -> theme.copy(id = value)
            "description" -> theme.copy(description = value)
            else -> theme
        }
    }

    fun updateNumber(field: String, value: Float) {
        theme = when (field) {
            "roundedness" -> theme.copy(roundedness = value)
            "scale_text" -> theme.copy(scaleText = value)
            "scale_hints" -> theme.copy(scaleHints = value)
            "weight_text" -> theme.copy(weightText = value)
            "weight_hints" -> theme.copy(weightHints = value)
            "shape_rotation" -> theme.copy(shapeRotation = value)
            "shape_sharpness" -> theme.copy(shapeSharpness = value)
            "shape_tilt" -> theme.copy(shapeTilt = value)
            "key_width_scale" -> theme.copy(keyWidthScale = value)
            "key_height_scale" -> theme.copy(keyHeightScale = value)
            else -> theme
        }
    }

    fun updateBool(field: String, value: Boolean) {
        theme = when (field) {
            "auto_borders" -> theme.copy(autoBorders = value)
            "center_hints" -> theme.copy(centerHints = value)
            "dark_mode" -> {
                darkMode = value
                theme.copy(darkMode = value)
            }
            else -> theme
        }
    }

    fun toggleDarkMode() {
        darkMode = !darkMode
        theme = theme.copy(darkMode = darkMode)
    }

    fun reset() {
        theme = ThemeData()
        darkMode = false
    }
}
