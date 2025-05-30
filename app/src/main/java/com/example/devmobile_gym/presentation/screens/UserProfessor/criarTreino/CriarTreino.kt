package com.example.devmobile_gym.presentation.screens.UserProfessor.criarTreino

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.presentation.components.CustomExerciseItem
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.components.BoxSeta
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import com.example.devmobile_gym.ui.theme.White

@Composable
fun CriarTreinoScreen(navController: NavHostController, backStackEntry: NavBackStackEntry, onBack: () -> Unit) {
    val viewModel: CriarTreinoViewModel = viewModel(
        viewModelStoreOwner = backStackEntry,
        factory = CriarTreinoViewModel.Factory
    )

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

    val search by viewModel.search
    val exerciciosFiltrados by viewModel.exerciciosFiltrados

    CustomScreenScaffoldProfessor(
        navController = navController,
        needToGoBack = true,
        onBackClick = { onBack() },
        selectedItemIndex = selectedItemIndex,
        content = { innerModifier ->
            val combinedModifier = innerModifier.padding(16.dp)

            Column(
                modifier = combinedModifier.fillMaxSize()
            ) {
                Text(
                    text = "Buscar",
                    fontSize = 30.sp,
                    color = White
                )
                Spacer(Modifier.height(5.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomTextField(
                        label = "Busque máquinas ou exercícios",
                        value = search,
                        onValueChange = viewModel::onSearchChange,
                        padding = 10,
                        modifier = Modifier.weight(1f)
                    )
                    BoxSeta()
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(exerciciosFiltrados) { exercicio ->
                        CustomExerciseItem(
                            exercise = exercicio.nome,
                            description = exercicio.grupoMuscular
                        )
                    }

                }
            }
        }
    )
}
