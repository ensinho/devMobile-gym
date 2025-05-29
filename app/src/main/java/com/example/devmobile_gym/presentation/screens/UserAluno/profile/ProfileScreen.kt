package com.example.devmobile_gym.presentation.screens.UserAluno.profile

import StreakBox
import android.R.attr.background
import android.R.id.background
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.components.CardCalendario
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.MonthlyCalendar
import com.example.devmobile_gym.presentation.components.ProfileCard
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.screens.authScreens.AuthState
import com.example.devmobile_gym.presentation.screens.authScreens.AuthViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun profileScrenn(navController: NavHostController, viewModel: ProfileViewModel = viewModel(), onNavigateToHistorico: () -> Unit) {
    val aluno by viewModel.aluno.collectAsState()
    val userWorkouts by viewModel.currentUserWorkouts.collectAsState()
    val nomeUltimoTreino by viewModel.lastTreinoName.collectAsState()
    val imcNum = aluno?.imc

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedItemIndex = when (currentRoute) {
        AlunoRoutes.Home -> 0
        AlunoRoutes.Search -> 1
        AlunoRoutes.QrCode -> 2
        AlunoRoutes.Chatbot -> 3
        AlunoRoutes.Profile -> 4
        else -> 0 // default
    }

    LaunchedEffect(aluno) { // Agora reage a qualquer mudança no objeto 'aluno'
        aluno?.uid?.let { userId ->
            viewModel.loadCurrentUserWorkouts(userId)
        }
    }

    // Este LaunchedEffect agora chama a função atualizada, passando o histórico
    LaunchedEffect(aluno?.historico) {
        viewModel.loadLastTreinoNameFromHistory(aluno?.historico)
        println("Nome do ultimo treino: $nomeUltimoTreino")
    }

    val authViewModel: AuthViewModel = viewModel()

    val authState by authViewModel.authState.observeAsState()

    LaunchedEffect(authState) {
        if (authState == AuthState.Unauthenticated) {
            navController.navigate(AuthRoutes.Login) {
                popUpTo(0)
                launchSingleTop = true
            }
        }
    }

    CustomScreenScaffold(
        navController = navController,
        onBackClick = {},
        selectedItemIndex = selectedItemIndex,
        color = Color(0xFF267FE7),
        content = { innerModifier ->
            val combinedModifier = innerModifier.padding(1.dp)
                .background(Color(0xFF1E1E1E))
            LazyColumn (
                modifier = innerModifier.background(Color(0xFF1E1E1E))


            ){

                item{
                    ProfileCard(
                        name = aluno?.nome ?: "Carregando...",
                        userId = (aluno?.uid?: "Carregando").toString(),
                        weight = aluno?.peso.toString() + " Kg",
                        height = aluno?.altura.toString() + " m"
                    )
                }
                item{
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    Text(
                        text = "Calendário de treinos",
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.DarkGray, // Cor da sombra
                                offset = Offset(1f, 3f), // Posição da sombra (X, Y)
                                blurRadius = 8f // Intensidade do desfoque
                            )
                        ),
                        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        userWorkouts?.let {
                            // Se userWorkouts não é null, exibe o calendário
                            MonthlyCalendar(it.workoutDates)
                        } ?: run {
                            // Se userWorkouts é null, exibe um indicador de carregamento ou mensagem
                            // Você pode adicionar um StateFlow booleano no ViewModel para 'isLoadingUserWorkouts'
                            // para exibir um indicador mais inteligente.
                            Text("Carregando calendário...", color = Color.Gray)
                            // Ou CircularProgressIndicator() se preferir
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }

                item {
                    Text(
                        text = "Informações",
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.DarkGray, // Cor da sombra
                                offset = Offset(1f, 3f), // Posição da sombra (X, Y)
                                blurRadius = 8f // Intensidade do desfoque
                            )
                        ),
                        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                    )
                }
                item {
                    userWorkouts?.currentStreak?.let {
                        StreakBox(
                            text = if (it > 1 || it == 0) "$it Dias seguidos" else "$it Dia seguido",
                            onClick = {},
                            color = if (it >= 1 && it < 30) Color(0xFF267FE7) else if (it >= 30 && it < 50) Color(0xFFFF4500) else if (it >= 50) Color(0xFFAD03DE) else Color.LightGray,
                            iconResId = R.drawable.baseline_local_fire_department_24
                        )
                    }
                }
                item {
                    StreakBox(
                        text = "Ver histórico",
                        onClick = onNavigateToHistorico,
                        iconResId = R.drawable.relogio_ic,
                        color = Color.LightGray
                    )
                }
                item {
                    // Usa o nome do último treino que vem do ViewModel
                    StreakBox(
                        text = "Último treino: $nomeUltimoTreino",
                        onClick = {},
                        iconResId = R.drawable.home_icon,
                        color = Color.LightGray
                    )
                }

                item {
                    imcNum?.let {
                        StreakBox(
                            text = "IMC: " + if (it < 18.5) {
                                "Abaixo do peso"
                            } else if (imcNum >= 18.5 && imcNum < 24.9) {
                                "Peso normal"
                            } else if (imcNum >= 24.9 && imcNum < 29.9) {
                                "Sobrepeso"
                            } else if (imcNum >= 29.9 && imcNum < 34.9) {
                                "Obesidade Grau I"
                            } else if (imcNum > 34.99 && imcNum < 39.9) {
                                "Obesidade Grau II"
                            } else {
                                "Obesidade Grau III (Mórbida)"
                            },
                            onClick = {},
                            iconResId = R.drawable.home_icon,
                            color = Color.LightGray
                        )
                    }
                }

            }

        }
    ) { /* Handle menu click */ }

}
