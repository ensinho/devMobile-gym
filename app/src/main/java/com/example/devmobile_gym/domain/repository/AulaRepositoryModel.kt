package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.Aula

interface AulaRepositoryModel {
    suspend fun getAulas():List<Aula?>
    suspend fun createAula(aula: Aula) : Boolean
    suspend fun deleteAula(aulaId: String) : Boolean
    suspend fun incrementQuantAlunos(aulaId: String)
    suspend fun getAula(aulaId: String): Aula?
    suspend fun inserirAluno(aulaID : String, alunoID : String)
}