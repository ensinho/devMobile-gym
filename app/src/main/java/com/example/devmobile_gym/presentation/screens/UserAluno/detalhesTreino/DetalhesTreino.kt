package com.example.devmobile_gym.presentation.screens.UserAluno.detalhesTreino

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes


@Composable
fun DetalhesTreinoScreen(navController: NavHostController, backStackEntry: NavBackStackEntry, onBack: () -> Unit, onConclude: (Int) -> Unit
) {
    val viewModel: DetalhesTreinoViewModel = viewModel(
        viewModelStoreOwner = backStackEntry,
        factory = DetalhesTreinoViewModel.Factory
    )
    val treino = viewModel.treinoSelecionado
    val quantidadeExercicios = treino?.exercicios?.size ?: 0
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
        navController = navController,
        title = treino.nome,
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
                        modifier = Modifier.padding(end = 15.dp)
                    ){
                        Text(
                            text = "Duração",
                            fontSize = 13.sp,
                            color = LightGray,
                        )
                        Text(
                            text = "1h 30min",
                            fontSize = 15.sp,
                            color = LightGray,
                        )
                    }

                    Column (
                        modifier = Modifier.padding(end = 15.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "Exercícios",
                            fontSize = 13.sp,
                            color = LightGray,
                        )
                        Text(
                            text = "$quantidadeExercicios",
                            fontSize = 15.sp,
                            color = LightGray,
                        )
                    }

                    CustomButton(
                        text = "Concluir",
                        onClick = { onConclude(treino.id) }
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    items(treino.exercicios) { exercicio ->
                        ExerciseCard(
                            title = exercicio.nome,
                            peso = exercicio.peso
                        )
                    }
                }
            }

        }
    )
}
