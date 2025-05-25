package com.example.devmobile_gym.data.repository

import android.util.Log
import com.example.devmobile_gym.domain.model.Aula
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.AulaRepositoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.ktx.toObjects
import java.util.Date

class AulaRepository : AulaRepositoryModel {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val aulasCollection = db.collection("aulas")

    override suspend fun getAulas(): List<Aula> {
        return try {
            val snapshot = aulasCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Aula::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e("AulaRepository", "Erro ao buscar aulas", e)
            emptyList()
        }
    }


    override suspend fun createAula(aula: Aula): Boolean {
        return try {
            val documentRef = aulasCollection.document() // gera referência com ID único
            val aulaComId = aula.copy(id = documentRef.id) // copia aula com ID preenchido
            documentRef.set(aulaComId).await() // salva com ID dentro do próprio objeto
            true
        } catch (e: Exception) {
            Log.e("AulaRepository", "Erro ao criar aula", e)
            false
        }
    }

    override suspend fun deleteAula(aulaId: String) : Boolean{
        return try {
            // 1. Remove o treino da coleção "treinos"
            aulasCollection.document(aulaId).delete().await()
            Log.d("Firestore", "Aula deletada com sucesso")
            true

        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao deletar aula: ${e.message}")
            false
        }
    }

    override suspend fun incrementQuantAlunos(id: String) {
        try {
            val docRef = aulasCollection.document(id)
            val snapshot = docRef.get().await()

            // Obter valores atuais
            val quantAtual = snapshot.getLong("quantidade_alunos") ?: 0
            val quantMaxima = snapshot.getLong("quantidade_maxima_alunos") ?: 15

            // Verificar limite máximo
            if (quantAtual >= quantMaxima) {
                throw Exception("A aula já atingiu o limite máximo de alunos")
            }

            // Atualizar usando operação atômica
            docRef.update("quantidade_alunos", FieldValue.increment(1)).await()

        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao incrementar alunos: ${e.message}")
            throw e // Re-lançar para tratamento posterior
        }
    }

}