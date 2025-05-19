package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.Exercicio

interface ExercicioRepositoryModel {
//    fun getExercicio(exercicioId: Int): Exercicio?
    suspend fun getAllExercicios(): List<Exercicio>
    suspend fun insertExercicio(exercicio: Exercicio)
}