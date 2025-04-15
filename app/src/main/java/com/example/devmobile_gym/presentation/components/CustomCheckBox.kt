package com.example.devmobile_gym.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = Modifier.padding(start = 5.dp),
        colors = CheckboxDefaults.colors(
            uncheckedColor = Color(0xFFC1C3C6),  // Cor do contorno quando NÃO está marcado
            checkedColor = Color.Green,  // Cor de fundo quando marcado
            checkmarkColor = Color.White // Cor do ícone de check quando marcado
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomCheckbox() {
    var isChecked by remember { mutableStateOf(false) }

    CustomCheckbox(checked = isChecked, onCheckedChange = { isChecked = it })
}