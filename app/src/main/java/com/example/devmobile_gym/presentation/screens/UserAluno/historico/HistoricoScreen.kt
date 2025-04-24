package com.example.devmobile_gym.presentation.screens.UserAluno.historico

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.presentation.components.CustomCard
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes

@Composable
fun HistoricoScreen(navController: NavHostController, onBack: () -> Unit, viewModel: HistoricoScreenViewModel = viewModel()) {

    val treinos by viewModel.treinos
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

                items(treinos) { treino ->
                    CustomCard(
                        treino = treino.nome,
                        description = treino.exercicios.map { it.nome },
                        buttonText = "Iniciar Treino",
                        needButton = false,
                        onButtonClick = { /* nao precisa de botao */ },
                        editButton = {}
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    ) { /* Handle menu click */ }
}