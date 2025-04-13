package com.example.devmobile_gym.data.mock

import com.example.devmobile_gym.*
import com.example.devmobile_gym.domain.model.*

import java.util.UUID

object MockData {

    // Use companion object for cleaner factory methods
    object Factory {

        fun createAluno(
            userId: Int = 1,
            nome: String = "João Silva",
            email: String = "joao@email.com",
            senha: String = "123456",
            rotina: Rotina = createRotina()
        ): Aluno {
            return Aluno(
                userId = userId,
                nome = nome,
                email = email,
                senha = senha,
                rotina = rotina
            )
        }

        fun createProfessor(
            id: Int = 1,
            nome: String = "Ana Paula",
            email: String = "ana.paula@academia.com",
            senha: String = "prof123"
        ): Professor {
            return Professor(
                id = id,
                nome = nome,
                email = email,
                senha = senha
            )
        }

        fun createRotina(
            id: Int = 1,
            nome: String = "Rotina Semana A",
            treinos: MutableList<Treino> = listOf(createTreinoA(), createTreinoB(), createTreinoC()).toMutableList()
        ): Rotina {
            return Rotina(
                id = id,
                nome = nome,
                treinos = treinos
            )
        }

        fun createTreinoA(
            id: Int = 1,
            nome: String = "Lower A - Quadríceps",
            exercicios: MutableList<Exercicio> = listOf(
                createExercicio(1, "Panturrilha em pé (máquina)"),
                createExercicio(2, "Cadeira extensora (máquina)"),
                createExercicio(3, "Agachamento livre")
            ).toMutableList()
        ): Treino {
            return Treino(
                id = id,
                nome = nome,
                exercicios = exercicios
            )
        }

        fun createTreinoB(
            id: Int = 2,
            nome: String = "Lower B - Posterior de Coxa",
            exercicios: MutableList<Exercicio> = listOf(
                createExercicio(4, "Panturrilha sentada (máquina)"),
                createExercicio(5, "Cadeira flexora (máquina)"),
                createExercicio(6, "Agachamento sumô")
            ).toMutableList()
        ): Treino {
            return Treino(
                id = id,
                nome = nome,
                exercicios = exercicios
            )
        }

        fun createTreinoC(
            id: Int = 3,
            nome: String = "Upper A - Peito e Ombro",
            exercicios: MutableList<Exercicio> = listOf(
                createExercicio(7, "Supino inclinado (halteres)"),
                createExercicio(8, "Peck Deck"),
                createExercicio(9, "Tríceps polia")
            ).toMutableList()
        ): Treino {
            return Treino(
                id = id,
                nome = nome,
                exercicios = exercicios
            )
        }

        fun createExercicio(
            id: Int,
            nome: String,
            series: Int = 3,
            repeticoes: Int = 12
        ): Exercicio {
            return Exercicio(
                id = id,
                nome = nome,
                series = series,
                repeticoes = repeticoes
            )
        }
    }

    // Use the factory methods to generate the mock objects
    val alunoMock: Aluno = Factory.createAluno()
    val professorMock: Professor = Factory.createProfessor()
    val rotinaMock: Rotina = Factory.createRotina()
    val treinoA: Treino = Factory.createTreinoA()
    val treinoB: Treino = Factory.createTreinoB()
    val treinoC: Treino = Factory.createTreinoC()
}