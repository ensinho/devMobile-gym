package com.example.devmobile_gym.data.repository

import android.util.Log
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TreinoRepository(
    private val auth : FirebaseAuth = FirebaseAuth.getInstance(),
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance(),
) : TreinoRepositoryModel {
    // TODO("Implementar quando tiver um banco")
//    override fun adicionaExercicio(treinoId: Int, exercicioId: Int): Exercicio {
//        var treinoModificado = getTreino(treinoId)
//
//        if (treinoModificado != null) {
////            treinoModificado.exercicios.add()
//
//        }
//
//        return treinoModificado!!.exercicios.last()
//    }

    // TODO("Implementar quando tiver um banco")
//    override fun removeExercicio(treinoId: Int, exercicioId: Int) {
//
//    }

    override suspend fun getTreino(treinoId: String): Treino? {
        val user = auth.currentUser ?: return null
        val documentSnapshot = db.collection("Aluno").document(user.uid).get().await()

        if (!documentSnapshot.exists()) return null
        val aluno = documentSnapshot.toObject(Aluno::class.java)
        return aluno?.rotina?.treinos?.find { it.id == treinoId }
    }

    override suspend fun getTreinos(): List<Treino> {
        val user = auth.currentUser ?: return emptyList()
        return try {
            val documentSnapshot = db.collection("Aluno").document(user.uid).get().await()
            if (!documentSnapshot.exists()) return emptyList()

            val aluno = documentSnapshot.toObject(Aluno::class.java)
            aluno?.rotina?.treinos ?: emptyList()
        } catch (e: Exception) {
            Log.e("TreinoRepositoryMock", "Erro ao buscar treinos", e)
            emptyList()
        }
    }
}