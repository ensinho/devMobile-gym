package com.example.devmobile_gym.domain.model

data class Message(
    val role: String,
    val content: String
)

enum class Sender {
    BOT,
    USER
}