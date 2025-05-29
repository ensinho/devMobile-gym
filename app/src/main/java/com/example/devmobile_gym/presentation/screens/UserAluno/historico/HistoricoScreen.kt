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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.screens.authScreens.AuthState
import com.example.devmobile_gym.presentation.screens.authScreens.AuthViewModel
// Remova SimpleDateFormat e Locale daqui, pois a data já será formatada no ViewModel

@Composable
fun HistoricoScreen(navController: NavHostController, onBack: () -> Unit, viewModel: HistoricoScreenViewModel = viewModel()) {

    // Coleta a nova lista de treinos com detalhes prontos para a UI
    val treinosHistoricoUi by viewModel.treinosHistoricoUi.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.observeAsState()

    LaunchedEffect(authState) {
        if (authState == AuthState.Unauthenticated) {
            navController.navigate(AuthRoutes.Login) {
                popUpTo(0)
                launchSingleTop = true
            }
        }
    }

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

                // Agora, iteramos sobre a lista de objetos TreinoHistoricoUi que já contêm tudo pronto
                items(treinosHistoricoUi) { treinoHistoricoUi ->
                    CustomCard(
                        treino = treinoHistoricoUi.nomeTreino, // Nome do treino já carregado
                        description = treinoHistoricoUi.nomesExercicios, // Nomes dos exercícios já carregados
                        buttonText = "Iniciar Treino",
                        needButton = false,
                        onButtonClick = { /* nao precisa de botao */ },
                        editButton = {},
                        deleteButton = {},
                        data = treinoHistoricoUi.dataRealizacao // Data já formatada
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    ) { /* Handle menu click */ }
}