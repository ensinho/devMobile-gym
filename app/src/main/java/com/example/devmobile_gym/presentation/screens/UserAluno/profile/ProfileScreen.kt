package com.example.devmobile_gym.presentation.screens.UserAluno.profile

import StreakBox
import android.content.Context // Importe Context
import android.graphics.Bitmap // Importe Bitmap
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator // Importe CircularProgressIndicator
import androidx.compose.material3.MaterialTheme // Importe MaterialTheme para cores de erro
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
import androidx.compose.ui.platform.LocalContext // Importe LocalContext para obter o Context
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.MonthlyCalendar
import com.example.devmobile_gym.presentation.components.ProfileCard // Ajuste ProfileCard para Bitmap
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

    // NOVO: Coleta o Bitmap da foto de perfil do ViewModel
    val profilePictureBitmap by viewModel.profilePictureBitmap.collectAsState()
    // NOVO: Coleta o estado da operação da foto de perfil (loading, success, error)
    val profilePictureState by viewModel.profilePictureState.collectAsState()

    val context = LocalContext.current // Obtenha o Context aqui para passar para o ViewModel

    // Launcher para selecionar a imagem da galeria
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            // Se uma URI foi selecionada, inicie o upload da imagem para o Firestore via Base64
            viewModel.uploadProfilePicture(context, uri)
        }
    }

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

    // Carregar userWorkouts e último nome do treino ao iniciar ou quando aluno mudar
    LaunchedEffect(aluno) {
        aluno?.uid?.let { userId ->
            viewModel.loadCurrentUserWorkouts(userId)
        }
        viewModel.loadLastTreinoNameFromHistory(aluno?.historico) // Chama com o histórico do aluno
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
        onBackClick = { /* Implementar lógica de volta se necessário */ },
        selectedItemIndex = selectedItemIndex,
        color = Color(0xFF267FE7),
        content = { innerModifier ->
            LazyColumn(
                modifier = innerModifier
                    .fillMaxSize()
                    .background(Color(0xFF1E1E1E)) // Cor de fundo para toda a LazyColumn
            ) {
                item {
                    // Passar o Bitmap da foto de perfil para o ProfileCard
                    ProfileCard(
                        name = aluno?.nome ?: "Carregando...",
                        userId = (aluno?.uid?: "Carregando").toString(),
                        weight = aluno?.peso.toString() + " Kg",
                        height = aluno?.altura.toString() + " m",
                        onEditProfileClick = {
                            // Esta é a ação do botão de lápis
                            pickImageLauncher.launch("image/*") // Inicia o seletor de imagens
                        },
                        profileImageBitmap = profilePictureBitmap, // PASSA O BITMAP AQUI
                        isLoading = profilePictureState is ProfilePictureState.Loading // Indica estado de carregamento
                    )
                }
                // Exibe mensagens de erro ou sucesso do upload/carregamento da foto de perfil
                item {
                    when (profilePictureState) {
                        is ProfilePictureState.Error -> {
                            Text(
                                text = (profilePictureState as ProfilePictureState.Error).message,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        is ProfilePictureState.Success -> {
                            // Opcional: Mostrar uma mensagem de sucesso temporária
                            // Text(text = "Foto atualizada com sucesso!", color = Color.Green)
                        }
                        else -> { /* Não faz nada para Idle ou Loading */ }
                    }
                }
                item {
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
                                color = Color.DarkGray,
                                offset = Offset(1f, 3f),
                                blurRadius = 8f
                            )
                        ),
                        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        userWorkouts?.let {
                            MonthlyCalendar(it.workoutDates)
                        } ?: run {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(vertical = 16.dp)
                            )
                            // Ou Text("Carregando calendário...", color = Color.Gray)
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
                                color = Color.DarkGray,
                                offset = Offset(1f, 3f),
                                blurRadius = 8f
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
                    StreakBox(
                        text = "Último treino: $nomeUltimoTreino",
                        onClick = {},
                        iconResId = R.drawable.home_icon,
                        color = Color.LightGray
                    )
                }

                item {
                    imcNum?.let {
                        val imcCategory = when {
                            it < 18.5 -> "Abaixo do peso"
                            it < 24.9 -> "Peso normal"
                            it < 29.9 -> "Sobrepeso"
                            it < 34.9 -> "Obesidade Grau I"
                            it < 39.9 -> "Obesidade Grau II"
                            else -> "Obesidade Grau III (Mórbida)"
                        }
                        StreakBox(
                            text = "IMC: $imcCategory",
                            onClick = {},
                            iconResId = R.drawable.home_icon, // Altere para um ícone mais relevante para IMC
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
    ) { /* Conteúdo da BottomNavigation, se houver */ }
}