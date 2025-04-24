package com.example.devmobile_gym.presentation.screens.UserProfessor.chatbot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.presentation.components.CardMessage
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes

@Composable
fun ProfessorChatBotScreen(navController: NavHostController, viewModel: ChatBotViewModel = viewModel(), onBack: () -> Unit) {
//    lista de mensagens do chat
    val messages = viewModel.message
//    input do usuario
    val userInput = viewModel.userInputs

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

    CustomScreenScaffoldProfessor(
        navController = navController,
        title = "ChatBot",
        onBackClick = { onBack() },
        needToGoBack = true,
        selectedItemIndex = selectedItemIndex,
        content = { innerModifier ->
            val combinedModifier = innerModifier.padding(0.5.dp)

            Column (
                modifier = combinedModifier.fillMaxSize()
            ){
                LazyColumn (
                    modifier = Modifier.weight(1.5f)
                ){
                    items(messages) { message ->
                        CardMessage(message)
                    }
                }

                Row (
                    modifier = Modifier.weight(0.17f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Column {

                        CustomTextField(
                            value = userInput.value,
                            onValueChange = { viewModel.onUserInputChange(it) },
                            label = "Digite sua mensagem",
                            padding = 0,
                            modifier = Modifier.width(270.dp)
                        )
                    }

                    Column {
                        CustomButton(
                            text = "Enviar",
                            onClick = {
                                viewModel.sendMessage(userInput.value)
                            },
                            modifier = Modifier
                        )
                    }

                }
            }
        }
    )
}