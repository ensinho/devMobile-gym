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
                createExercicio(id = 1, nome = "Panturrilha em pé (máquina)", peso = 180, series = 3, repeticoes = 12),
                createExercicio(id = 2, nome = "Cadeira extensora (máquina)", peso = 125, series = 3, repeticoes = 12),
                createExercicio(id = 3, nome = "Agachamento livre", peso = 55, series = 3, repeticoes = 12)
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
                createExercicio(id = 4, nome = "Panturrilha sentada (máquina)", peso = 80, series = 3, repeticoes = 12),
                createExercicio(id = 5, nome = "Cadeira flexora (máquina)", peso = 100, series = 3, repeticoes = 12),
                createExercicio(id = 6, nome = "Agachamento sumô", peso = 120, series = 3, repeticoes = 12)
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
                createExercicio(id = 7, nome = "Supino inclinado (halteres)", peso = 25, series = 3, repeticoes = 12),
                createExercicio(id = 8, nome = "Peck Deck", peso = 60, series = 3, repeticoes = 12),
                createExercicio(id = 9, nome = "Tríceps polia", peso = 60, series = 3, repeticoes = 12)
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
            series: Int,
            repeticoes: Int,
            peso: Int
        ): Exercicio {
            return Exercicio(
                id = id,
                nome = nome,
                series = series,
                repeticoes = repeticoes,
                peso = peso
            )
        }

        fun createAulas(
            quantidade: Int = 5,
            aluno: Aluno = createAluno(),
            professor: Professor = createProfessor(),
            treinos: List<Treino> = listOf(createTreinoA(), createTreinoB(), createTreinoC())
        ): List<Aula> {
            return (1..quantidade).map { index ->
                Aula(
                    id = index,
                    professor = professor,
                    data = "13/04", // datas fictícias no mês de abril
                    alocacaoMaxima = 15
                )
            }
        }
    }



    // Use the factory methods to generate the mock objects
    val alunoMock: Aluno = Factory.createAluno()
    var usuarios: MutableList<Aluno> = mutableListOf(alunoMock)
    val professorMock: Professor = Factory.createProfessor()
    val rotinaMock: Rotina = Factory.createRotina()
    val treinoA: Treino = Factory.createTreinoA()
    val treinoB: Treino = Factory.createTreinoB()
    val treinoC: Treino = Factory.createTreinoC()
    val aulasMock: List<Aula> = Factory.createAulas()
}