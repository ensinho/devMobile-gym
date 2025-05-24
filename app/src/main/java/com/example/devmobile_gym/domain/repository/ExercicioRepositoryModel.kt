package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.Exercicio

interface ExercicioRepositoryModel {
    suspend fun getExercicio(exercicioId: String): Exercicio?
    suspend fun getAllExercicios(): List<Exercicio>
    suspend fun insertExercicio(exercicio: Exercicio)
    suspend fun getExerciciosOfTreino(treinoId: String): List<Exercicio>
    suspend fun getExerciciosByIds(ids: List<String>): List<Exercicio>
    suspend fun getExercicioImageUrl(id: String): String?
}