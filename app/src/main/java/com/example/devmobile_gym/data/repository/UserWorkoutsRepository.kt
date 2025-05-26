package com.example.devmobile_gym.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.devmobile_gym.domain.model.UserWorkouts
import com.example.devmobile_gym.domain.repository.UserWorkoutsRepositoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class UserWorkoutsRepository(
    private val auth : FirebaseAuth = FirebaseAuth.getInstance(),
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
) : UserWorkoutsRepositoryModel {
    private val userWorkoutsCollection = db.collection("userWorkouts")

    // funcao que deve ser utilizada no registro do aluno
    override suspend fun criarStreak(userId: String) {
        val existing = userWorkoutsCollection
            .whereEqualTo("userId", userId)
            .get()
            .await()

        if (!existing.isEmpty) {
            Log.w("Firestore", "Streak já existe para esse aluno.")
            return
        }

        val novoDocRef = userWorkoutsCollection.document()

        // inicializa um streak zerado e atribui para o aluno que criou a conta
        val userWorkouts = UserWorkouts(
            id = novoDocRef.id,
            userId = userId,
            workoutDates = emptyList(),
            currentStreak = 0,
            maxStreak = 0,
            lastWorkoutDate = null
        )

        try {
            novoDocRef.set(userWorkouts).await()
            Log.d("Firestore", "Streak adicionado ao aluno com sucesso.")
        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao adicionar streak ao aluno: ", e)
        }
    }

    override suspend fun obterStreakPorUserId(userId: String): UserWorkouts? {
        return try {
            // busca o streak
            val querySnapshot = userWorkoutsCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()

            // verifica se o streak existe
            if (querySnapshot.isEmpty) {
                Log.w("Firestore", "Nenhum streak encontrado para userId: $userId")
                null
            } else {
                // constroi um objeto UserWorkouts com os dados do streak e o retorna
                val doc = querySnapshot.documents.first()
                doc.toObject(UserWorkouts::class.java)?.copy(id = doc.id)
            }

        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao buscar streak do usuário: ", e)
            null
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun atualizarStreakDoAluno(userWorkouts: UserWorkouts) {
        try {
            val querySnapshot = userWorkoutsCollection
                .whereEqualTo("userId", userWorkouts.userId)
                .whereEqualTo("id", userWorkouts.id)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                Log.w("Firestore", "Nenhum streak encontrado para atualização. userId: ${userWorkouts.userId}, id: ${userWorkouts.id}")
                return
            }

            val docRef = querySnapshot.documents.first().reference

            docRef.set(userWorkouts).await()
            Log.d("Firestore", "Streak atualizado com sucesso para userId: ${userWorkouts.userId}")

        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao atualizar streak do aluno: ", e)
        }
    }



}