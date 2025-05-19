package com.example.devmobile_gym.data.repository

import android.util.Log
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.model.Exercicio
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

    override fun criarTreino(exercicios: List<Exercicio>, nome: String, alunoId: String) {
        Log.d("Firestore", alunoId)
        val treinosRef = db.collection("treinos")

        // 1. Cria um novo documento com ID gerado automaticamente
        val novoDocRef = treinosRef.document()

        // 2. Cria o objeto treino com o ID gerado
        val treino = Treino(
            id = novoDocRef.id,
            nome = nome,
            exercicios = exercicios.toMutableList()
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
        val user = auth.currentUser ?: return null
        val documentSnapshot = db.collection("alunos").document(user.uid).get().await()

        if (!documentSnapshot.exists()) return null
        val aluno = documentSnapshot.toObject(Aluno::class.java)
        return aluno?.rotina?.treinos?.find { it.id == treinoId }
    }

    override suspend fun getTreinos(): List<Treino> {
        val user = auth.currentUser ?: return emptyList()
        return try {
            val documentSnapshot = db.collection("alunos").document(user.uid).get().await()
            if (!documentSnapshot.exists()) return emptyList()

            val aluno = documentSnapshot.toObject(Aluno::class.java)
            aluno?.rotina?.treinos ?: emptyList()
        } catch (e: Exception) {
            Log.e("TreinoRepositoryMock", "Erro ao buscar treinos", e)
            emptyList()
        }
    }
}