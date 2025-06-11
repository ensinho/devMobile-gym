// presentation/screens/UserAluno/searchScreen/SearchScreen.kt
package com.example.devmobile_gym.presentation.screens.UserAluno.searchScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.presentation.components.CustomExerciseSearchCard
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.screens.authScreens.AuthState
import com.example.devmobile_gym.presentation.screens.authScreens.AuthViewModel
import com.example.devmobile_gym.ui.theme.White

@Composable
fun SearchScreen(navController: NavHostController, viewModel: SearchScreenViewModel = viewModel()) {
    val search by viewModel.search.collectAsState()
    val exerciciosFiltrados by viewModel.exerciciosFiltrados.collectAsState()

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

    val selectedItemIndex = when (currentRoute) {
        AlunoRoutes.Home -> 0
        AlunoRoutes.Search -> 1
        AlunoRoutes.QrCode -> 2
        AlunoRoutes.Chatbot -> 3
        AlunoRoutes.Profile -> 4
        else -> 0
    }

    CustomScreenScaffold(
        navController = navController,
        onBackClick = { /*TODO: Implementar lógica de volta*/ },
        selectedItemIndex = selectedItemIndex,
        content = { innerModifier ->

            val combinedModifier = innerModifier.padding(0.5.dp)

            Column (
                modifier = combinedModifier
                    .fillMaxSize()
                    .background(Color(0xFF1E1E1E))
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
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(exerciciosFiltrados, key = { it.id ?: it.nome.hashCode() }) { exercicio ->
                        CustomExerciseSearchCard(
                            exercicio = exercicio.nome.toString(),
                            description = exercicio.descricao.toString(),
                            photoBitmap = exercicio.photoBitmap // APENAS O BITMAP
                        )
                        Spacer(Modifier.height(15.dp))
                    }
                }
            }
        }
    ) { /* Handle menu click */ }
}