//package com.example.devmobile_gym.data.repository
//
//import android.util.Log
//import com.example.devmobile_gym.data.mock.MockData
//import com.example.devmobile_gym.domain.model.Aluno
//import com.example.devmobile_gym.domain.model.Professor
//import com.example.devmobile_gym.domain.repository.ProfessorRepositoryModel
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.tasks.await
//
//class ProfessorRepository(
//    private val auth : FirebaseAuth = FirebaseAuth.getInstance(),
//    private val db : FirebaseFirestore = FirebaseFirestore.getInstance(),
//) : ProfessorRepositoryModel {
//
//    override fun getProfessorLogado(): Professor {
//        return MockData.professorMock
//    }
//
//    override fun getAlunos(): MutableList<Aluno>? {
//        return MockData.usuarios ?: null
//    }
//
//    override suspend fun getAlunoById(alunoId: String): Aluno? {
//        return try {
//            val documentSnapshot = db.collection("Aluno").document(alunoId).get().await()
//            if (documentSnapshot.exists()) {
//                documentSnapshot.toObject(Aluno::class.java)
//            } else {
//                null
//            }
//        } catch (e: Exception) {
//            Log.e("AlunoRepository", "Erro ao buscar aluno: ${e.message}", e)
//            null
//        }
//    }
//
//}