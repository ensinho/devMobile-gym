package com.example.devmobile_gym.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.presentation.components.CustomCalendario
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.CustomCard
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.devmobile_gym.ui.theme.White

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel(), modifier: Modifier = Modifier) {
    val treinos by viewModel.treinos

    CustomScreenScaffold (
        title = "Home",
        onBackClick = { /* Handle back click */ },
        onMenuClick = { /* Handle menu click */ },
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
                        onClick = { /*...*/ }
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
                        description = treino.exercicios.map { it.nome },
                        buttonText = "Iniciar Treino",
                        onButtonClick = { /*...*/ }
                    )
                    Spacer(Modifier.height(8.dp))
                }

                items(100) { item ->
                    Text("Item $item")
                }
            }
        }
    )

}



@Preview
@Composable
private fun HomePreview() {
    HomeScreen()
}