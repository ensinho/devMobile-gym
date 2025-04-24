package com.example.devmobile_gym.presentation.screens.UserProfessor.AulasFuncionais

import ClassCardProfessor
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.components.CardHomeProfessor
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import com.example.devmobile_gym.presentation.screens.UserAluno.AulasFucionais.AulasProfessorViewModel

@Composable
fun AulasProfessorScreen(navController: NavHostController, onNavigateToAulas:() -> Unit){
    val viewmodel: AulasProfessorViewModel = viewModel()
    val aulas by viewmodel.aulas

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedItemIndex = when (currentRoute) {
        ProfessorRoutes.Home -> 0
        ProfessorRoutes.Aulas -> 1
        ProfessorRoutes.AdicionarRotina -> 2
        ProfessorRoutes.Chatbot-> 3
        ProfessorRoutes.Gerenciar-> 4
        else -> 0 // default
    }
    CustomScreenScaffoldProfessor (
        navController = navController,
        title = "Olá",
        onBackClick = {  },
        onMenuClick = { /* Handle menu click */ },
        selectedItemIndex = selectedItemIndex,
        needToGoBack = true,
        content = { innerModifier ->
            val combinedModifier = innerModifier.padding(1.dp)

            LazyColumn (
                modifier = combinedModifier
            ) {
                item {
                    Row( modifier = Modifier
                        .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            fontSize = 30.sp,
                            text = "Aulas & Funcionais"
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(id = R.drawable.add_circle_item),
                            contentDescription = "adicionar aula",
                            modifier = Modifier
                                .clickable {  }
                                .height(40.dp)
                                .size(27.dp),
                            tint = Color.Unspecified
                        )
                    }
                }

                items(aulas) { aula ->
                    if (aula != null) {
                        ClassCardProfessor(
                            data = aula.data,
                            aula = "Jiu-jitsu", //adicionar atributo para a Classe Aula depois
                            professor = aula.professor.nome,
                            hora = aula.hora
                        )
                    }
                    Spacer(Modifier.height(8.dp))

                }
            }
        })
}

//@Preview
//@Composable
//fun aulasProfessorPreview(){
//    AulasProfessorScreen(
//        navController = rememberNavController()
//    )
//}