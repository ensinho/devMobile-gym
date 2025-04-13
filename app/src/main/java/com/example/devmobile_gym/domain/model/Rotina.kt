package com.example.devmobile_gym.domain.model

data class Rotina(
    val id: Int,
    val nome: String,
    val descricao: String,
    val treinos: List<Treino>

)