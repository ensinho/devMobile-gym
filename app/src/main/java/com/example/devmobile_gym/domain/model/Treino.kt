package com.example.devmobile_gym.domain.model

import java.util.Date

data class Treino(
    val id: String = "",
    val nome: String = "",
    val exercicios: List<String> = emptyList()
)