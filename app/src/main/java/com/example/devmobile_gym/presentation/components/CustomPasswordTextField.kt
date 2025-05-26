package com.example.devmobile_gym.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomPasswordTextField(
    modifier: Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    padding: Int,
    isPassword: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(padding.dp),
        singleLine = true,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (isPassword) {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.baseline_visibility_24)
                else
                    painterResource(id = R.drawable.baseline_visibility_off_24)

                val color = if (passwordVisible)
                    Color(0xFF5D98DD)
                else
                    Color.Gray

                val description = if (passwordVisible) "Ocultar senha" else "Mostrar senha"

                IconButton( onClick = { passwordVisible = !passwordVisible } ) {
                    Icon(
                        painter = image,
                        contentDescription = description,
                        tint = color,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color(0xFF1E1E1E),
            unfocusedContainerColor = Color(0xFF1E1E1E),
        )
    )
}

