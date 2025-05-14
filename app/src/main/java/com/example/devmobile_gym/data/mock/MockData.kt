package com.example.devmobile_gym.data.mock

import com.example.devmobile_gym.domain.model.*

object MockData {

    // Use companion object for cleaner factory methods
    object Factory {

        fun createAluno(
            userId: String = "1",
            nome: String = "João Silva",
            email: String = "joao@email.com",
            senha: String = "123456",
            rotina: Rotina = createRotina()
        ): Aluno {
            return Aluno(
                uid = userId,
                nome = nome,
                email = email,
                rotina = rotina
            )
        }

        fun createProfessor(
            id: String = "1",
            nome: String = "Ana Paula",
            email: String = "adm",
            senha: String = "prof123"
        ): Professor {
            return Professor(
                uid = id,
                nome = nome,
                email = email,
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
            id: String = "1",
            nome: String = "Lower A - Quadríceps",
            exercicios: MutableList<Exercicio> = listOf(
                createExercicio(id = 1, nome = "Panturrilha em pé (máquina)", peso = 180, series = 3, repeticoes = 12, grupoMuscular = "Panturrilha"),
                createExercicio(id = 2, nome = "Cadeira extensora (máquina)", peso = 125, series = 3, repeticoes = 12, grupoMuscular = "Quadríceps"),
                createExercicio(id = 3, nome = "Agachamento livre", peso = 55, series = 3, repeticoes = 12, grupoMuscular = "Quadríceps"),
                createExercicio(id = 10, nome = "Agachamento Smith", peso = 55, series = 3, repeticoes = 12, grupoMuscular = "Quadríceps")
            ).toMutableList()
        ): Treino {
            return Treino(
                id = id,
                nome = nome,
                exercicios = exercicios
            )
        }

        fun createTreinoB(
            id: String = "2",
            nome: String = "Lower B - Posterior de Coxa",
            exercicios: MutableList<Exercicio> = listOf(
                createExercicio(id = 4, nome = "Panturrilha sentada (máquina)", peso = 80, series = 3, repeticoes = 12, grupoMuscular = "Panturrilha"),
                createExercicio(id = 5, nome = "Cadeira flexora (máquina)", peso = 100, series = 3, repeticoes = 12, grupoMuscular = "Posterior de Coxa"),
                createExercicio(id = 6, nome = "Agachamento sumô", peso = 120, series = 3, repeticoes = 12, grupoMuscular = "Posterior de Coxa")
            ).toMutableList()
        ): Treino {
            return Treino(
                id = id,
                nome = nome,
                exercicios = exercicios
            )
        }

        fun createTreinoC(
            id: String = "3",
            nome: String = "Upper A - Peito e Ombro",
            exercicios: MutableList<Exercicio> = listOf(
                createExercicio(id = 7, nome = "Supino inclinado (halteres)", peso = 25, series = 3, repeticoes = 12, grupoMuscular = "Peitoral Superior"),
                createExercicio(id = 8, nome = "Peck Deck", peso = 60, series = 3, repeticoes = 12, grupoMuscular = "Peitoral Superior"),
                createExercicio(id = 9, nome = "Tríceps polia", peso = 60, series = 3, repeticoes = 12, grupoMuscular = "Tríceps Braquial")
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
            peso: Int,
            grupoMuscular: String
        ): Exercicio {
            return Exercicio(
                id = id,
                nome = nome,
                series = series,
                repeticoes = repeticoes,
                peso = peso,
                grupoMuscular = grupoMuscular
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
                    alocacaoMaxima = 15,
                    hora = "19h",
                    tipoAula = "Boxe"
                )
            }
        }
    }



    // Use the factory methods to generate the mock objects
    val alunoMock: Aluno = Factory.createAluno()
    var usuarios: MutableList<Aluno> = mutableListOf(alunoMock)
    val professorMock: Professor = Factory.createProfessor()
    val professores: MutableList<Professor> = mutableListOf(professorMock)
    val rotinaMock: Rotina = Factory.createRotina()
    val treinoA: Treino = Factory.createTreinoA()
    val treinoB: Treino = Factory.createTreinoB()
    val treinoC: Treino = Factory.createTreinoC()
    val aulasMock: List<Aula> = Factory.createAulas()
}