package com.example.devmobile_gym.presentation.screens.AulasFucionais

import ClassCard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.presentation.components.CustomCalendario
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.ui.theme.White
import kotlin.time.Duration.Companion.days


@Composable
fun ShowAulas(navController: NavHostController, selectedItemIndex: Int) {

    val viewmodel: AulasViewModel = viewModel()
    val aulas by viewmodel.aulas

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val selectedItemIndex = when (currentRoute) {
        "home" -> 0
        "search" -> 1
        "qrcode" -> 2
        "chatbot" -> 3
        "profile" -> 4
        else -> 0 // default
    }

    CustomScreenScaffold(
        navController = navController,
        title = "Home",
        onBackClick = { /* Handle back click */ },
        onMenuClick = { /* Handle menu click */ },
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
                    ClassCard(
                        data = aula?.data.toString(),
                        aula = "Jiu-Jitsu",
                        professor = aula?.professor?.nome.toString(),
                        hora = "00:00"
                    )
                }
            }
        })
}


@Preview
@Composable
fun ShowAulasPreview() {
    ShowAulas(navController = NavHostController(LocalContext.current), selectedItemIndex = 0)
}
