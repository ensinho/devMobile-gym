package com.example.devmobile_gym.presentation.screens.UserAluno.chatBot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.presentation.components.CardMessage
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes

@Composable
fun ChatBotScreen(navController: NavHostController, viewModel: ChatBotViewModel = viewModel(), onBack: () -> Unit) {
//    lista de mensagens do chat
    val messages = viewModel.message
//    input do usuario
    val userInput = viewModel.userInputs
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
    ) { /* Handle menu click */ }
}