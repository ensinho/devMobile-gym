package com.example.devmobile_gym.domain.model

data class Aula(
    val id: Int,
    val professor: Professor,
    val alocacaoMaxima: Int,
    val data: String // ou LocalDate, se preferir
)