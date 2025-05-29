package com.example.devmobile_gym.presentation.screens.UserProfessor.adicionaEditaAula

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.presentation.components.BoxSeta2
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.components.DateTimePickerInput
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.domain.model.Aula
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.screens.authScreens.AuthState
import com.example.devmobile_gym.presentation.screens.authScreens.AuthViewModel
import com.example.devmobile_gym.ui.theme.White
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun AdicionaEditaAula(
    navController: NavHostController,
    onBack: () -> Unit,
    viewmodel: AdicionaEditaAulaViewModel = viewModel()
) {

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

    val tipoAula by viewmodel.tipoAula.collectAsState()
    val dataHoraSelecionada by viewmodel.dataHoraSelecionada.collectAsState()
    val professor by viewmodel.professor.collectAsState()
    val alocacaoMax by viewmodel.alocacaoMax.collectAsState()
    val status by viewmodel.status.collectAsState()
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
                    Text(
                        text = "Criar Aula ou Funcional",
                        fontSize = 30.sp,
                        color = White,
                        modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                    )
                    CustomTextField(
                        label = "Tipo da aula",
                        value = tipoAula,
                        onValueChange = viewmodel::onTipoAulaChange,
                        padding = 10,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    DateTimePickerInput(
                        selectedDateTime = dataHoraSelecionada,
                        onDateTimeSelected = { novaData ->
                            viewmodel.atualizarDataHora(novaData)
                        }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    CustomTextField(
                        label = "Professor",
                        value = professor,
                        onValueChange = viewmodel::onProfessorChange,
                        padding = 10,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    CustomTextField(
                        label = "Alocação Máxima",
                        value = alocacaoMax,
                        onValueChange = viewmodel::onAlocacaoMaxChange,
                        padding = 10,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    CustomButton(
                        text = "Concluir",
                        onClick = {
                            if (viewmodel.isNumeroInteiro(alocacaoMax)) {
                                val novaAula = dataHoraSelecionada?.let {
                                    Aula(
                                        aula = tipoAula,
                                        dataHora = it,
                                        professor = professor,
                                        quantidade_maxima_alunos = alocacaoMax.toInt()
                                    )
                                }

                                viewmodel.criarAula(
                                    aula = novaAula,
                                    onSuccess = {
                                        navController.popBackStack()
                                        coroutineScope.launch {
                                            delay(300L) // espera meio segundo (300 milissegundos)
                                            Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    onError = {
                                        coroutineScope.launch {
                                            delay(300L) // espera meio segundo (300 milissegundos)
                                            Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                )
                            } else {
                                Toast.makeText(context, "Alocação Máxima deve ser um número inteiro.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp) // opcional: deixa o botão com tamanho fixo
                    )
                }
            }
        }
    )
}

