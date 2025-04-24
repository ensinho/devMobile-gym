package com.example.devmobile_gym.presentation.screens.UserProfessor.adicionaEditaAula

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.presentation.components.BoxSeta2
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes


@Composable
fun AdicionaEditaAula(navController: NavHostController, onBack: () -> Unit, viewmodel: AdicionaEditaAulaViewModel = viewModel()) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedItemIndex = when (currentRoute) {
        ProfessorRoutes.Home -> 0
        ProfessorRoutes.Aulas -> 1
        ProfessorRoutes.AdicionarRotina -> 2
        ProfessorRoutes.Chatbot -> 3
        ProfessorRoutes.Gerenciar -> 4
        else -> 0
    }

    val tipoAula by viewmodel.tipoAula
    val horario by viewmodel.horario
    CustomScreenScaffoldProfessor (
        navController = navController,
        onBackClick = { onBack() },
        selectedItemIndex = selectedItemIndex,
        needToGoBack = true,
        content = { innerModifier ->
            val combinedModifier = innerModifier
                .padding(1.dp)
                .fillMaxSize() // ocupa a tela inteira

            Box(
                modifier = combinedModifier,
                contentAlignment = Alignment.Center // centraliza o conteúdo da coluna
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomTextField(
                        label = "Tipo da aula",
                        value = tipoAula,
                        onValueChange = viewmodel::onTipoAulaChange,
                        padding = 10,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    BoxSeta2("Data")
                    Spacer(modifier = Modifier.height(6.dp))
                    BoxSeta2("Professor")
                    Spacer(modifier = Modifier.height(6.dp))
                    BoxSeta2("Alocação max")
                    Spacer(modifier = Modifier.height(6.dp))
                    CustomTextField(
                        label = "Horário",
                        value = horario,
                        onValueChange = viewmodel::onHorarioChange,
                        padding = 10,
                        modifier = Modifier
                    )
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Concluir", fontSize = 12.sp)
                    }
                }
            }
        }
    )
}

