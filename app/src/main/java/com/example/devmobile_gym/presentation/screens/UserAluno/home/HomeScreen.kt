package com.example.devmobile_gym.presentation.screens.UserAluno.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.presentation.components.CustomCalendario
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.CustomCard
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.ui.theme.White

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = viewModel(), onNavigateToTreino: (String) -> Unit, onNavigateToAulas: () -> Unit) {
    val treinos by viewModel.treinos.collectAsState()

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

    LaunchedEffect(treinos) {
        viewModel.loadData()
    }


    CustomScreenScaffold (
        navController = navController,
        onBackClick = { /* Handle back click */ },
        selectedItemIndex = selectedItemIndex,
        content = { innerModifier ->
            val combinedModifier = innerModifier.padding(1.dp)

            LazyColumn(
                modifier = combinedModifier
            ) {
                item {
                    CustomCalendario()
                    Spacer(Modifier.height(16.dp))
                    CustomButton(
                        text = "Aulas & Funcionais",
                        onClick = { onNavigateToAulas() }
                    )
                    Box(
                    ){
                        Divider(color = White, thickness = 0.8.dp, modifier =  Modifier.fillMaxWidth().padding(horizontal = 12.dp))
                    }
                    Spacer(Modifier.height(24.dp))

                    Text(
                        "Rotinas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                }

                items(treinos) { treino ->
                    CustomCard(
                        treino = treino.nome,
                        description = treino.exercicios.map {
                            viewModel.getNomeExercicio(it)
                        },
                        buttonText = "Iniciar Treino",
                        onButtonClick = { onNavigateToTreino(treino.id) },
                        editButton = {},
                        deleteButton = {}
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    ) { /* Handle menu click */ }

}
