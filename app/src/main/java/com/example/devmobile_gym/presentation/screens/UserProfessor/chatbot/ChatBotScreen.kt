package com.example.devmobile_gym.presentation.screens.UserProfessor.chatbot

import android.R.attr.prompt
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.domain.model.Message
import com.example.devmobile_gym.domain.model.Sender
import com.example.devmobile_gym.presentation.ChatViewModel
import com.example.devmobile_gym.presentation.components.CardMessage
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import com.example.devmobile_gym.utils.parseHtmlToAnnotated

[@Composable
fun ProfessorChatBotScreen(navController: NavHostController, viewModel: ChatViewModel = viewModel(), onBack: () -> Unit) {
    val messages = viewModel.chatMessages
    var userInput by remember { mutableStateOf("") }

    CustomScreenScaffoldProfessor(
        navController = navController,
        onBackClick = { onBack() },
        needToGoBack = true,
        selectedItemIndex = 3,
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(modifier = Modifier.weight(1.5f)) {
                    items(messages) { message ->
                        val annotatedText = parseHtmlToAnnotated(message.content)
                        CardMessage(content = annotatedText, sender = message.role)
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(0.17f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CustomTextField(
                        value = userInput,
                        onValueChange = { userInput = it },
                        label = "Digite sua mensagem",
                        padding = 0,
                        modifier = Modifier.weight(1f)
                    )
                    CustomButton(
                        text = "Enviar",
                        onClick = {
                            viewModel.sendMessage(userInput)
                            userInput = ""
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    )
}
]