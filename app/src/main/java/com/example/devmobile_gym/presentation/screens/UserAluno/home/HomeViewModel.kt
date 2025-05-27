package com.example.devmobile_gym.presentation.screens.UserAluno.home


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.AlunoRepository
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.data.repository.UserWorkoutsRepository
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.model.UserWorkouts
import com.example.devmobile_gym.domain.model.Usuario.Aluno
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import com.example.devmobile_gym.domain.repository.UserWorkoutsRepositoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel (
) : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val treinoRepository: TreinoRepositoryModel = TreinoRepository()
    private val exerciciosRepository: ExercicioRepositoryModel = ExercicioRepository()
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val userWorkoutsRepository: UserWorkoutsRepositoryModel = UserWorkoutsRepository()

    private val _treinos = MutableStateFlow<List<Treino>>(emptyList())
    val treinos: StateFlow<List<Treino>> = _treinos

    private val _nomesExercicios = MutableStateFlow<Map<String, String>>(emptyMap())
    val nomesExercicios: StateFlow<Map<String, String>> = _nomesExercicios.asStateFlow()

    private val _aluno = MutableStateFlow<Aluno?>(null)
    val aluno: StateFlow<Aluno?> = _aluno.asStateFlow()

    private val _currentUserWorkouts = MutableStateFlow<UserWorkouts?>(null)
    val currentUserWorkouts: StateFlow<UserWorkouts?> = _currentUserWorkouts.asStateFlow()


    init {
        loadData()
    }

    fun getNomeExercicio(id: String): String {
        println("Buscando nome para o exercício ID: $id")
        return _nomesExercicios.value[id] ?: "Exercício não encontrado"
    }

    fun getUserId() : String {
        val userId = auth.currentUser?.uid
        Log.d("getUserId", "User ID: $userId")
        return userId ?: throw IllegalStateException("User ID is null. User might not be authenticated.")
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

    fun loadData() {
        viewModelScope.launch {

            // 1. Carrega todos os exercícios e mapeia nomes por ID
            val todosExercicios = exerciciosRepository.getAllExercicios()
            _nomesExercicios.value = todosExercicios.associateBy({ it.id.toString() }, { it.nome.toString() })

            // carrega os treinos
            val listaTreinos = treinoRepository.getTreinos()
            _treinos.value = listaTreinos
            println("Treinos carregados: ${_treinos.value}")


            println("Exercícios carregados: ${_nomesExercicios.value}")
        }
    }
}