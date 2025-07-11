package com.example.devmobile_gym.data.repository

import android.util.Log
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import kotlin.collections.mutableListOf

class ExercicioRepository : ExercicioRepositoryModel {
    private val db = FirebaseFirestore.getInstance()
    private val exerciciosCollection = db.collection("exercicios")
    private val treinoRepository : TreinoRepositoryModel = TreinoRepository()
    override suspend fun getExercicio(exercicioId: String): Exercicio? {
        return try {
            val exercicioSnapshot = exerciciosCollection.document(exercicioId).get().await()
            if (!exercicioSnapshot.exists()) return null

            exercicioSnapshot.toObject(Exercicio::class.java)
        } catch (e: Exception) {
            Log.e("TreinoRepository", "Erro ao buscar treino por ID", e)
            null
        }
    }

    override suspend fun getExerciciosByIds(ids: List<String>): List<Exercicio> {
        return try {
            val chunks = ids.chunked(10) // Firestore whereIn tem limite de 10
            val resultados = mutableListOf<Exercicio>()

            for (chunk in chunks) {
                val snapshot = exerciciosCollection
                    .whereIn(FieldPath.documentId(), chunk)
                    .get()
                    .await()

                resultados.addAll(snapshot.toObjects(Exercicio::class.java))
            }

            resultados
        } catch (e: Exception) {
            Log.e("TreinoRepository", "Erro ao buscar exercícios em lote", e)
            emptyList()
        }
    }

    override suspend fun getExercicioImageUrl(id: String): String? {
        return try {
            val exercicioSnapshot = exerciciosCollection.document(id).get().await()
            if (!exercicioSnapshot.exists()) return null

            val exercicio = exercicioSnapshot.toObject(Exercicio::class.java)
            exercicio?.imagem // retorna a url da imagem do exercicio
        } catch (e: Exception) {
            Log.e("TreinoRepository", "Erro ao buscar treino por ID", e)
            null
        }
    }


    override suspend fun getAllExercicios(): List<Exercicio> {
        return try {
            val result = db.collection("exercicios").get().await()
            result.map { document ->
                Exercicio(
                    id = document.id,
                    nome = document.getString("nome") ?: "",
                    grupoMuscular = document.getString("grupoMuscular") ?: "",
                    imagem = document.getString("imagem") ?: "",
                    descricao = document.getString("descricao") ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e("ExercicioRepository", "Erro ao buscar exercícios", e)
            emptyList()
        }
    }


    override suspend fun insertExercicio(exercicio: Exercicio) {
        try {
            val docRef = exerciciosCollection.add(exercicio).await()

            exerciciosCollection.document(docRef.id).update("id", docRef.id).await()

            Log.d("Firestore", "Exercício inserido com ID: ${docRef.id}")
        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao inserir exercício: ${e.message}")
        }
    }

    override suspend fun getExerciciosOfTreino(treinoId: String): List<Exercicio> {
        val treino = treinoRepository.getTreino(treinoId) ?: return emptyList()
        val ids = treino.exercicios.map { it }
        return getExerciciosByIds(ids)
    }

    override suspend fun deletarExercicio(exercicioID: String): Boolean {
        return try {
            val db = FirebaseFirestore.getInstance()
            db.collection("exercicios")
                .document(exercicioID)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    override suspend fun updateExercicio(exercicioID: String,newExercicioName : String, newGrupoMuscular : String){
        try {
            val exercicioMap = mapOf(
                "nome" to newExercicioName,
                "grupoMuscular" to newGrupoMuscular
            )

            exerciciosCollection.document(exercicioID).update(exercicioMap).await()
            Log.d("Firestore", "exercicio atualizado com sucesso")
        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao atualizar exercicio: ${e.message}")
        }


    }

}
