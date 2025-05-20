package com.example.devmobile_gym.presentation.screens.UserProfessor.gerenciaAluno

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.model.Usuario
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.example.devmobile_gym.domain.repository.ProfessorRepositoryModel
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GerenciaAlunoViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val db = FirebaseFirestore.getInstance()
    private val alunoId: String = savedStateHandle["uid"] ?: throw IllegalArgumentException("UID não encontrado")

    private val treinoRepository: TreinoRepositoryModel = TreinoRepository()
    private val exerciciosRepository : ExercicioRepositoryModel = ExercicioRepository()

    private val _alunoSelecionado = MutableStateFlow<Usuario.Aluno?>(null)
    val alunoSelecionado: StateFlow<Usuario.Aluno?> = _alunoSelecionado

    private val _treinos = mutableStateOf<List<Treino?>>(emptyList())
    val treinos: State<List<Treino?>> = _treinos

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _nomesExercicios = MutableStateFlow<Map<String, String>>(emptyMap())
    val nomesExercicios: StateFlow<Map<String, String>> = _nomesExercicios.asStateFlow()

    init {
        viewModelScope.launch {
            loadData()
        }
    }

    private suspend fun loadData() {
        try {
            val todos = exerciciosRepository.getAllExercicios()
            _nomesExercicios.value = todos.associateBy({ it.id }, { it.nome })

            _isLoading.value = true
            val aluno = getAlunoById(alunoId)
            _alunoSelecionado.value = aluno

            if (!aluno?.rotina.isNullOrEmpty()) {
                val treinosCarregados = treinoRepository.getTreinosByIds(aluno.rotina)
                _treinos.value = treinosCarregados
            } else {
                _treinos.value = emptyList()
            }

        } catch (e: Exception) {
            _error.value = "Erro ao carregar dados: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }


    private suspend fun getAlunoById(uid: String): Usuario.Aluno? {
        val snap = db.collection("alunos").document(uid).get().await()
        return snap.toObject(Usuario.Aluno::class.java)
    }

    fun getNomeExercicio(id: String): String {
        return _nomesExercicios.value[id] ?: "Exercício não encontrado"
    }


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val savedStateHandle = extras.createSavedStateHandle()
                return GerenciaAlunoViewModel(
                    savedStateHandle = savedStateHandle
                ) as T
            }
        }
    }
}