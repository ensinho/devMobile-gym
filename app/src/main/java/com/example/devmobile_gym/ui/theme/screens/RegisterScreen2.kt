package com.example.devmobile_gym.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devmobile_gym.R

@Composable
fun RegisterScreen2(onBack: ()-> Unit) {
    Column(

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1E1E1E))

    ) {
        Image(

            painter = painterResource(id = R.drawable.print),
            contentDescription = "Descrição da imagem",
            modifier = Modifier.size(150.dp),
        )
        Text(
            text = "Crie sua conta",
            color = Color.White,
            fontSize = 20.sp
        )
        Text(
            text = "Digite o sua senha para se registrar" ,
            color = Color.White
        )
        var text1 by remember { mutableStateOf("") }
        com.example.devmobile_gym.ui.theme.components.CustomTextField(
            label = "Senha",
            value = text1,
            onValueChange = { text1 = it },
            padding = 10

        )
        var text2 by remember { mutableStateOf("") }
        com.example.devmobile_gym.ui.theme.components.CustomTextField(
            label = "Confirme sua senha",
            value = text2,
            onValueChange = { text2 = it },
            padding = 10

        )
        com.example.components.ui.theme.components.CustomButton(
            text = "Registrar",
            onClick = { onBack

            }

        )
        Text(
            textAlign = TextAlign.Center,
            text = " By clicking continue, you agree to our Terms of service and Privacy Policy" ,
            color = Color.White
        )

    }




}