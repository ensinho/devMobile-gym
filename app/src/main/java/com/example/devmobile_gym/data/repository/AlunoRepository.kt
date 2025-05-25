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

            //  máximo 20 treinos
            val limite = 20
            // Recupera o array atual de histórico (pode ser null)
            val historicoAtual = snapshot.get("historico") as? List<String> ?: emptyList()

            // Se atingir o limite, remove o primeiro antes de adicionar o novo
            val novoHistorico = (
                    if (historicoAtual.size >= limite) historicoAtual.drop(1) else historicoAtual
                    ) + treinoId

            // Atualiza o Firestore com o novo histórico
            alunoRef.update("historico", novoHistorico).await()



        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao adicionar treino ao histórico", e)
        }
    }


}