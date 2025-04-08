package com.example.devmobile_gym.ui.theme.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devmobile_gym.R
import com.example.devmobile_gym.ui.theme.components.CustomTextField

@Composable
fun LoginScreen(onNavigateToRegister: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1E1E1E))
    ) {
        Image(
            painter = painterResource(id = R.drawable.print),
            contentDescription = null,
            modifier = Modifier.size(150.dp),
        )
        Text(text = "Acessar sua conta", color = Color.White)
        Text(text = "Insira suas informações e acesse sua conta", color = Color.White)

        CustomTextField("Email", email, { email = it }, padding = 10)
        CustomTextField("Senha", senha, { senha = it }, padding = 10)

        CustomButton("Acessar", onClick = { /* lógica de login */ })
        Text(
            text = "Registrar-se na plataforma",
            color = Color.Blue,
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable(onClick = onNavigateToRegister),
            textDecoration = TextDecoration.Underline
        )

        Text(
            textAlign = TextAlign.Center,
            text = "By clicking continue, you agree to our Terms of Service and Privacy Policy",
            color = Color.White
        )
    }
}
@Composable
fun CustomButton(

    text: String,
    onClick: () -> Unit,
    backgroundColor: Color = Color(0xFF5D98DD)
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(8.dp), // Bordas arredondadas
        modifier = Modifier.fillMaxWidth().height(34.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}