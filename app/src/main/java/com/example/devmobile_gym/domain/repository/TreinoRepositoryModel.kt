package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.model.Treino

interface TreinoRepositoryModel {
    fun criarTreino(exercicios: List<String>, nome: String, alunoId: String)
    suspend fun getTreino(treinoId: String): Treino?
    suspend fun getTreinos() : List<Treino>
    suspend fun getTreinosByIds(ids: List<String>): List<Treino?>
    suspend fun updateTreino(treinoId: String, nome: String, exercicios: List<String>)

    suspend fun deleteTreino(treinoId: String)

}