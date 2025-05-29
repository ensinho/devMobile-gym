package com.example.devmobile_gym.presentation.screens.UserProfessor.adicionaMaquinaExercicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.components.BoxSeta
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import com.example.devmobile_gym.presentation.screens.authScreens.AuthState
import com.example.devmobile_gym.presentation.screens.authScreens.AuthViewModel

// ao concluir o treino, não leva info de uma tela pra outra
// futuramente, o view model desse componente vai chamar uma função que adiciona o treino finalizado
// na lista de treinos finalizados do histórico do aluno.
@Composable
fun AdicionaMaquinaScreen(navController: NavHostController, onBack: () -> Unit, viewmodel: adicionaMaquinaViewModel = viewModel(), onConclude: () -> Unit ) {
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

    val nomeMaquina by viewmodel.nomeMaquina
    val numMaquina by viewmodel.numMaquina
    val exerciciosPossiveis by viewmodel.ExerciciosPossiveis
    val isIncluded by viewmodel.isIncluded

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

    CustomScreenScaffoldProfessor (
        needToGoBack = true,
        onBackClick = { onBack() },
        navController = navController,
        selectedItemIndex = selectedItemIndex
    ) { innerModifier ->
        val combinedModifier = innerModifier.padding(1.dp)
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = combinedModifier.fillMaxSize().verticalScroll(rememberScrollState())
        ){
            CustomTextField(
                label = "Nome da Máquinas",
                value = nomeMaquina,
                onValueChange = viewmodel::onNomeMaquinaChange,
                padding = 10,
                modifier = Modifier
            )
            CustomTextField(
                label = "Número da Máquina no CT",
                value = numMaquina,
                onValueChange = viewmodel::onNumMaquinaChange,
                padding = 10,
                modifier = Modifier
            )
            BoxSeta()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                CustomTextField(
                    label = "Exercícios possíveis na máquina",
                    value = exerciciosPossiveis,
                    onValueChange = viewmodel::onExerciciosPossiveisChange,
                    padding = 10,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = viewmodel::onIsIncludedChange,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = if (isIncluded) painterResource(id = R.drawable.add_circle_item) else painterResource(id = R.drawable.remove_item),
                        contentDescription = if (isIncluded) "Adicionar" else "Remover",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }



            Spacer(modifier = Modifier.height(32.dp)) // Se quiser espaço antes do botão

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // margem lateral opcional
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomButton(
                    text = "Concluir",
                    onClick = { onConclude() }
                )
            }

        }
    }
}