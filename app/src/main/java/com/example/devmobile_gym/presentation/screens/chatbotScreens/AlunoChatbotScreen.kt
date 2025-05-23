package com.example.devmobile_gym.presentation.screens.chatbotScreens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import com.example.devmobile_gym.presentation.screens.UserProfessor.home.ProfessorHomeViewModel
import kotlinx.coroutines.delay

@Composable
fun AlunoChatbotScreen(
    viewModel: ChatbotViewModel = viewModel(),
    navController: NavHostController
) {
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var inputText by remember { mutableStateOf("") }

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
        onBackClick = { /* Handle back click */ },
        needToGoBack = false,
        selectedItemIndex = selectedItemIndex,
        content = {
                innerModifier ->
            val combinedModifier = innerModifier.padding(1.dp)

            Column(modifier = combinedModifier.fillMaxSize()) {

                // Lista de mensagens
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    reverseLayout = true
                ) {
                    items(messages.reversed()) { message ->
                        ChatBubble(message)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Campo de entrada e botão enviar
                Row(modifier = Modifier.fillMaxWidth().padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
//                TextField(
//                    value = inputText,
//                    onValueChange = { inputText = it },
//                    modifier = Modifier.weight(1f),
//                    placeholder = { Text("Digite sua dúvida sobre exercícios...") }
//                )
                    CustomTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        label = "Digite sua dúvida sobre exercícios...",
                        modifier = Modifier.weight(1f),
                        padding = 0
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            viewModel.sendMessage(inputText)
                            inputText = ""
                        },
                        enabled = inputText.isNotBlank()
                    ) {
                        Text("Enviar")
                    }
                }
            }
        },
        onMenuClick = {}
    )


}





