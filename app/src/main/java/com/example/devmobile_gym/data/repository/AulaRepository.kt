package com.example.devmobile_gym.data.repository

import com.example.devmobile_gym.data.mock.MockData
import com.example.devmobile_gym.domain.model.Aula
import com.example.devmobile_gym.domain.repository.AulaRepositoryModel

class AulaRepository : AulaRepositoryModel {
    override fun getAula(): List<Aula?> {
        return MockData.aulasMock
    }

}