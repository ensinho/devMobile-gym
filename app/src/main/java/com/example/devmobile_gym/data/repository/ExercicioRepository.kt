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


    override suspend fun getAllExercicios(): List<Exercicio> {
        val exercicios = MutableStateFlow<List<Exercicio>>(emptyList())
        db.collection("exercicios")
            .get()
            .addOnSuccessListener { result ->
                val listaExercicios = result.map { document ->
                    Exercicio(
                        id = document.id,
                        nome = document.getString("nome") ?: "",
                        grupoMuscular = document.getString("grupoMuscular") ?: "",
                        imagem = document.getString("imagem") ?: ""
                    )
                }
                exercicios.value = listaExercicios
            }
            .addOnFailureListener {
                Log.e("ExercicioRepository", "Erro ao buscar exercícios", it)
            }
            .await()

        return exercicios.value
    }

    override suspend fun insertExercicio(exercicio: Exercicio) {
        try {
            exerciciosCollection.add(exercicio).await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getExerciciosOfTreino(treinoId: String): List<Exercicio> {
        val treino = treinoRepository.getTreino(treinoId) ?: return emptyList()
        val ids = treino.exercicios.map { it }
        return getExerciciosByIds(ids)
    }

}
