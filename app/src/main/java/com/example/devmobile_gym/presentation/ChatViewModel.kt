package com.example.devmobile_gym.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.ChatRepository
import com.example.devmobile_gym.domain.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()
    val chatMessages = mutableStateListOf<Message>()

    private val _userInput = MutableStateFlow<String>("")
    val userInput : StateFlow<String> = _userInput.asStateFlow()

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        val userMessage = Message(role = "user", content = userText)
        chatMessages.add(userMessage)

        viewModelScope.launch {
            try {
                val botReply = repository.sendChat(chatMessages)
                chatMessages.add(Message(role = "model", content = botReply))
                resetInput()
            } catch (e: Exception) {
                chatMessages.add(
                    Message(
                        role = "model",
                        content = "Erro ao responder: ${e.message ?: e.toString()}"
                    )
                )
            }
        }
    }

    fun onUserInputChange(newInput: String) {
        _userInput.value = newInput
    }

    private fun resetInput() {
        _userInput.value = ""
    }
}
