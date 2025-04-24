package com.example.devmobile_gym.presentation.screens.UserAluno.AulasFucionais

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.presentation.components.ClassCardAluno
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes


@Composable
fun ShowAulas(onBack: () -> Unit, navController: NavHostController) {

    val viewmodel: AulasViewModel = viewModel()
    val aulas by viewmodel.aulas

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val selectedItemIndex = when (currentRoute) {
        AlunoRoutes.Home -> 0
        AlunoRoutes.Search -> 1
        AlunoRoutes.QrCode -> 2
        AlunoRoutes.Chatbot -> 3
        AlunoRoutes.Profile -> 4
        else -> 0 // default
    }

    CustomScreenScaffold(
        navController = navController,
        needToGoBack = true,
        onBackClick = { onBack() },
        selectedItemIndex = selectedItemIndex,
        content = { innerModifier ->
            val combinedModifier = innerModifier.padding(1.dp)



            LazyColumn(
                modifier = combinedModifier
            ) {
                item {
                    Text(
                        text = "Aulas & Funcionais",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(8.dp),
                        fontSize = 30.sp
                    )
                }

                itemsIndexed(aulas) { index, aula ->
                    ClassCardAluno(
                        data = aula?.data.toString(),
                        aula = "Jiu-Jitsu",
                        professor = aula?.professor?.nome.toString(),
                        hora = "00:00"
                    )
                }
            }
        }
    ) { /* Handle menu click */ }
}


@Preview
@Composable
fun ShowAulasPreview() {
}
