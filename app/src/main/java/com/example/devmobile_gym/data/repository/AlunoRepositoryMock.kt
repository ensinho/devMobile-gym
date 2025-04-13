package com.example.devmobile_gym.data.repository

import com.example.devmobile_gym.data.mock.MockData
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.repository.AlunoRepository

class AlunoRepositoryMock : AlunoRepository {
    override fun logar(email: String, senha: String): String {
        if (email == MockData.alunoMock.email && senha == MockData.alunoMock.senha) {
            return "Aluno logado com sucesso."
        } else {
            return "Credenciais inválidas."
        }
    }

    override fun registrar(email: String, senha: String, confirmSenha: String): String {
        if (email == MockData.alunoMock.email && senha == confirmSenha) {
            MockData.alunoMock.senha = senha
            return "Aluno registrado com sucesso."
        } else if (email == MockData.alunoMock.email && !senha.equals(confirmSenha)) {
            return "As senhas não coincidem."
        } else {
            return "Erro ao registrar o aluno."
        }
    }

}