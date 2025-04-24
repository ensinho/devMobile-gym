package com.example.devmobile_gym.data.repository

import com.example.devmobile_gym.data.mock.MockData
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.ExercicioRepository

class ExercicioRepositoryMock : ExercicioRepository {

    override fun getExercicio(exercicioId: Int): Exercicio? {
        return MockData.rotinaMock.treinos
            .flatMap { it.exercicios }
            .find { it.id == exercicioId }
    }

    override fun getAllExercicios(): List<Exercicio> {
        return MockData.rotinaMock.treinos
            .flatMap { it.exercicios }
    }

    override fun editarExercicio(
        exercicioId: Int,
        nome: String,
        series: Int,
        grupoMuscular: String,
        repeticoes: Int,
        peso: Int
    ): Boolean {
        MockData.rotinaMock.treinos.forEach { treino ->
            treino.exercicios.forEach { exercicio ->
                if (exercicio.id == exercicioId) {
                    exercicio.nome = nome
                    exercicio.series = series
                    exercicio.repeticoes = repeticoes
                    exercicio.peso = peso
                    return true
                }
            }
        }
        return false
    }
}
