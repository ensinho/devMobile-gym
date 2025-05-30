package com.example.devmobile_gym.presentation.screens.UserProfessor.editarTreino


import android.widget.Toast
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.presentation.components.BoxSeta
import com.example.devmobile_gym.presentation.components.CustomExerciseItem
import com.example.devmobile_gym.presentation.components.CustomExerciseItemAlreadyAdd
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import com.example.devmobile_gym.presentation.screens.UserProfessor.criarTreino.CriarTreinoViewModel
import com.example.devmobile_gym.presentation.screens.authScreens.AuthState
import com.example.devmobile_gym.presentation.screens.authScreens.AuthViewModel
import com.example.devmobile_gym.ui.theme.White
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.logging.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EditarTreinoScreen(
    navController: NavHostController,
    backStackEntry: NavBackStackEntry,
    onBack: () -> Unit){

    val viewModel: EditarTreinoViewModel = viewModel(
        viewModelStoreOwner = backStackEntry,
        factory = EditarTreinoViewModel.Factory
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

    val search by viewModel.search.collectAsState()
    val titulo by viewModel.titulo.collectAsState()
    val error = viewModel.status.collectAsState()
    val exerciciosFiltrados by viewModel.exerciciosFiltrados
    val exerciciosAdicionados by viewModel.exerciciosAdicionados.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

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
                    text = "Editar Treino",
                    fontSize = 30.sp,
                    color = White
                )
                Spacer(Modifier.height(5.dp))


                CustomTextField(
                    label = "Título do treino",
                    value = titulo,
                    onValueChange = viewModel::onTituloChange,
                    padding = 8,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(1.dp))

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(exerciciosFiltrados) { exercicio ->
                        CustomExerciseItem(
                            exercise = exercicio.nome ?: "Sem nome",
                            description = exercicio.grupoMuscular,
                            isIncluded = exerciciosAdicionados.any { it.id == exercicio.id },
                            onToggle = {
                                if (viewModel.isExercicioIncluido(exercicio)) {
                                    viewModel.removerExercicio(exercicio)
                                } else {
                                    viewModel.adicionarExercicio(exercicio)
                                }
                            }
                        )

                    }
                }

                CustomButton(
                    text = "Concluir",
                    onClick = {
                        viewModel.editarTreino {
                            navController.popBackStack()

                            coroutineScope.launch {
                                delay(300L) // espera meio segundo (300 milissegundos)
                                Toast.makeText(context, "Treino editado com sucesso!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp) // opcional: deixa o botão com tamanho fixo
                )

                error.value.let {
                    Text(
                        text = it,
                        color = Color.Red
                    )
                }


            }
        }
    )


}


