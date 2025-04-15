package com.example.devmobile_gym.domain.model

data class Treino(
    val id: Int,
    val nome: String,
    val exercicios: List<Exercicio>

)