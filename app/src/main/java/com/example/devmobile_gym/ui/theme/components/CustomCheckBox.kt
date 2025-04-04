package com.example.devmobile_gym.ui.theme.components

import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomCheckbox() {
    var isChecked by remember { mutableStateOf(false) }

    CustomCheckbox(checked = isChecked, onCheckedChange = { isChecked = it })
}