package com.example.devmobile_gym.presentation.screens.concluiTreino

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.screens.detalhesTreino.ConcluiTreinoViewModel
import com.example.devmobile_gym.presentation.screens.detalhesTreino.DetalhesTreinoViewModel
import com.example.devmobile_gym.ui.theme.LightGray
import com.example.devmobile_gym.ui.theme.White

// ao concluir o treino, não leva info de uma tela pra outra
// futuramente, o view model desse componente vai chamar uma função que adiciona o treino finalizado
// na lista de treinos finalizados do histórico do aluno.
@Composable
fun ConcluiTreino(navController: NavHostController, backStackEntry: NavBackStackEntry, onBack: () -> Unit, onConclude: () -> Unit
) {
    val viewModel: ConcluiTreinoViewModel = viewModel(
        viewModelStoreOwner = backStackEntry,
        factory = ConcluiTreinoViewModel.Factory
    )
    val treinoFinalizado = viewModel.treinoSelecionado

    if (treinoFinalizado == null) {
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

    val quantidadeExercicios = treinoFinalizado.exercicios?.size ?: 0

    CustomScreenScaffold(
        title = "Concluir Treino",
        needToGoBack = true,
        onMenuClick = { /* Handle menu click */ },
        onBackClick = { onBack() },
        navController = navController
    ) { innerModifier ->
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(0.3f)
            ){
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center

                ) {
                    Text(
                        text = "$quantidadeExercicios",
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
                        text = "1h 20min",
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
//            Spacer(Modifier.fillMaxSize(0.3f))

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(0.09f)) {

                CustomButton(
                    text = "Concluir Treino",
                    onClick = { onConclude() }
                )
            }
        }
    }
}

