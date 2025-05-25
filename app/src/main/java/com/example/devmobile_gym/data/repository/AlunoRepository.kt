package com.example.devmobile_gym.data.repository

import android.util.Log
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.model.Usuario.Aluno
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AlunoRepository(
    private val auth : FirebaseAuth = FirebaseAuth.getInstance(),
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance(),
) : AlunoRepositoryModel {

    override suspend fun getAlunoLogado(): Aluno? {
        val user = auth.currentUser ?: return null
        val documentSnapshot = db.collection("alunos").document(user.uid).get().await()

        if (!documentSnapshot.exists()) return null
        val aluno = documentSnapshot.toObject(Aluno::class.java)

        return aluno
    }

    override suspend fun addToHistory(treino: Treino) {
        val user = auth.currentUser ?: return
        val treinoId = treino.id ?: return

        try {
            val alunoRef = db.collection("alunos").document(user.uid)
            val snapshot = alunoRef.get().await()

            // Recupera o array atual de histórico (pode ser null)
            val historicoAtual = snapshot.get("historico") as? List<String> ?: emptyList()

            // Cria nova lista com o treinoId adicionado (permitindo duplicatas)
            val novoHistorico = historicoAtual + treinoId

            // Atualiza o campo "historico" com a nova lista
            alunoRef.update("historico", novoHistorico).await()

        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao adicionar treino ao histórico", e)
        }
    }


}