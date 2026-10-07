package com.futo.themestudiopro.ui.sheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themestudiopro.data.ThemeState

/**
 * واجهة المظهر — اسم الثيم والمؤلف.
 */
@Composable
fun AppearanceSheet() {
    val theme = ThemeState.theme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 12.dp),
    ) {
        Text(
            text = "🎨 المظهر",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Text(
            text = "اسم الثيم والمؤلف والوصف",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = theme.name,
            onValueChange = { ThemeState.replace(theme.copy(name = it)) },
            label = { Text("اسم الثيم") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true,
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = theme.author,
            onValueChange = { ThemeState.replace(theme.copy(author = it)) },
            label = { Text("المؤلف") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true,
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = theme.id,
            onValueChange = { ThemeState.replace(theme.copy(id = it)) },
            label = { Text("المعرّف (id)") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true,
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = theme.description,
            onValueChange = { ThemeState.replace(theme.copy(description = it)) },
            label = { Text("الوصف") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        )

        Spacer(Modifier.height(16.dp))

        // استدارة الحواف
        Text(
            text = "استدارة الحواف: %.0f%%".format(theme.roundedness * 100),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Slider(
            value = theme.roundedness,
            onValueChange = { ThemeState.replace(theme.copy(roundedness = it)) },
            valueRange = 0f..1f,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}
