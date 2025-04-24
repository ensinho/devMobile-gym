package com.example.devmobile_gym.data.repository

import com.example.devmobile_gym.data.mock.MockData
import com.example.devmobile_gym.domain.model.Aula
import com.example.devmobile_gym.domain.repository.AulaRepository

class AulaRepositoryMock : AulaRepository {
    override fun getAula(): List<Aula?> {
        return MockData.aulasMock
    }

}