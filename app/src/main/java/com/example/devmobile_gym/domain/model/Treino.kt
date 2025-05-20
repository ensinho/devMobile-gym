package com.example.devmobile_gym.domain.model

data class Treino(
    val id: String = "",
    val nome: String = "",
    val exercicios: List<String> = emptyList()

)