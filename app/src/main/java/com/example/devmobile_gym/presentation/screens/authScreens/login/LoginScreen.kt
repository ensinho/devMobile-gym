package com.example.devmobile_gym.presentation.screens.authScreens.login

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.components.CustomPasswordTextField
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import com.example.devmobile_gym.presentation.screens.authScreens.AuthState
import com.example.devmobile_gym.presentation.screens.authScreens.AuthViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel(), authViewModel: AuthViewModel = viewModel(), navController: NavHostController) {
    val email by viewModel.email.collectAsState()
    val senha by viewModel.senha.collectAsState()
    val errorMessage = viewModel.errorMessage

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authViewModel.isLoginComplete) {
        when {
            authViewModel.isLoginComplete -> {
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

//    LaunchedEffect(authState.value) {
//        val state = authState.value
//        if (state is AuthState.Error) {
//            Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
//        }
//    }

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

        CustomTextField(label = "Email", value = email, onValueChange = viewModel::onEmailChange, padding = 10, modifier = Modifier)
        CustomPasswordTextField(label = "Senha", value = senha, onValueChange = viewModel::onSenhaChange, padding = 10, modifier = Modifier)


        if (authState.value is AuthState.Error) {
            val errorMessage = (authState.value as AuthState.Error).message
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }


        Spacer(modifier = Modifier.height(5.dp))

        CustomButton("Acessar", onClick = {
            authViewModel.login(email, senha)
        })
        Text(
            text = "Registrar-se na plataforma",
            color = Color(0xFF519bed),
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable(onClick = { navController.navigate(AuthRoutes.Register) }),
            textDecoration = TextDecoration.Underline
        )

        Text(
            textAlign = TextAlign.Center,
            text = "Ao clicar em continuar, você concorda com nossos Termos de Serviço e Política de Privacidade.",
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


