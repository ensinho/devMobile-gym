package com.example.devmobile_gym.domain.model

import java.util.Date

data class TreinoComData(
    val treino: Treino = Treino(), // Valor padrão
    val dataRealizacao: Date = Date() // Valor padrão
)