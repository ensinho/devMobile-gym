package com.example.devmobile_gym.domain.repository

interface AuthRepository {
    suspend fun registerUser(nome: String, email: String, senha: String): Result<Unit>
}