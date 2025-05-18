package com.example.devmobile_gym.presentation.screens.UserProfessor.criarTreino

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.data.local.importarExerciciosParaFirestore
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

    val context = LocalContext.current
    var importStatus by remember { mutableStateOf<String?>(null) }

    val search by viewModel.search.collectAsState() // busca por exercicio
    val exercicios by viewModel.exercicioos.collectAsState() // lista de exercicios adicionados no treino
    val exerciciosFiltrados by viewModel.exerciciosFiltrados // exercicios filtrados de acordo com a busca
    // TODO("Armazenar todos os exercicios no banco")

    CustomScreenScaffoldProfessor(
        navController = navController,
        needToGoBack = true,
        onBackClick = { onBack() },
        selectedItemIndex = selectedItemIndex,
        content = { innerModifier ->
            val combinedModifier = innerModifier.padding(1.dp)

            Box(
                modifier = combinedModifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Buscar",
                        fontSize = 30.sp,
                        color = White,
                        modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                    )
                    Spacer(Modifier.height(5.dp))

                    CustomTextField(
                        label = "Título do treino",
                        value = search,
                        onValueChange = viewModel::onTituloChange,
                        padding = 8,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(1.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CustomTextField(
                            label = "Busque máquinas ou exercícios",
                            value = search,
                            onValueChange = viewModel::onSearchChange,
                            padding = 8,
                            modifier = Modifier.weight(1f)
                        )
                        BoxSeta()
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f) // <- Aqui é a mágica!
                    ) {
                        items(exerciciosFiltrados) { exercicio ->
                            CustomExerciseItem(
                                exercise = exercicio.nome,
                                description = exercicio.grupoMuscular
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp)) // Margem entre lista e botão

                    CustomButton(
                        text = "Concluir",
                        onClick = {} ,
//                        metodo para adicionar exercicios no firebase
//                        usei ele por esse botao, vou deixar aqui por enquanto
//                                importarExerciciosParaFirestore(context) { success ->
//                                importStatus = if (success) "Importação concluída com sucesso!"
//                                else "Erro ao importar exercícios."
//                                }

                            modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp) // opcional: deixa o botão com tamanho fixo
                    )
                    importStatus?.let {
                        Text(text = it, color = if (it.contains("Erro")) Color.Red else Color.Green)
                    }
                }
            }


        }
    )
}
