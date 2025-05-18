package com.example.devmobile_gym.data.repository

import com.example.devmobile_gym.data.remote.GeminiService
import com.example.devmobile_gym.domain.repository.ChatRepository


class ChatRepositoryImpl(private val geminiService: GeminiService): ChatRepository{
    override suspend fun sendMessage(prompt: String): String {
        return geminiService.sendMessage(prompt)
    }
}