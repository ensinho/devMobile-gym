package com.example.devmobile_gym.presentation.screens.UserAluno.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
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

    private val _treinos = MutableStateFlow<List<Treino>>(emptyList())
    val treinos: StateFlow<List<Treino>> = _treinos

    private val _nomesExercicios = MutableStateFlow<Map<String, String>>(emptyMap())
    val nomesExercicios: StateFlow<Map<String, String>> = _nomesExercicios.asStateFlow()

    init {
        loadData()
    }

    fun getNomeExercicio(id: String): String {
        return _nomesExercicios.value[id] ?: "Exercício não encontrado"
    }

    private fun loadData() {
        viewModelScope.launch {
            // carrega os treinos
            val listaTreinos = treinoRepository.getTreinos()
            _treinos.value = listaTreinos

            // carrega os nomes dos exercícios para serem colocados nos cards
            val todos = exerciciosRepository.getAllExercicios()
            _nomesExercicios.value = todos.associateBy({ it.id }, { it.nome })
        }
    }
}