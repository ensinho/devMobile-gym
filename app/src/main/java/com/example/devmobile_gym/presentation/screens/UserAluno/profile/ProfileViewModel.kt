package com.example.devmobile_gym.presentation.screens.UserAluno.profile

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.AlunoRepository
import com.example.devmobile_gym.data.repository.UserWorkoutsRepository
import com.example.devmobile_gym.domain.model.UserWorkouts
import com.example.devmobile_gym.domain.model.Usuario.Aluno
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel
import com.example.devmobile_gym.domain.repository.UserWorkoutsRepositoryModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.log

class ProfileViewModel (): ViewModel()  {

    private val alunoRepositoryModel: AlunoRepositoryModel = AlunoRepository()
    private val userWorkoutsRepository : UserWorkoutsRepositoryModel = UserWorkoutsRepository()

    private val _aluno = MutableStateFlow<Aluno?>(null)
    val aluno: StateFlow<Aluno?> = _aluno

    private val _currentUserWorkouts = MutableStateFlow<UserWorkouts?>(null)
    val currentUserWorkouts: StateFlow<UserWorkouts?> = _currentUserWorkouts.asStateFlow()

    init {
        carregarAluno()
    }

    private fun carregarAluno() {

        viewModelScope.launch {
            val aluno = alunoRepositoryModel.getAlunoLogado()
            println("🔥 Aluno carregado: $aluno")
            _aluno.value = aluno
        }
    }

    fun loadCurrentUserWorkouts(userId: String) {
        viewModelScope.launch {
            try {
                val workouts = userWorkoutsRepository.obterStreakPorUserId(userId)
                _currentUserWorkouts.value = workouts // Atualiza o StateFlow
            } catch (e: Exception) {
                // Lidar com o erro, talvez registrar ou exibir um Toast
                Log.e("HomeViewModel", "Erro ao carregar UserWorkouts: ${e.message}", e)
                _currentUserWorkouts.value = null // Opcional: definir como null em caso de erro
            }
        }
    }


}