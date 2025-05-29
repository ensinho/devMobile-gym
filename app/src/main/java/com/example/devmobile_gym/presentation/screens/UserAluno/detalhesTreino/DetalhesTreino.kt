package com.example.devmobile_gym.presentation.screens.UserAluno.detalhesTreino

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
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
import com.example.components.ui.theme.components.CustomButton // Verifique o import correto para CustomButton
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.ExerciseCard
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.screens.authScreens.AuthState
import com.example.devmobile_gym.presentation.screens.authScreens.AuthViewModel
import com.example.devmobile_gym.ui.theme.LightGray


@SuppressLint("DefaultLocale")
@Composable
fun DetalhesTreinoScreen(
    navController: NavHostController,
    backStackEntry: NavBackStackEntry,
    onBack: () -> Unit,
) {
    val viewModel: DetalhesTreinoViewModel = viewModel(
        viewModelStoreOwner = backStackEntry,
        factory = DetalhesTreinoViewModel.Factory
    )
    val treino by viewModel.treinoSelecionado.collectAsState() // Use 'by' para desempacotar
    val quantidadeExercicios = treino?.exercicios?.size ?: 0 // Use '?' para acesso seguro
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val horas by viewModel.tempoEmHoras.collectAsState()
    val minutos by viewModel.tempoEmMinutos.collectAsState()

    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.observeAsState()

    // Observe o estado das séries concluídas
    val seriesConcluidas by viewModel.seriesConcluidas.collectAsState()


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

    if (treino == null) { // Verifique se treino é nulo aqui
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1C1C1E)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Treino não encontrado", color = Color.White)
        }
        return
    }

    CustomScreenScaffold(
        navController = navController,
        needToGoBack = true,
        onBackClick = { onBack() },
        selectedItemIndex = selectedItemIndex,
        content = { innerModifier ->
            val combinedModifier = innerModifier.padding(0.5.dp)

            Column(modifier = combinedModifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E1E1E))
                        .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Column (
                        modifier = Modifier.padding(end = 15.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "Duração",
                            fontSize = 15.sp,
                            color = LightGray,
                        )
                        Text(
                            text = String.format("%02dh %02dmin", horas, minutos),
                            fontSize = 13.sp,
                            color = LightGray,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                    Column (
                        modifier = Modifier.padding(end = 15.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "Exercícios",
                            fontSize = 15.sp,
                            color = LightGray,
                        )
                        Text(
                            text = "$quantidadeExercicios",
                            fontSize = 13.sp,
                            color = LightGray,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                    CustomButton(
                        text = "Concluir",
                        onClick = {
                            val tempo = viewModel.finalizarTreino()
                            treino?.let { // Use '?' para acesso seguro
                                navController.navigate("aluno/concluirTreino/${viewModel.getTreinoId()}/${tempo}")
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    treino?.let { currentTreino -> // Use '?' para acesso seguro
                        items(currentTreino.exercicios, key = { it }) { exercicioId -> // Adicione 'key' para otimização do LazyColumn
                            val imagemUrl by produceState<String?>(initialValue = null, key1 = exercicioId) {
                                value = viewModel.getImagemExercicio(exercicioId)
                            }

                            if (!imagemUrl.isNullOrBlank()) {
                                ExerciseCard(
                                    exercicioId = exercicioId, // Passe o ID do exercício
                                    title = viewModel.getNomeExercicio(exercicioId),
                                    url = imagemUrl!!,
                                    // Passe o estado e o callback do ViewModel
                                    seriesConcluidasState = seriesConcluidas,
                                    onSerieCheckedChange = { id, index, checked ->
                                        viewModel.onSerieCheckedChange(id, index, checked)
                                    }
                                )
                            } else {
                                ExerciseCard(
                                    exercicioId = exercicioId, // Passe o ID do exercício
                                    title = viewModel.getNomeExercicio(exercicioId),
                                    url = "",
                                    // Passe o estado e o callback do ViewModel
                                    seriesConcluidasState = seriesConcluidas,
                                    onSerieCheckedChange = { id, index, checked ->
                                        viewModel.onSerieCheckedChange(id, index, checked)
                                    }
                                )
                            }
                        }
                    }
                }
            }

        }
    ) { /* Handle menu click */ }
}