package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.Aluno

interface AlunoRepository {
    fun logar(email: String, senha: String): Aluno?
    fun registrar(email: String, senha: String, confirmSenha: String): Aluno?
    fun getAlunoLogado(): Aluno
}
