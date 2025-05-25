package com.example.devmobile_gym.presentation.screens.UserAluno.searchScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.presentation.components.BoxSeta
import com.example.devmobile_gym.presentation.components.CustomExerciseSearchCard
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.ui.theme.White

@Composable
fun SearchScreen(navController: NavHostController, viewModel: SearchScreenViewModel = viewModel()) {
    val search by viewModel.search.collectAsState() // busca por exercicio
    val exerciciosFiltrados by viewModel.exerciciosFiltrados // exercicios filtrados de acordo com a busca

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
        onBackClick = { /*TODO*/ },
        selectedItemIndex = selectedItemIndex,
        content = { innerModifier ->

            val combinedModifier = innerModifier.padding(0.5.dp)

            Column (
                modifier = combinedModifier.fillMaxSize()
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                ){
                    Text(
                        text = "Buscar",
                        fontSize = 30.sp,
                        color = White
                    )
                }
                Spacer(Modifier.height(5.dp))
                Row {
                    CustomTextField(
                        label = "Busque exercícios",
                        value = search,
                        onValueChange = viewModel::onSearchChange,
                        padding = 10,
                        modifier = Modifier
                    )
                }
                LazyColumn {
                    for (exercicio in exerciciosFiltrados) {
                        item {
                            CustomExerciseSearchCard(
                                exercicio = exercicio.nome,
                                description = "",
                                url = exercicio.imagem
                            )
                            Spacer(Modifier.height(15.dp))
                        }
                    }
                }
            }
        }
    ) { /* Handle menu click */ }
}