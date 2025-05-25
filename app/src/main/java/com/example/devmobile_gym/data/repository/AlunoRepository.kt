package com.example.devmobile_gym.data.repository

import android.util.Log
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.model.TreinoComData
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

            val limite = 20

            // Histórico atual como lista de maps
            val historicoAtual = snapshot.get("historico") as? List<Map<String, Any>> ?: emptyList()

            val novoTreino = mapOf(
                "id" to treinoId,
                "data" to com.google.firebase.Timestamp.now()
            )

            val novoHistorico = (
                    if (historicoAtual.size >= limite) historicoAtual.drop(1) else historicoAtual
                    ) + novoTreino

            alunoRef.update("historico", novoHistorico).await()

        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao adicionar treino ao histórico", e)
        }
    }


    override suspend fun getHistory(): List<TreinoComData> {
        val user = auth.currentUser ?: return emptyList()
        val historicoTreinos = mutableListOf<TreinoComData>()

        try {
            val alunoRef = db.collection("alunos").document(user.uid)
            val snapshot = alunoRef.get().await()

            // Lista de objetos com id + data
            val listaTreinosInfo = snapshot.get("historico") as? List<Map<String, Any>> ?: emptyList()

            for (info in listaTreinosInfo) {
                val treinoId = info["id"] as? String ?: continue
                val dataTimestamp = info["data"] as? com.google.firebase.Timestamp ?: continue

                val treinoSnapshot = db.collection("treinos").document(treinoId).get().await()
                val treino = treinoSnapshot.toObject(Treino::class.java)

                if (treino != null) {
                    historicoTreinos.add(TreinoComData(treino, dataTimestamp.toDate()))
                }
            }

            return historicoTreinos

        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao obter histórico", e)
            return emptyList()
        }
    }





}