package com.example.devmobile_gym.presentation.screens.chatBot

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.domain.model.Message
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatBotViewModel : ViewModel() {
    private val _message = mutableStateListOf<Message>()
    val message: List<Message> = _message

    private val _userInputs = mutableStateOf("")
    val userInputs: State<String> = _userInputs

    fun onUserInputChange(newInput: String) {
        _userInputs.value = newInput

    }

    fun sendMessage(userMessage: String) {
        if (userMessage.isNotEmpty()) {
            _message.add(Message(text = userMessage, isUser = true))
            _userInputs.value = ""

            // Resposta do chatbot
            simulateChatBotResponse(userMessage)
        }

    }

    private fun generateBotResponse(userMessage: String): String {
        return when {
            userMessage.contains("olá", ignoreCase = true) -> "Olá! Como posso ajudar?"
            userMessage.contains("treino", ignoreCase = true) -> "Posso te explicar sobre exercícios e máquinas!"
            else -> "Desculpe, ainda estou aprendendo. Tente perguntar de outra forma!!"

        }
    }

    private fun simulateChatBotResponse(userMessage: String) {
        viewModelScope.launch {
            delay(1000)
            val botResponse = generateBotResponse(userMessage)
            _message.add(Message(text = botResponse, isUser = false))
        }
    }
}