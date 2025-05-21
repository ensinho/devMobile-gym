package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.Aula

interface AulaRepositoryModel {
    suspend fun getAulas():List<Aula?>
}