package com.example.devmobile_gym.presentation.screens.chatBot

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.presentation.components.CardMessage
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.CustomTextField

@Composable
fun ChatBotScreen(viewModel: ChatBotViewModel = viewModel(), onBack: () -> Unit) {
//    lista de mensagens do chat
    val messages = viewModel.message
//    input do usuario
    val userInput = viewModel.userInputs

    CustomScreenScaffold(
        title = "ChatBot",
        onBackClick = { onBack() },
        onMenuClick = { /* Handle menu click */ },
        needToGoBack = true,
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