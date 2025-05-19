package com.example.devmobile_gym.data.repository

import android.util.Log
import com.example.devmobile_gym.data.mock.MockData
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await

class ExercicioRepository : ExercicioRepositoryModel {
    private val db = FirebaseFirestore.getInstance()
    private val exerciciosCollection = db.collection("exercicios")
//    override fun getExercicio(exercicioId: Int): Exercicio? {
//        return MockData.rotinaMock.treinos
//            .flatMap { it.exercicios }
//            .find { it.id == exercicioId }
//    }

    override suspend fun getAllExercicios(): List<Exercicio> {
        val exercicios = MutableStateFlow<List<Exercicio>>(emptyList())
        db.collection("exercicios")
            .get()
            .addOnSuccessListener { result ->
                val listaExercicios = result.map { document ->
                    Exercicio(
                        uid = document.id,
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
}
