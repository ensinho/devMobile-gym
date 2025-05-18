package com.example.devmobile_gym.domain.UseCases

import com.example.devmobile_gym.domain.repository.ChatRepository

class SendChatMessageUseCase(private val  repository: ChatRepository) {
    suspend operator fun invoke(prompt: String) : String{
        return repository.sendMessage(prompt)
    }
}