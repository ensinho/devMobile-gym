package com.example.devmobile_gym.data.repository

import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {
    override suspend fun registerUser(nome: String, email: String, senha: String): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, senha).await()
            val uid = result.user?.uid ?: throw Exception("Erro ao obter UID")


            val user = Aluno(userId = uid, nome = nome, email = email, senha = senha)

            firestore.collection("users").document(uid).set(user).await()
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
