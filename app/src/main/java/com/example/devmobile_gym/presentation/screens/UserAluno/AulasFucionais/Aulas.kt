package com.example.devmobile_gym.presentation.screens.UserAluno.AulasFucionais

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.domain.model.getDataFormatada
import com.example.devmobile_gym.domain.model.getHoraFormatada
import com.example.devmobile_gym.presentation.components.ClassCardAluno
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.screens.authScreens.AuthState
import com.example.devmobile_gym.presentation.screens.authScreens.AuthViewModel


@Composable
fun ShowAulas(onBack: () -> Unit, navController: NavHostController) {


    val viewmodel: AulasViewModel = viewModel()
    val aulas by viewmodel.aulas.collectAsState()

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
                    aula?.let {
                        ClassCardAluno(
                            idAula = it.id,
                            data = it.getDataFormatada(),
                            aula = it.aula,
                            professor = it.professor,
                            hora = it.getHoraFormatada(),
//                            onQuantidadeChange = viewmodel::onNovaQuantidadeChange,
                            quantAtual =  it.inscritos.size,
                            quantMaxima = it.quantidade_maxima_alunos,
                            onInscricaoClick = { aulaId ->
                                viewmodel.inscreverAluno(
                                    idAula = aula.id,
                                    idAluno = viewmodel.getUserId()
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { /* Handle menu click */ }
}


@Preview
@Composable
fun ShowAulasPreview() {

}
