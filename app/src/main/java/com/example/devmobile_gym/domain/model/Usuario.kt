package com.example.devmobile_gym.domain.model

data class Usuario(
    val email: String,
    val nome: String,
    var isProfessor: Boolean = false
)
