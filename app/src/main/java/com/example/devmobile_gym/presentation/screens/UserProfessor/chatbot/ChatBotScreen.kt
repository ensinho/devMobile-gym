package com.example.devmobile_gym.presentation.screens.UserProfessor.chatbot

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.presentation.ChatViewModel
import com.example.devmobile_gym.presentation.components.CardMessage
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes

@Composable
fun ProfessorChatBotScreen(
    navController: NavHostController,
    chatViewModel: ChatViewModel = viewModel(),
    onBack: () -> Unit
) {
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

    val messages = chatViewModel.chatMessages
    val userInput by chatViewModel.userInput.collectAsState()

    CustomScreenScaffoldProfessor(
        navController = navController,
        onBackClick = onBack,
        needToGoBack = true,
        selectedItemIndex = selectedItemIndex,
        content = { innerModifier ->
            val combinedModifier = innerModifier.padding(1.dp)

            Column(
                modifier = combinedModifier
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxWidth()
                ) {
                    items(messages) { message ->
                        CardMessage(message = message)
                    }
                }

                Row (
                    modifier = Modifier.weight(0.17f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Column {

                        CustomTextField(
                            value = userInput,
                            onValueChange = { chatViewModel.onUserInputChange(it) }, // ← CORRETO
                            label = "Digite sua mensagem",
                            padding = 0,
                            modifier = Modifier.width(270.dp)
                        )

                    }

                    Column {
                        CustomButton(
                            text = "Enviar",
                            onClick = {
                                chatViewModel.sendMessage(userInput)
                            },
                            modifier = Modifier
                        )
                    }

                }
            }
        }
    )
}

