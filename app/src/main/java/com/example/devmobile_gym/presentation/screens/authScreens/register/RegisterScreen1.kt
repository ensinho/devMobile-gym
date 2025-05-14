package com.example.devmobile_gym.presentation.screens.authScreens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.components.CustomTextField
import java.net.URLEncoder

@Composable
fun RegisterScreen(viewModel: RegisterViewModel = viewModel(), navcontroller: NavHostController) {

    val email by viewModel.email.collectAsState()
    val nome by viewModel.nome.collectAsState()
    val errorMessage = viewModel.errorMessage

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
            text = "Digite o seu email para se registrar." ,
            color = Color.White
        )

        CustomTextField(
            label = "Email@domain.com",
            value = email, // Segurança adicional,
            onValueChange = viewModel::onEmailChange,
            padding = 10,
            modifier = Modifier

        )

        CustomTextField(
            label = "Insira seu nome",
            value = nome,
            onValueChange = viewModel::onNomeChange,
            padding = 10,
            modifier = Modifier

        )

        Spacer(modifier = Modifier.height(8.dp))

        if (!errorMessage.isNullOrEmpty()) {

            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        CustomButton(
            text = "Continuar",
            onClick = {
                viewModel.validarCamposRegistro1(email, nome) {
                    val encodedEmail = URLEncoder.encode(email, "UTF-8")
                    navcontroller.navigate("register2/$encodedEmail/$nome")
                }
            }

        )
        Text(
            textAlign = TextAlign.Center,
            text = " By clicking continue, you agree to our Terms of service and Privacy Policy" ,
            color = Color.White
        )
    }




}
