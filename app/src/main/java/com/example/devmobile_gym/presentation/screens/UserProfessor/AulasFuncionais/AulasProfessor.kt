package com.example.devmobile_gym.presentation.screens.UserProfessor.AulasFuncionais

import ClassCardProfessor
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.devmobile_gym.R
import com.example.devmobile_gym.domain.model.getDataFormatada
import com.example.devmobile_gym.domain.model.getHoraFormatada
import com.example.devmobile_gym.presentation.components.CardHomeProfessor
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import com.example.devmobile_gym.presentation.screens.UserAluno.AulasFucionais.AulasProfessorViewModel
import com.example.devmobile_gym.ui.theme.White
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AulasProfessorScreen(navController: NavHostController){
    val viewmodel: AulasProfessorViewModel = viewModel()
    val aulas by viewmodel.aulas.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val status by viewmodel.status.collectAsState()

    LaunchedEffect(aulas) {
        viewmodel.carregaAulas()
    }


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
        onBackClick = {  },
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
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ){

                            Text(
                                fontSize = 30.sp,
                                text = "Aulas & Funcionais"
                            )
                            IconButton(
                                onClick = {
                                    navController.navigate(ProfessorRoutes.AdicionaEditaAula)
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
                    aula?.aula?.let {
                        ClassCardProfessor(
                            data = aula.getDataFormatada(),
                            aula = it,
                            professor = aula.professor,
                            hora = aula.getHoraFormatada(),
                            onEditClick = { navController.navigate(ProfessorRoutes.AdicionaEditaAula) },
                            onRemoveClick = {
                                viewmodel.deleteAula(
                                    aula = aula,
                                    onSuccess = {
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
                            }
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                }
            }
        })
}
