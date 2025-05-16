package com.example.devmobile_gym.presentation.screens.UserProfessor.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.components.CardHomeProfessor
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes

@Composable
fun ProfessorHomeScreen(
    navController: NavHostController,
    viewModel: ProfessorHomeViewModel = viewModel(),
    onNavigateToAluno: (String) -> Unit
) {
    val alunos by viewModel.alunos.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        viewModel.fetchAlunos()
        isLoading = false
    }


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


    CustomScreenScaffoldProfessor  (
        navController = navController,
        onBackClick = { /* Handle back click */ },
        needToGoBack = false,
        selectedItemIndex = selectedItemIndex,
        content = { innerModifier ->
            val combinedModifier = innerModifier.padding(1.dp)

            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onSurface // Se seu fundo for escuro
                )
            } else {
                LazyColumn(
                    modifier = combinedModifier
                ) {
                    item {
                        Text(
                            text = "Painel de Alunos",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 30.sp,
                            modifier = Modifier.padding(top = 20.dp, start = 30.dp, end = 0.dp, bottom = 25.dp)
                        )
                    }
                    items(alunos) { aluno ->
                        aluno.nome?.let {
                            CardHomeProfessor(
                                texto = it,
                                icone = R.drawable.ic_caneta,
                                onClick = { aluno.uid?.let { it1 -> onNavigateToAluno(it1) } }
                            )
                        }

                    }

                }
            }

        }
    )

}

//@Preview(showBackground = true)
//@Composable
//fun ProfessorHomePreview() {
//    val fakeNavController = rememberNavController()
//    val fakeAlunos = listOf(
//        Aluno(1,"renan","","",),
//        Aluno(2,"joao","","",),
//        Aluno(3,"Lucas","","",)
//    )
//
//    // Recria o conteúdo da tela usando dados mockados
//    CustomScreenScaffoldProfessor(
//        navController = fakeNavController,
//        title = "Home",
//        onBackClick = {},
//        onMenuClick = {},
//        selectedItemIndex = 0,
//        content = { innerModifier ->
//            val combinedModifier = innerModifier.padding(0.dp)
//                .background(Color(0xFF1E1E1E))
//            LazyColumn(modifier = combinedModifier
//                .fillMaxSize()) {
//
//                item {
//                    Text(modifier = Modifier
//                        .padding(10.dp),
//                        color = White,
//                        text = "Painel de Alunos",
//                        fontSize = 35.sp
//                    )
//                }
//                items(fakeAlunos) { aluno ->
//                    CardHomeProfessor(
//                        texto = aluno.nome,
//                        icone = R.drawable.ic_caneta
//                    )
//                    Spacer(Modifier.height(4.dp))
//                }
//            }
//        }
//    )
//}
