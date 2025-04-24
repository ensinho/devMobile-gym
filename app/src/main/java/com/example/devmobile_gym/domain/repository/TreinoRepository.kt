package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.model.Treino

interface TreinoRepository {
//    fun adicionaExercicio(treinoId: Int, exercicioId: Int): Exercicio
//    fun removeExercicio(treinoId: Int, exercicioId: Int)
    fun getTreino(treinoId: Int): Treino?
}