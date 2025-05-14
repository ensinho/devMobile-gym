package com.example.devmobile_gym.domain.model

// Arquivo: domain/model/User.kt
sealed class Usuario(
    open val uid: String?,
    open val nome: String,
    open val email: String?
) {
    data class Aluno(
        override val uid: String?,
        override val nome: String,
        override val email: String?,
        val rotina: List<String>? = null
    ) : Usuario(uid, nome, email)

    data class Professor(
        override val uid: String?,
        override val nome: String,
        override val email: String?
    ) : Usuario(uid, nome, email)
}
