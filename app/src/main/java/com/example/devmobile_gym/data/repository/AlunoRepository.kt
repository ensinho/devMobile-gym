package com.example.devmobile_gym.data.repository

import com.example.devmobile_gym.domain.model.Usuario.Aluno
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AlunoRepository(
    private val auth : FirebaseAuth = FirebaseAuth.getInstance(),
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance(),
) : AlunoRepositoryModel {
    override fun logar(email: String, senha: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun registrar(email: String, senha: String, confirmSenha: String): Aluno? {
        TODO("Not yet implemented")
    }

    override suspend fun getAlunoLogado(): Aluno? {
        val user = auth.currentUser ?: return null
        val documentSnapshot = db.collection("Aluno").document(user.uid).get().await()

        if (!documentSnapshot.exists()) return null
        val aluno = documentSnapshot.toObject(Aluno::class.java)

        return aluno
    }
}