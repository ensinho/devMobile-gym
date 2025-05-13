package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.Treino

interface TreinoRepositoryModel {
//    fun adicionaExercicio(treinoId: Int, exercicioId: Int): Exercicio
//    fun removeExercicio(treinoId: Int, exercicioId: Int)
    suspend fun getTreino(treinoId: String): Treino?
    suspend fun getTreinos() : List<Treino>
}