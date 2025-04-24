package com.example.devmobile_gym.presentation.screens.UserProfessor.TeladeGerenciamento

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.presentation.components.BoxSeta
import com.example.devmobile_gym.presentation.components.BoxSeta2
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes



@Composable
fun TelaGerenciamento(navController: NavHostController, onBack: () -> Unit){

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
        title = "Aulas & Funcionais",
        onBackClick = { onBack() },
        onMenuClick = { /* Handle menu click */ },
        selectedItemIndex = selectedItemIndex,
        needToGoBack = true,
        content = { innerModifier ->
            val combinedModifier = innerModifier.padding(1.dp)

            Box(
                contentAlignment = Alignment.Center,
            ){
                Column {
                    //CustomTextField(Modifier.weight(1f), "Título da Área",,{nome = it},12)
                    Spacer(modifier = Modifier.height(6.dp))
                    BoxSeta2("Data")
                    Spacer(modifier = Modifier.height(6.dp))
                    BoxSeta2("Professor")
                    Spacer(modifier = Modifier.height(6.dp))
                    BoxSeta2("Alocação max")
                    Spacer(modifier = Modifier.height(6.dp))
                    //CustomTextField(Modifier.weight(1f), "Horário","customfield",,12)
                    Spacer(modifier = Modifier.height(6.dp))
                    Button( onClick = { /*TODO*/ }) {
                        Text(text = "Concluir", fontSize = 12.sp)
                    }
                }
        }
    })

}
