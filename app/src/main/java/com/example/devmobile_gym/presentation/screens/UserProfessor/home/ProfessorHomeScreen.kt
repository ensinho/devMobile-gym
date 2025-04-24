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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.components.CardHomeProfessor
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes

@Composable
fun ProfessorHomeScreen(navController: NavHostController, viewModel: ProfessorHomeViewModel = viewModel(), onNavigateToAluno: (Int) -> Unit) {
    val alunos by viewModel.alunos

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

            LazyColumn(
                modifier = combinedModifier
            ) {
//                Text
                items(alunos) { aluno ->
                    CardHomeProfessor(
                        texto = aluno.nome,
                        icone = R.drawable.ic_caneta,
                        onClick = { onNavigateToAluno(aluno.userId)}
                    )
                    Spacer(Modifier.height(8.dp))

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
