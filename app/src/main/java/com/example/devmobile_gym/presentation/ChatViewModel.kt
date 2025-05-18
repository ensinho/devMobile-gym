package com.example.devmobile_gym.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.domain.UseCases.SendChatMessageUseCase
import kotlinx.coroutines.launch
import kotlin.collections.mutableListOf

class ChatViewModel(
    private val sendChatMessageUseCase: SendChatMessageUseCase
) : ViewModel() {

    private val _chatMessages = mutableListOf<String>()
    val chatMessages: List<String> = _chatMessages

    fun sendMessage(prompt : String){
        viewModelScope.launch {
            val response = sendChatMessageUseCase(prompt)
            _chatMessages += "Você: $prompt"
            _chatMessages += "Gemini: $response"

        }
    }

}