package com.example.devmobile_gym.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.presentation.components.CustomCalendario
import com.example.devmobile_gym.presentation.components.CustomCard
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.devmobile_gym.R
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.presentation.components.CardHomeProfessor
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.ui.theme.White

@Composable
fun ProfessorHomeScreen(navController: NavHostController, viewModel: ProfessorHomeViewModel = viewModel(), onNavigateToAluno: (Int) -> Unit) {
    val alunos by viewModel.alunos

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedItemIndex = when (currentRoute) {
        "homeProfessor" -> 0
        "aulas" -> 1
        "adicionarRotina" -> 2
        "chatbot" -> 3
        "gerenciar" -> 4
        else -> 0 // default
    }

    CustomScreenScaffoldProfessor  (
        navController = navController,
        title = "Painel Professor",
        onBackClick = { /* Handle back click */ },
        onMenuClick = { /* Handle menu click */ },
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
