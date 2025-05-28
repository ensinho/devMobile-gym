package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.Usuario.Aluno
import com.example.devmobile_gym.domain.model.Professor

interface ProfessorRepositoryModel {
    fun getProfessorLogado(): Professor
    fun getAlunos(): MutableList<Aluno>?
    suspend fun getAlunoById(alunoId: String): Aluno?
}