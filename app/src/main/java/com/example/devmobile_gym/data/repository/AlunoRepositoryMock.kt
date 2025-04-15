package com.example.devmobile_gym.data.repository

import com.example.devmobile_gym.data.mock.MockData
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.repository.AlunoRepository

class AlunoRepositoryMock : AlunoRepository {
    override fun logar(email: String, senha: String): Boolean {
        return MockData.usuarios.any { it.email == email && it.senha == senha }
    }

    override fun registrar(email: String, senha: String, confirmSenha: String): Aluno? {
        if (email == MockData.alunoMock.email && senha == confirmSenha) {
            MockData.alunoMock.senha = senha
            return MockData.alunoMock
        } else {
            return null
        }
    }

    override fun getAlunoLogado(): Aluno {
        return MockData.alunoMock
    }

}