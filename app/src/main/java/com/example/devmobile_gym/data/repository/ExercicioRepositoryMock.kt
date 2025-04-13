package com.example.devmobile_gym.data.repository

import com.example.devmobile_gym.data.mock.MockData
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.ExercicioRepository

class ExercicioRepositoryMock : ExercicioRepository {
    override fun getExercicio(exercicioId: Int): Exercicio? {
        /* vamos mudar isso aq quando tivermos uma api com os exercicios */
        /* vai ficar mais simples quando trocarmos os dados mockados */
        MockData.rotinaMock.treinos.forEach { treino ->
            treino.exercicios.forEach { exercicio ->
                if (exercicio.id == exercicioId) {
                    return exercicio
                }
            }
        }

        return null
    }
//    criar quando tiver o banco. Não dá para alterar as info com os dados mockados
//    override fun editarExercicio(exercicioId: Int, nome: String, series: Int, repeticoes: Int) {
//        MockData.rotinaMock.treinos.forEach { treino ->
//            treino.exercicios.forEach { exercicio ->
//                if (exercicio.id == exercicioId) {
//                    exercicio.nome = nome
//                    exercicio.series = series
//                    exercicio.repeticoes = repeticoes
//                }
//            }
//        }
//    }
}