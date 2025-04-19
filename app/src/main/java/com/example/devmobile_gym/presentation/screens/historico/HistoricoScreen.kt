package com.example.devmobile_gym.presentation.screens.historico

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.presentation.components.CustomCalendario
import com.example.devmobile_gym.presentation.components.CustomCard
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.ui.theme.White

@Composable
fun HistoricoScreen(navController: NavHostController, onBack: () -> Unit, viewModel: HistoricoScreenViewModel = viewModel()) {

    val treinos by viewModel.treinos

    CustomScreenScaffold(
        navController = navController,
        title = "Histórico",
        onBackClick = { onBack() },
        onMenuClick = { /* Handle menu click */ },
        needToGoBack = true,
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
                        onButtonClick = { /* nao precisa de botao */ }
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    )
}