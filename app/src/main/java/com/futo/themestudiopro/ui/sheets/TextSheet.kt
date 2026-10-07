package com.futo.themestudiopro.ui.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themestudiopro.data.ThemeState

/**
 * واجهة النص — تحكم بحجم ووزن النص والتلميحات.
 */
@Composable
fun TextSheet() {
    val theme = ThemeState.theme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 12.dp),
    ) {
        Text(
            text = "📝 النص",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Text(
            text = "تحكم بحجم ووزن النص والتلميحات",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )

        Spacer(Modifier.height(16.dp))

        TextControl(
            label = "حجم النص",
            value = theme.scaleText,
            range = 0.5f..2.0f,
            onChange = { ThemeState.replace(theme.copy(scaleText = it)) },
        )
        TextControl(
            label = "وزن النص",
            value = theme.weightText,
            range = 100f..900f,
            onChange = { ThemeState.replace(theme.copy(weightText = it)) },
        )
        TextControl(
            label = "حجم التلميحات",
            value = theme.scaleHints,
            range = 0.5f..1.5f,
            onChange = { ThemeState.replace(theme.copy(scaleHints = it)) },
        )
        TextControl(
            label = "وزن التلميحات",
            value = theme.weightHints,
            range = 100f..900f,
            onChange = { ThemeState.replace(theme.copy(weightHints = it)) },
        )

        Spacer(Modifier.height(16.dp))

        // خيارات إضافية
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("حدود تلقائية للأزرار", style = MaterialTheme.typography.bodyMedium)
            Switch(
                checked = theme.autoBorders,
                onCheckedChange = { ThemeState.replace(theme.copy(autoBorders = it)) },
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("توسيط التلميحات", style = MaterialTheme.typography.bodyMedium)
            Switch(
                checked = theme.centerHints,
                onCheckedChange = { ThemeState.replace(theme.copy(centerHints = it)) },
            )
        }
    }
}

@Composable
private fun TextControl(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onChange: (Float) -> Unit,
) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium)
            Text(
                text = "%.2f".format(value),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Slider(
            value = value,
            onValueChange = onChange,
            valueRange = range,
        )
    }
}
