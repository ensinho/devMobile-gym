package com.example.devmobile_gym.domain.repository

interface ChatRepository {
    suspend fun sendMessage(prompt: String): String
}