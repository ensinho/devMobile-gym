package com.example.devmobile_gym.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.ChatRepository
import com.example.devmobile_gym.domain.model.Message
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()
    val chatMessages = mutableStateListOf<Message>()

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        val userMessage = Message(role = "user", content = userText)
        chatMessages.add(userMessage)

        viewModelScope.launch {
            try {
                val botReply = repository.sendChat(chatMessages)
                chatMessages.add(Message(role = "model", content = botReply))
            } catch (e: Exception) {
                chatMessages.add(Message(role = "model", content = "Erro ao responder: ${e.localizedMessage}"))
            }
        }
    }
}
