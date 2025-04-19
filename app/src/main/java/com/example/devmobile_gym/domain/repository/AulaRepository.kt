package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.Aula

interface AulaRepository {
    fun getAula():List<Aula?>
}