package com.example.devmobile_gym.presentation.screens.UserAluno.historico

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.presentation.components.CustomCard
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HistoricoScreen(navController: NavHostController, onBack: () -> Unit, viewModel: HistoricoScreenViewModel = viewModel()) {

    val treinosComData by viewModel.treinos.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedItemIndex = when {
        currentRoute?.startsWith(AlunoRoutes.Home) == true -> 0
        currentRoute?.startsWith(AlunoRoutes.Search) == true -> 1
        currentRoute?.startsWith(AlunoRoutes.QrCode) == true -> 2
        currentRoute?.startsWith(AlunoRoutes.Chatbot) == true -> 3
        currentRoute?.startsWith(AlunoRoutes.Profile) == true ||
                currentRoute?.startsWith(AlunoRoutes.Historico) == true -> 4
        else -> 0
    }

    CustomScreenScaffold(
        navController = navController,
        needToGoBack = true,
        onBackClick = { onBack() },
        selectedItemIndex = selectedItemIndex,
        content = { innerModifier ->
            val combinedModifier = innerModifier.padding(1.dp)

            LazyColumn(
                modifier = combinedModifier
            ) {
                item {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(
                            "Histórico de Treinos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 20.dp)
                        )
                    }
                }

                items(treinosComData) { treinoComData ->
                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val dataFormatada = formatter.format(treinoComData.dataRealizacao)
                    CustomCard(
                        treino = treinoComData.treino.nome,
                        description = treinoComData.treino.exercicios.map { exercicio ->
                            viewModel.getNomeExercicio(exercicio)
                        },
                        buttonText = "Iniciar Treino",
                        needButton = false,
                        onButtonClick = { /* nao precisa de botao */ },
                        editButton = {},
                        deleteButton = {},
                        data = dataFormatada
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    ) { /* Handle menu click */ }
}