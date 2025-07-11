package com.example.devmobile_gym.presentation.screens.UserProfessor.gerenciarMaquinasExercicios




import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.components.BoxSeta
import com.example.devmobile_gym.presentation.components.CustomCard
import com.example.devmobile_gym.presentation.components.CustomExerciseItem
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import com.example.devmobile_gym.presentation.screens.authScreens.AuthState
import com.example.devmobile_gym.presentation.screens.authScreens.AuthViewModel
import com.example.devmobile_gym.ui.theme.White

@Composable
fun GerenciarMaquinasExerciciosScreen(navController: NavHostController, viewModel: GerenciarMaquinasExerciciosViewModel = viewModel(), onBack: () -> Unit) {

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

    val search by viewModel.search.collectAsState() // busca por exercicio
    val maqExercFiltrados by viewModel.MaqExercFiltrados

    CustomScreenScaffoldProfessor(
        navController = navController,
        needToGoBack = false,
        onBackClick = { onBack() },
        selectedItemIndex = selectedItemIndex,
        content = { innerModifier ->

            val combinedModifier = innerModifier.padding(1.dp)

            Column(
                modifier = combinedModifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ){

                    Text(
                        text = "Buscar",
                        fontSize = 30.sp,
                        color = White
                    )
                    IconButton(
                        onClick = {
                            navController.navigate(ProfessorRoutes.AdicionarExercicio)
                            //navController.navigate("${ProfessorRoutes.CriarTreino}/${alunoSelecionado?.id}")
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
                Spacer(Modifier.height(5.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomTextField(
                        label = "Busque exercícios" ,
                        value = search,
                        onValueChange = viewModel::onSearchChange,
                        padding = 10,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(maqExercFiltrados) { MaqExerc ->
                        val grupoMuscular: MutableList<String> = mutableListOf(MaqExerc.grupoMuscular)

                        CustomCard(
                            isAdm = true,
                            needButton = false,
                            treino = MaqExerc.nome.toString(),
                            description = grupoMuscular,
                            buttonText = "",
                            onButtonClick = {},
                            editButton = { navController.navigate("${ProfessorRoutes.EditarExercicio}/${MaqExerc.id}")},
                            deleteButton = {viewModel.removerExercicio(MaqExerc.id.toString()) }
                        )
                    }

                }
            }
        }
    )
}