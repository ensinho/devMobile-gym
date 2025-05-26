package com.example.devmobile_gym.data.repository

import android.util.Log
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TreinoRepository(
    private val auth : FirebaseAuth = FirebaseAuth.getInstance(),
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance(),
) : TreinoRepositoryModel {
    private val treinoCollection = db.collection("treinos")

    override fun criarTreino(exercicios: List<String>, nome: String, alunoId: String) {
        Log.d("Firestore", alunoId)
        val treinosRef = db.collection("treinos")

        // 1. Cria um novo documento com ID gerado automaticamente
        val novoDocRef = treinosRef.document()

        // 2. Cria o objeto treino com o ID gerado
        val treino = Treino(
            id = novoDocRef.id,
            nome = nome,
            exercicios = exercicios
        )

        // 3. Salva o treino na coleção "treinos"
        novoDocRef.set(treino)
            .addOnSuccessListener {
                // 4. Atualiza o campo "rotina" do aluno com o ID do novo treino
                val alunoRef = db.collection("alunos").document(alunoId)

                // o campo rotina armazena um array com os ids dos treinos
                // ELE NÃO ARMAZENA OS TREINOS EM SI, APENAS AS SUAS REFERÊNCIAS!
                alunoRef.update("rotina", FieldValue.arrayUnion(treino.id))
                    .addOnSuccessListener {
                        Log.d("Firestore", "Treino adicionado à rotina do aluno com sucesso.")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Erro ao atualizar rotina do aluno: ", e)
                    }

            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erro ao salvar treino: ", e)
            }
    }



    override suspend fun getTreino(treinoId: String): Treino? {
        return try {
            val treinoSnapshot = treinoCollection.document(treinoId).get().await()
            if (!treinoSnapshot.exists()) return null

            treinoSnapshot.toObject(Treino::class.java)
        } catch (e: Exception) {
            Log.e("TreinoRepository", "Erro ao buscar treino por ID", e)
            null
        }
    }


    override suspend fun getTreinos(): List<Treino> {
        val user = auth.currentUser
        Log.d("TreinoRepository", "Usuário atual: ${user?.uid}")
        if (user == null) return emptyList()

        return try {
            val documentSnapshot = db.collection("alunos").document(user.uid).get().await()
            if (!documentSnapshot.exists()) return emptyList()

            val aluno = documentSnapshot.toObject(Aluno::class.java)
            val treinosRef = aluno?.rotina ?: return emptyList()

            val treinos = mutableListOf<Treino>()
            for (treinoId in treinosRef) {
                val treinoSnapshot = treinoCollection.document(treinoId).get().await()
                if (treinoSnapshot.exists()) {
//                    treinoSnapshot.toObject(Treino::class.java)?.let { treinos.add(it) }
                    val treino = treinoSnapshot.toObject(Treino::class.java)
                    if (treino == null) {
                        Log.e("Firestore", "Erro ao converter treino: ${treinoSnapshot.id}")
                    } else {
                        Log.d("Firestore", "Treino convertido: $treino")
                        treinos.add(treino)
                    }

                }
            }

            treinos
        } catch (e: Exception) {
            Log.e("TreinoRepository", "Erro ao buscar treinos", e)
            emptyList()
        }
    }

    override suspend fun getTreinosByIds(ids: List<String>): List<Treino?> {
        val treinos = mutableListOf<Treino?>()
        try {
            for (id in ids) {
                val doc = db.collection("treinos").document(id).get().await()
                if (doc.exists()) {
                    val treino = doc.toObject(Treino::class.java)
                    treino?.let { treinos.add(it) }
                }
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao buscar treinos por IDs: ${e.message}")
        }
        return treinos
    }

    override suspend fun updateTreino(treinoId: String, nome: String, exercicios: List<String>) {
        try {
            val treinoMap = mapOf(
                "nome" to nome,
                "exercicios" to exercicios
            )

            treinoCollection.document(treinoId).update(treinoMap).await()
            Log.d("Firestore", "Treino atualizado com sucesso")
        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao atualizar treino: ${e.message}")
        }
    }


    override suspend fun deleteTreino(treinoId: String) {
        val user = auth.currentUser ?: return

        try {
            // 1. Remove o treino da coleção "treinos"
            treinoCollection.document(treinoId).delete().await()

            // 2. Remove o ID do treino da rotina do aluno
            db.collection("alunos").document(user.uid)
                .update("rotina", FieldValue.arrayRemove(treinoId))
                .await()

            Log.d("Firestore", "Treino deletado com sucesso")
        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao deletar treino: ${e.message}")
        }
    }



}