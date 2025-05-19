package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.model.Treino

interface TreinoRepositoryModel {
    fun criarTreino( exercicios: List<Exercicio>, nome: String, alunoId: String)
    suspend fun getTreino(treinoId: String): Treino?
    suspend fun getTreinos() : List<Treino>
}