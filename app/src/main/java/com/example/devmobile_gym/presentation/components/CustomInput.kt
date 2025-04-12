package com.example.devmobile_gym.presentation.components

// Arquivo: CustomTextField.kt

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(label: String, value: String, onValueChange: (String) -> Unit, padding : Int) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
            .padding(padding.dp),
        singleLine = true,
        visualTransformation = VisualTransformation.None

    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomTextField() {
    var text by remember { mutableStateOf("") }
    CustomTextField(label = "A", value = text, onValueChange = { text = it }, padding = 40)
}
