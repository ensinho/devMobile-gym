package com.example.devmobile_gym.domain.model

data class Aluno(
    val uid: String?,
    val nome: String?,
    val email: String?,
    val rotina: Rotina? = null,
    val streakAtual: Int = 0,
    val ultimoDiaTreinado: String? = null,
    val diasTreinados: List<String> = emptyList()

)