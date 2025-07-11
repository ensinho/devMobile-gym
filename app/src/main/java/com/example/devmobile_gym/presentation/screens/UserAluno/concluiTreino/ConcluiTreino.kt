package com.example.devmobile_gym.presentation.screens.UserAluno.concluiTreino

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.screens.UserAluno.StreakViewModel
import com.example.devmobile_gym.presentation.screens.detalhesTreino.ConcluiTreinoViewModel
import com.example.devmobile_gym.ui.theme.White
import androidx.compose.runtime.rememberCoroutineScope // Importe isso
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.screens.authScreens.AuthState
import com.example.devmobile_gym.presentation.screens.authScreens.AuthViewModel
import kotlinx.coroutines.launch // Importe isso

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConcluiTreino(
    navController: NavHostController,
    backStackEntry: NavBackStackEntry,
    onBack: () -> Unit
) {
    val viewModel: ConcluiTreinoViewModel = viewModel(
        viewModelStoreOwner = backStackEntry,
        factory = ConcluiTreinoViewModel.Factory
    )
    val streakViewModel: StreakViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope() // Cria um CoroutineScope aqui

    val tempoTreino by viewModel.tempo.collectAsState()
    val nomeTreino by viewModel.nomeTreino.collectAsState()
    val quantidadeExercicios by viewModel.quantidadeExercicios.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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

    val selectedItemIndex = when (currentRoute) {
        AlunoRoutes.Home -> 0
        AlunoRoutes.Search -> 1
        AlunoRoutes.QrCode -> 2
        AlunoRoutes.Chatbot -> 3
        AlunoRoutes.Profile -> 4
        else -> 0 // default
    }

    CustomScreenScaffold(
        navController = navController,
        needToGoBack = true,
        onBackClick = { onBack() },
        selectedItemIndex = selectedItemIndex,
        content = {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = it.fillMaxSize() // Usa o modifier passado pelo scaffold
            ){
                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(0.5f)
                ){
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = " Você concluiu o ",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = nomeTreino,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D98DD)
                        )
                    }
                }
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(0.4f)
                ){
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = quantidadeExercicios,
                            fontSize = 30.sp,
                            color = White
                        )
                        Text(
                            text = "Exercícios",
                            fontSize = 15.sp,
                            color = White
                        )
                    }
                    Spacer(modifier = Modifier.width(50.dp))

                    Column {
                        Text(
                            text = tempoTreino,
                            fontSize = 30.sp,
                            color = White
                        )
                        Text(
                            text = "De Duração.",
                            fontSize = 15.sp,
                            color = White
                        )
                    }
                }

                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(0.4f)) {

                    CustomButton(
                        text = if (isLoading) "Salvando..." else "Concluir Treino",
                        enabled = !isLoading,
                        onClick = {
                            coroutineScope.launch {
                                // Obtendo o UserWorkouts ANTES de chamar addToHistory
                                val currentUserWorkouts = viewModel.getCurrentUserWorkouts()

                                // Chamando addToHistory
                                viewModel.addToHistory(
                                    onSuccess = {
                                        // O sucesso do addToHistory não impede que o streak seja atualizado
                                        if (currentUserWorkouts != null) {
                                            streakViewModel.updateStreak(currentUserWorkouts)
                                        } else {
                                            android.util.Log.e("ConcluiTreino", "Falha ao obter UserWorkouts para atualização de streak.")
                                        }
                                        navController.navigate(AlunoRoutes.Home)
                                    }
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { /* Handle menu click, se houver */ }
}