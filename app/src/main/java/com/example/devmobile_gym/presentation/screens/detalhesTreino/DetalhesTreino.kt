package com.example.devmobile_gym.presentation.screens.detalhesTreino

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.ExerciseCard
import com.example.devmobile_gym.ui.theme.LightGray
import androidx.navigation.NavBackStackEntry


@Composable
fun DetalhesTreinoScreen(backStackEntry: NavBackStackEntry, onBack: () -> Unit) {
    val viewModel: DetalhesTreinoViewModel = viewModel(
        viewModelStoreOwner = backStackEntry,
        factory = DetalhesTreinoViewModel.Factory
    )
    val treino = viewModel.treinoSelecionado

    if (treino == null) {
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
        title = treino.nome,
        needToGoBack = true,
        onMenuClick = { /* Handle menu click */ },
        onBackClick = { onBack() },
        content = { innerModifier ->
            val combinedModifier = innerModifier.padding(1.dp)

            LazyColumn(
                modifier = combinedModifier
            ) {
                items(treino.exercicios) { exercicio ->
                    ExerciseCard(
                        title = exercicio.nome,
                        peso = exercicio.peso
                    )
                }
            }
        }
    )
}
