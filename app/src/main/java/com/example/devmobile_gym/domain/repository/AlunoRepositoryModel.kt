package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.model.Usuario
import com.example.devmobile_gym.domain.model.Usuario.Aluno

interface AlunoRepositoryModel {
    suspend fun getAlunoLogado(): Aluno?
    suspend fun addToHistory(treino: Treino)
    suspend fun getHistory() : List<Treino>
}
