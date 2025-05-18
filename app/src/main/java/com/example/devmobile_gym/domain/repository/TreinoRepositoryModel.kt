package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.model.Treino

interface TreinoRepositoryModel {
    fun adicionaExercicio(treinoId: String, exercicios: List<Exercicio>): List<Exercicio>
//    fun removeExercicio(treinoId: Int, exercicioId: Int)
    suspend fun getTreino(treinoId: String): Treino?
    suspend fun getTreinos() : List<Treino>
}