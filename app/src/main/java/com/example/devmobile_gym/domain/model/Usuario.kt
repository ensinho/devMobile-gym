package com.example.devmobile_gym.domain.model

data class Usuario(
    val email: String,
    val nome: String,
    var isProfessor: Boolean = false
)

/* 
    sealed class Usuario(val email: String, val nome: String) {
    data class Professor(val email: String, val nome: String, val area: String) : Usuario(email, nome)
    data class Aluno(val email: String, val nome: String, val matricula: String) : Usuario(email, nome)
    }

*/
