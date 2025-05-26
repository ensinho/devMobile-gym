package com.example.devmobile_gym.domain.model

data class Aluno(
    val uid: String = "", // gerado pelo banco de dados

    // informações do cadastro
    val nome: String = "",
    val email: String = "",

    // geradas ao treinar
    val diasTreinados: List<String> = emptyList(),
    val historico: List<TreinoComData> = emptyList(), // armazena os ids dos treinos
    val ultimoDiaTreinado: String = "",
    val exercicioFavorito: String = "",

    // gerada pelo professor(a)
    val rotina: List<String> = emptyList()
)