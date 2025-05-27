package com.example.devmobile_gym.presentation.screens.UserAluno.profile

import android.R.attr.background
import android.R.id.background
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.components.BoxDayStreak
import com.example.devmobile_gym.presentation.components.CardCalendario
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.MonthlyCalendar
import com.example.devmobile_gym.presentation.components.ProfileCard
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun profileScrenn(navController: NavHostController, viewModel: ProfileViewModel = viewModel(), onNavigateToHistorico: () -> Unit) {
    val aluno by viewModel.aluno.collectAsState()
    val userWorkouts by viewModel.currentUserWorkouts.collectAsState()

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

    LaunchedEffect(aluno) { // Agora reage a qualquer mudança no objeto 'aluno'
        aluno?.uid?.let { userId ->
            viewModel.loadCurrentUserWorkouts(userId)
        }
    }

    CustomScreenScaffold(
        navController = navController,
        onBackClick = {},
        selectedItemIndex = selectedItemIndex,
        content = { innerModifier ->
            val combinedModifier = innerModifier.padding(1.dp)
                .background(Color(0xFF1E1E1E))
            LazyColumn (
                modifier = innerModifier.background(Color(0xFF1E1E1E))


            ){
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp, top = 16.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                onNavigateToHistorico()
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.relogio_ic),
                                contentDescription = "Ícone exemplo",
                                modifier = Modifier
                                    .height(40.dp)
                                    .size(27.dp),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Historico",
                                color = Color.White
                            )
                        }
                    }

                }

                item{
                    ProfileCard(
                        name = aluno?.nome ?: "Carregando...",
                        userId = (aluno?.uid?: "Carregando").toString(),
                        weight = " 64 kg",
                        height = " 1,70 m "
                    )
                }
                item{
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        userWorkouts?.let {
                            // Se userWorkouts não é null, exibe o calendário
                            MonthlyCalendar(it.workoutDates)
                        } ?: run {
                            // Se userWorkouts é null, exibe um indicador de carregamento ou mensagem
                            // Você pode adicionar um StateFlow booleano no ViewModel para 'isLoadingUserWorkouts'
                            // para exibir um indicador mais inteligente.
                            Text("Carregando calendário...", color = Color.Gray)
                            // Ou CircularProgressIndicator() se preferir
                        }
                    }
                }
                item {  Spacer(modifier = Modifier.height(40.dp))
                }
                item {Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.width(16.dp))

                    BoxDayStreak("Day Streak", icone = R.drawable.fire_svgrepo_com)

                    Spacer(modifier = Modifier.width(16.dp))

                    BoxDayStreak("Texto exemplo", icone = R.drawable.home_icon)
                }
                }
                item {Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.width(16.dp))

                    BoxDayStreak("Day Streak", icone = R.drawable.medal_icon)

                    Spacer(modifier = Modifier.width(16.dp))

                    BoxDayStreak("Texto exemplo", icone = R.drawable.relogio_ic)
                }
                }
            }

        }
    ) { /* Handle menu click */ }

}
