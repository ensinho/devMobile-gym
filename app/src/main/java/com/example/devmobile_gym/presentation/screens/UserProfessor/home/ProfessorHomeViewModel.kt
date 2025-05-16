package com.example.devmobile_gym.presentation.screens.UserProfessor.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.devmobile_gym.data.repository.ProfessorRepository
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.model.Rotina
import com.example.devmobile_gym.domain.repository.ProfessorRepositoryModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class ProfessorHomeViewModel (
) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _alunos = MutableStateFlow<List<Aluno>>(emptyList())
    val alunos: StateFlow<List<Aluno>> = _alunos


    suspend fun fetchAlunos() {
        try {
            val colecaoAlunos = db
                .collection("alunos")
                .get()
                .await()

            val listaAlunos = colecaoAlunos.documents.mapNotNull { doc ->
                // TODO: buscar a rotina do aluno
//                val rotinaRef = doc.reference.collection("rotina").document("dados")
//                val rotinaSnapshot = rotinaRef.get().await()

                Aluno(
                    uid = doc.id,
                    nome = doc.getString("nome") ?: "",
                    email = doc.getString("email") ?: "",
//                    rotina = rotinaSnapshot.toObject(Rotina::class.java)
                )
            }

            _alunos.value = listaAlunos
        } catch(e: Exception) {
            // Trate erros (ex: log, mostrar mensagem)
            Log.e("Firestore", "Erro ao buscar usuários", e)
        }
    }
}