package com.example.devmobile_gym.presentation.screens.UserProfessor.adicionaEditaAula

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.components.DateTimePickerInput // Importa a versão corrigida do DateTimePickerInput
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.domain.model.Aula
import com.example.devmobile_gym.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun AdicionaEditaAula(
    navController: NavHostController,
    onBack: () -> Unit,
    viewmodel: AdicionaEditaAulaViewModel = viewModel()
) {
    // Observa a rota atual para determinar o item selecionado na barra de navegação inferior
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedItemIndex = when (currentRoute) {
        ProfessorRoutes.Home -> 0
        ProfessorRoutes.Aulas -> 1
        ProfessorRoutes.AdicionarRotina -> 2 // Se esta rota também for mapeada na navbar
        ProfessorRoutes.Chatbot -> 3
        ProfessorRoutes.Gerenciar -> 4
        ProfessorRoutes.AdicionaEditaAula -> 1 // Considero que AdicionaEditaAula se encaixa em "Aulas"
        else -> 0 // Padrão para Home se a rota não for encontrada
    }

    // Coleta os estados do ViewModel
    val tipoAula by viewmodel.tipoAula.collectAsState()
    val dataHoraSelecionada by viewmodel.dataHoraSelecionada.collectAsState() // Agora do tipo Date?
    val professor by viewmodel.professor.collectAsState()
    val alocacaoMax by viewmodel.alocacaoMax.collectAsState()
    val status by viewmodel.status.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Usa o scaffold personalizado para o professor, que inclui a TopBar e BottomBar
    CustomScreenScaffoldProfessor (
        navController = navController,
        onBackClick = { onBack() }, // Função para voltar à tela anterior
        selectedItemIndex = selectedItemIndex,
        needToGoBack = true, // Indica que a tela precisa de um botão de voltar
        content = { innerModifier -> // Conteúdo principal da tela
            val combinedModifier = innerModifier
                .padding(1.dp)
                .fillMaxSize() // Ocupa todo o espaço disponível após o padding do scaffold

            Box(
                modifier = combinedModifier,
                contentAlignment = Alignment.Center // Centraliza o conteúdo da coluna na Box
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally // Centraliza os elementos da coluna horizontalmente
                ) {
                    Text(
                        text = "Criar Aula ou Funcional",
                        fontSize = 30.sp,
                        color = White,
                        modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp)) // Espaçamento maior para o título
                    CustomTextField(
                        label = "Tipo da aula",
                        value = tipoAula,
                        onValueChange = viewmodel::onTipoAulaChange,
                        padding = 10,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(10.dp)) // Espaçamento entre campos

                    // --- AQUI É ONDE O DateTimePickerInput É USADO ---
                    DateTimePickerInput(
                        selectedDateTime = dataHoraSelecionada, // Agora do tipo Date?
                        onDateTimeSelected = { novaData -> // novaData é do tipo Date
                            viewmodel.atualizarDataHora(novaData)
                        },
                        modifier = Modifier.padding(horizontal = 10.dp) // Adiciona padding horizontal
                    )
                    // --- FIM DA ALTERAÇÃO ---

                    Spacer(modifier = Modifier.height(10.dp)) // Espaçamento entre campos
                    CustomTextField(
                        label = "Professor",
                        value = professor,
                        onValueChange = viewmodel::onProfessorChange,
                        padding = 10,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(10.dp)) // Espaçamento entre campos
                    CustomTextField(
                        label = "Alocação Máxima",
                        value = alocacaoMax,
                        onValueChange = viewmodel::onAlocacaoMaxChange,
                        padding = 10,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(16.dp)) // Espaçamento antes do botão

                    CustomButton(
                        text = "Concluir",
                        onClick = {
                            // Valida se a alocação máxima é um número inteiro
                            if (viewmodel.isNumeroInteiro(alocacaoMax)) {
                                // Cria um objeto Aula se a data e hora foram selecionadas
                                val novaAula = dataHoraSelecionada?.let {
                                    Aula(
                                        aula = tipoAula,
                                        dataHora = it, // 'it' agora é do tipo Date
                                        professor = professor,
                                        quantidade_maxima_alunos = alocacaoMax.toInt()
                                    )
                                }

                                // Chama a função do ViewModel para criar a aula
                                viewmodel.criarAula(
                                    aula = novaAula,
                                    onSuccess = {
                                        navController.popBackStack() // Volta para a tela anterior
                                        coroutineScope.launch {
                                            delay(300L) // Pequeno atraso para a navegação ser percebida
                                            Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    onError = {
                                        coroutineScope.launch {
                                            delay(300L)
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
                            .height(50.dp) // Define a altura do botão
                    )
                }
            }
        }
    )
}
