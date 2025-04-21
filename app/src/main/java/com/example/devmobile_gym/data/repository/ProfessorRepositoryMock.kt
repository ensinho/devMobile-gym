package com.example.devmobile_gym.data.repository

import com.example.devmobile_gym.data.mock.MockData
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.model.Professor
import com.example.devmobile_gym.domain.repository.ProfessorRepository

class ProfessorRepositoryMock : ProfessorRepository {
    override fun logar(email: String, senha: String): Boolean {
        return MockData.professores.any { it.email == email && it.senha == senha }
    }

    override fun getProfessorLogado(): Professor {
        return MockData.professorMock
    }

    override fun getAlunos(): MutableList<Aluno>? {
        return MockData.usuarios ?: null
    }
}