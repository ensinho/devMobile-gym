package com.example.devmobile_gym.presentation.screens.UserProfessor.gerenciaAluno

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.components.CustomCard
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import com.example.devmobile_gym.presentation.screens.authScreens.AuthState
import com.example.devmobile_gym.presentation.screens.authScreens.AuthViewModel
import com.example.devmobile_gym.ui.theme.White

@Composable
fun GerenciaAlunoScreen(
    navController: NavHostController,
    backStackEntry: NavBackStackEntry,
    onBack: () -> Unit,
    navigateToEdit: (String) -> Unit
) {
    // factory para o viewmodel receber os parametros da rota
    val viewModel: GerenciaAlunoViewModel = viewModel(
        viewModelStoreOwner = backStackEntry,
        factory = GerenciaAlunoViewModel.Factory
    )

//    config de rota da navbar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedItemIndex = when (currentRoute) {
        ProfessorRoutes.Home -> 0
        ProfessorRoutes.Aulas -> 1
        ProfessorRoutes.AdicionarRotina -> 2
        ProfessorRoutes.Chatbot -> 3
        ProfessorRoutes.Gerenciar -> 4
        else -> 0 // default
    }

    val alunoSelecionado by viewModel.alunoSelecionado.collectAsState()
    val treinosAluno by viewModel.treinos.collectAsState()
    val context = LocalContext.current

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

    LaunchedEffect(alunoSelecionado?.rotina) {
        alunoSelecionado?.let {
            viewModel.loadData()
        }
    }

    CustomScreenScaffoldProfessor(
        navController = navController,
        needToGoBack = true,
        onBackClick = { onBack() },
        selectedItemIndex = selectedItemIndex,
        content = { innerModifier ->
            val combinedModifier = innerModifier.padding(8.dp)

            LazyColumn(
                verticalArrangement = Arrangement.Center,
                modifier = combinedModifier
            ) {
                if (viewModel.isLoading.value) {
                    item {
                        CircularProgressIndicator()
                    }
                } else if (viewModel.error.value != null) {
                    item {
                        Text("Erro: ${viewModel.error.value}", color = Color.Red)
                    }
                } else if (alunoSelecionado == null) {
                    item {
                        Text("Aluno não encontrado", color = Color.White)
                    }
                }

                item {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(
                            text = "${alunoSelecionado?.nome} - GERENCIAMENTO",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }
                    Spacer(Modifier.height(25.dp))

                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ){

                        Row {

                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = "Rotinas",
                                tint = Color.White,
                                modifier = Modifier.size(35.dp)
                            )
                            Text(
                                text = "Rotinas",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                        }

                        IconButton(
                            onClick = {
                                alunoSelecionado?.uid?.let { uid ->
                                    navController.navigate("${ProfessorRoutes.AdicionarRotina}/${alunoSelecionado!!.uid}")
                                }
                            },
                            modifier = Modifier.size(35.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_circle_item),
                                contentDescription = "Adicionar",
                                tint = Color.White,
                                modifier = Modifier.size(35.dp)
                            )
                        }

                    }
                }
                items(treinosAluno) { treino ->
                    treino?.nome?.let {
                        CustomCard(
                            isAdm = true,
                            needButton = false,
                            treino = it,
                            description = treino.exercicios.map {
                                viewModel.getNomeExercicio(it)
                                                                },
                            buttonText = "Iniciar Treino",
                            /* Implementar a logica de Editar e Remover treino*/
                            // obs -> vai precisar modificar o componente CustomCard
                            onButtonClick = { /*user adm nao precisa dessa funcao*/ },
                            editButton = {
                                treino.id.let { id -> // Verifique se o ID não é nulo
                                    navigateToEdit(id)
                                }
                            },
                            deleteButton = {
                                treino.id.let {
                                   viewModel.deletarTreino(it) {
                                       Toast.makeText(context, "Treino deletado com sucesso!", Toast.LENGTH_SHORT).show()
                                   }
                                }
                            }
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }


        }
    )
}

