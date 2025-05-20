package com.example.devmobile_gym.domain.model

import com.google.firebase.firestore.IgnoreExtraProperties

// Arquivo: domain/model/User.kt
sealed class Usuario(
    open val uid: String?,
    open val nome: String,
    open val email: String?
) {
    @IgnoreExtraProperties
    data class Aluno(
        override val uid: String? = "",
        override val nome: String = "",
        override val email: String? = "",
        val rotina: List<String>? = emptyList()
    ) : Usuario(uid, nome, email)

    data class Professor(
        override val uid: String? = "",
        override val nome: String = "",
        override val email: String? = ""
    ) : Usuario(uid, nome, email)
}
