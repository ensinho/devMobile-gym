package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.Exercicio

interface ExercicioRepository {
    fun getExercicio(exercicioId: Int): Exercicio?
//    fun editarExercicio(exercicioId: Int, nome: String, series: Int, repeticoes: Int)
}