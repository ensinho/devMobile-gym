package com.example.devmobile_gym.presentation.screens.UserAluno

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.UserWorkoutsRepository
import com.example.devmobile_gym.domain.model.UserWorkouts
import com.example.devmobile_gym.domain.repository.UserWorkoutsRepositoryModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class StreakViewModel (
    private val repository: UserWorkoutsRepositoryModel = UserWorkoutsRepository()
) : ViewModel() {


    @RequiresApi(Build.VERSION_CODES.O)
    fun updateStreak(userWorkouts: UserWorkouts) {
        viewModelScope.launch {
            try {
                val workoutDatesLocal = userWorkouts.workoutDates.map { LocalDate.parse(it) }.toMutableList()
                val today = LocalDate.now()

                // Adiciona a data de hoje se ainda não estiver registrada
                if (!workoutDatesLocal.contains(today)) {
                    workoutDatesLocal.add(today)
                }

                // Ordena as datas para consistência
                workoutDatesLocal.sort()

                // Calcula o streak com base em datas consecutivas
                val currentStreak = calcularStreakConsecutivo(workoutDatesLocal, today)
                val maxStreak = maxOf(userWorkouts.maxStreak, currentStreak)

                val updatedUserWorkouts = userWorkouts.copy(
                    workoutDates = workoutDatesLocal.map { it.toString() },
                    currentStreak = currentStreak,
                    maxStreak = maxStreak,
                    lastWorkoutDate = today.toString()
                )

                repository.atualizarStreakDoAluno(updatedUserWorkouts)

            } catch (e: Exception) {
                Log.e("StreakViewModel", "Erro ao atualizar streak", e)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun calcularStreakConsecutivo(workoutDates: List<LocalDate>, today: LocalDate = LocalDate.now()): Int {
        // Coloca as datas em ordem decrescente: da mais recente para a mais antiga.
        val sortedDates = workoutDates.distinct().sortedDescending()

        var streak = 0
        var expectedDate = today

        for (date in sortedDates) { // percorrer cada data da lista, da mais recente para a mais antiga.
            if (date == expectedDate) { // Verifica se a date atual é igual à que esperamos (expectedDate).
                streak++
                expectedDate = expectedDate.minusDays(1) // E esperamos que o próximo dia seja o anterior (expectedDate--)
            } else if (date.isBefore(expectedDate)) {
                // Se a date atual for antes da data esperada, significa que ele pulou um dia
                // → o streak foi quebrado
                // → paramos de contar.
                // Quebrou a sequência
                break
            }
        }

        return streak
    }



}