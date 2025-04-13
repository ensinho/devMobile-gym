package com.example.devmobile_gym.data.repository

import com.example.devmobile_gym.data.mock.MockData
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.TreinoRepository

class TreinoRepositoryMock : TreinoRepository {
    // TODO("Implementar quando tiver um banco")
//    override fun adicionaExercicio(treinoId: Int, exercicioId: Int): Exercicio {
//        var treinoModificado = getTreino(treinoId)
//
//        if (treinoModificado != null) {
////            treinoModificado.exercicios.add()
//
//        }
//
//        return treinoModificado!!.exercicios.last()
//    }

    // TODO("Implementar quando tiver um banco")
//    override fun removeExercicio(treinoId: Int, exercicioId: Int) {
//
//    }

    override fun getTreino(treinoId: Int): Treino? {
        MockData.rotinaMock.treinos.forEach { treino ->
            if (treino.id == treinoId) {
                return treino
            }

        }

        return null
    }
}