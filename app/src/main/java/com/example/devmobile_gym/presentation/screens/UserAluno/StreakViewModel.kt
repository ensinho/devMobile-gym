package com.example.devmobile_gym.presentation.screens.UserAluno

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.UserWorkoutsRepository
import com.example.devmobile_gym.domain.model.UserWorkouts
import com.example.devmobile_gym.domain.repository.UserWorkoutsRepositoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class StreakViewModel (
    private val repository: UserWorkoutsRepositoryModel = UserWorkoutsRepository()
) : ViewModel() {

    private val _streakUiState = MutableStateFlow<StreakUiState>(StreakUiState.Loading)
    val streakUiState: StateFlow<StreakUiState> = _streakUiState.asStateFlow()

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

    fun getUserStreak(userId: String) {
        viewModelScope.launch {
            // 1. Define o estado como Carregando antes de iniciar a busca
            _streakUiState.value = StreakUiState.Loading
            Log.d("StreakViewModel", "Iniciando busca do streak para userId: $userId")

            try {
                // 2. Chama a função do repositório
                val userWorkouts = repository.obterStreakPorUserId(userId)

                // 3. Verifica o resultado do repositório
                if (userWorkouts != null) {
                    // Sucesso: o streak foi encontrado
                    _streakUiState.value = StreakUiState.Success(userWorkouts)
                    Log.d("StreakViewModel", "Streak do usuário obtido com sucesso: $userWorkouts")
                } else {
                    // Streak não encontrado ou ocorreu um erro silencioso no repositório
                    // O repositório já loga se foi 'não encontrado' ou 'erro', então aqui tratamos como 'não encontrado' para a UI.
                    _streakUiState.value = StreakUiState.NotFound
                    Log.w("StreakViewModel", "Nenhum streak encontrado para userId: $userId (ou erro silencioso no repositório)")
                }
            } catch (e: Exception) {
                // 4. Captura qualquer exceção que possa ter sido lançada (erros inesperados)
                _streakUiState.value = StreakUiState.Error("Não foi possível carregar o streak. ${e.localizedMessage ?: "Erro desconhecido"}")
                Log.e("StreakViewModel", "Erro inesperado ao obter streak do usuário", e)
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

// Em um arquivo separado ou dentro do seu ViewModel, fora da classe
sealed class StreakUiState {
    object Loading : StreakUiState() // Indica que a operação está em andamento
    data class Success(val userWorkouts: UserWorkouts) : StreakUiState() // Sucesso com os dados do streak
    object NotFound : StreakUiState() // O streak não foi encontrado para o usuário
    data class Error(val message: String) : StreakUiState() // Ocorreu um erro
}