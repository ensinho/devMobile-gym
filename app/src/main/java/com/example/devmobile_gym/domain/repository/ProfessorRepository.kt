package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.Professor

interface ProfessorRepository {
    fun logar(email: String, senha: String): Boolean
    fun getProfessorLogado(): Professor
}