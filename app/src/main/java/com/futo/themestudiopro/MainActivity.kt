package com.futo.themestudiopro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.futo.themestudiopro.data.ThemeState
import com.futo.themestudiopro.ui.screens.EditorScreen
import com.futo.themestudiopro.ui.theme.ThemeStudioTheme

/**
 * النشاط الرئيسي — نقطة دخول التطبيق.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ThemeStudioTheme(darkTheme = ThemeState.darkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    EditorScreen()
                }
            }
        }
    }
}
