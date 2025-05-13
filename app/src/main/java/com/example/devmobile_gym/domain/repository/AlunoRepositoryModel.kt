package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.Aluno

interface AlunoRepositoryModel {
    fun logar(email: String, senha: String): Boolean
    fun registrar(email: String, senha: String, confirmSenha: String): Aluno?
    suspend fun getAlunoLogado(): Aluno?
}
