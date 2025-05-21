package com.example.devmobile_gym.data.repository

import android.util.Log
import com.example.devmobile_gym.domain.model.Aula
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.AulaRepositoryModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.ktx.toObjects
import java.util.Date

class AulaRepository : AulaRepositoryModel {
    private val db = FirebaseFirestore.getInstance()
    private val aulasCollection = db.collection("aulas")

    override suspend fun getAulas(): List<Aula> {
        return try {
            val snapshot = aulasCollection.get().await()
            snapshot.toObjects(Aula::class.java) // retorna as aulas como uma lista de objetos Aula
        } catch (e: Exception) {
            Log.e("AulaRepository", "Erro ao buscar aulas", e)
            emptyList() // retorna uma lista vazia em caso de erro
        }
    }


}