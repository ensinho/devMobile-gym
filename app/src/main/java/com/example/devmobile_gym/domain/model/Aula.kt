package com.example.devmobile_gym.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Aula(
    val id: String = "",
    val professor: String = "",
    val aula: String = "",
    val dataHora: Date = Date(),
    val quantidade_alunos: Int = 0,
    val quantidade_maxima_alunos: Int = 0

)

fun Aula.getDataFormatada(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(this.dataHora)
}

fun Aula.getHoraFormatada(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(this.dataHora)
}