package com.example.devmobile_gym.presentation.components

import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun BotaoAbrirAcessibilidade() {
    val context = LocalContext.current

    Button(
        onClick = {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF5D98DD),
            disabledContainerColor = Color.Gray // cor quando desabilitado
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text("Abrir Acessibilidade", fontWeight = FontWeight.Bold)
    }
}
