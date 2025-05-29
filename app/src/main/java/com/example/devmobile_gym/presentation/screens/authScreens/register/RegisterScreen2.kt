package com.example.devmobile_gym.presentation.screens.authScreens.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.devmobile_gym.R
import com.example.devmobile_gym.domain.model.Usuario
import com.example.devmobile_gym.presentation.components.CustomPasswordTextField
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import com.example.devmobile_gym.presentation.screens.authScreens.AuthState
import com.example.devmobile_gym.presentation.screens.authScreens.AuthViewModel
import com.example.devmobile_gym.presentation.screens.authScreens.login.CustomButton

@Composable
fun RegisterScreen2(
    email: String,
    nome: String,
    authViewModel: AuthViewModel = viewModel(),
    registerViewModel: RegisterViewModel = viewModel(),
    navController: NavHostController
) {

    // Preenche o email no ViewModel
    LaunchedEffect(Unit) {
        registerViewModel.onEmailChange(email)
    }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    val peso by registerViewModel.peso.collectAsState()
    val altura by registerViewModel.altura.collectAsState()
    val senha by registerViewModel.senha.collectAsState()
    val confirmarSenha by registerViewModel.confirmSenha.collectAsState()
    val errorMessage = registerViewModel.errorMessage


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

        CustomPasswordTextField(
            label = "Senha",
            value = senha, // Segurança adicional,
            onValueChange = registerViewModel::onSenhaChange,
            padding = 10,
            modifier = Modifier
        )

        CustomPasswordTextField(
            label = "Confirme sua senha",
            value = confirmarSenha, // Segurança adicional,
            onValueChange = registerViewModel::onConfirmaSenhaChange,
            padding = 10,
            modifier = Modifier
        )

        CustomTextField(
            label = "Peso",
            value = peso, // Segurança adicional,
            onValueChange = registerViewModel::onPesoChange,
            padding = 10,
            modifier = Modifier
        )

        CustomTextField(
            label = "Altura",
            value = altura, // Segurança adicional,
            onValueChange = registerViewModel::onAlturaChange,
            padding = 10,
            modifier = Modifier
        )

        if (authState.value is AuthState.Error) {
            val errorMessage = (authState.value as AuthState.Error).message
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

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
            authViewModel.signup(email = email, nome = nome, senha = registerViewModel.senha.value, confirmarSenha = registerViewModel.confirmSenha.value, peso = peso, altura = altura)

        })

        // Controle de estado
        // trocar para acessar a tela de login ao inves da home
        LaunchedEffect(authViewModel.isRegistrationComplete) {
            when {
                authViewModel.isRegistrationComplete -> {
                    val route = if (authViewModel.isCurrentUserProfessor()) {
                        ProfessorRoutes.Home
                    } else {
                        AlunoRoutes.Home
                    }
                    navController.navigate(route) {
                        popUpTo(AuthRoutes.Register) { inclusive = true }
                    }
                }
                authState.value is AuthState.Error -> {
                    Toast.makeText(context,
                        (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
                }
            }
        }





        Text(
            textAlign = TextAlign.Center,
            text = " By clicking continue, you agree to our Terms of service and Privacy Policy" ,
            color = Color.White
        )

    }




}