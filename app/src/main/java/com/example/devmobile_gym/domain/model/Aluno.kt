package com.example.devmobile_gym.domain.model

data class Aluno(
    val uid: String?,
    val nome: String?,
    val email: String?,
    val rotina: Rotina? = null
)