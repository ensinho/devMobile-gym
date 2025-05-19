package com.example.devmobile_gym.data.repository

import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AlunoRepository(
    private val auth : FirebaseAuth = FirebaseAuth.getInstance(),
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance(),
) : AlunoRepositoryModel {
    override fun logar(email: String, senha: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun registrar(email: String, senha: String, confirmSenha: String): Aluno? {
        TODO("Not yet implemented")
    }

    override suspend fun getAlunoLogado(): Aluno? {
        val user = auth.currentUser ?: return null
        val documentSnapshot = db.collection("Aluno").document(user.uid).get().await()

        if (!documentSnapshot.exists()) return null
        val aluno = documentSnapshot.toObject(Aluno::class.java)

        return aluno
    }

    suspend fun concluirTreinoParaAluno() {
        val user = auth.currentUser ?: return
        val uid = user.uid
        val alunoRef = db.collection("Aluno").document(uid)

        val snapshot = alunoRef.get().await()
        val aluno = snapshot.toObject(Usuario.Aluno::class.java) ?: return

        val hoje = LocalDate.now()
        val formatador = DateTimeFormatter.ISO_DATE
        val hojeStr = hoje.format(formatador)

        val ultimoDia = aluno.ultimoDiaTreinado?.let { LocalDate.parse(it, formatador) }
        val diasTreinados = aluno.diasTreinados.toMutableSet()

        // Evitar duplicação
        if (!diasTreinados.contains(hojeStr)) {
            diasTreinados.add(hojeStr)
        }

        val novoStreak = when {
            ultimoDia == null -> 1
            ultimoDia == hoje.minusDays(1) -> aluno.streakAtual + 1
            ultimoDia == hoje -> aluno.streakAtual // já contou hoje
            else -> 1 // streak quebrado
        }

        val updates = mapOf(
            "streakAtual" to novoStreak,
            "ultimoDiaTreinado" to hojeStr,
            "diasTreinados" to diasTreinados.toList()
        )

        alunoRef.update(updates).await()
    }

}