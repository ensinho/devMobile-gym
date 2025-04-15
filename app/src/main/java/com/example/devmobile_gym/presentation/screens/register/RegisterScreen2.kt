package com.example.devmobile_gym.presentation.screens.register

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.screens.login.CustomButton

@Composable
fun RegisterScreen2(viewModel: RegisterViewModel = viewModel(),  onBack: ()-> Unit) {
    var senha = viewModel.senha.value
    var confirmarSenha = viewModel.confirmSenha.value
    var errorMessage = viewModel.errorMessage

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

        CustomTextField(
            label = "Senha",
            value = senha,
            onValueChange = viewModel::onSenhaChange,
            padding = 10
        )

        CustomTextField(
            label = "Confirme sua senha",
            value = confirmarSenha,
            onValueChange = viewModel::onConfirmaSenhaChange,
            padding = 10
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

        CustomButton("Acessar", onClick = {
            viewModel.registrar {
                // lambda para navegar para a próxima tela
                onBack()
            }
        })

        Text(
            textAlign = TextAlign.Center,
            text = " By clicking continue, you agree to our Terms of service and Privacy Policy" ,
            color = Color.White
        )

    }




}