package com.example.devmobile_gym.domain.model

data class Aluno(
    val userId: Int,
    val nome: String,
    val email: String,
    var senha: String,
    val rotina: Rotina? = null
)