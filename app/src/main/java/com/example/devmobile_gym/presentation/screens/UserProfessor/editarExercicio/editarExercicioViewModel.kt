package com.example.devmobile_gym.presentation.screens.UserProfessor.adicionaMaquinaExercicio

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class editarExercicioViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: ExercicioRepositoryModel = ExercicioRepository()
) : ViewModel() {

    private val exercicioID: String = savedStateHandle["MaqExerc.id"] ?: ""

    private val _exercicioSelecionado = mutableStateOf<Exercicio?>(null)
    val exercicioSelecionado: State<Exercicio?> = _exercicioSelecionado

    private val _isIncluded = mutableStateOf(false)
    val isIncluded: State<Boolean> = _isIncluded

    private val _numExercicio = mutableStateOf("")
    val numExercicio: State<String> = _numExercicio

    // -------------
    private val _novoNome = mutableStateOf("")
    val novoNome: MutableState<String> = _novoNome

    private val _novoGrupoMuscular = mutableStateOf("")
    val novoGrupoMuscular: State<String> = _novoGrupoMuscular

    fun onNovoNomeChange(novoNome: String) {
        _novoNome.value = novoNome
    }

    fun onNovoGrupoMuscularChange(novoGrupo: String) {
        _novoGrupoMuscular.value = novoGrupo
    }

//    fun onNumExercicioChange(newSearch: String) {
//        _numExercicio.value = newSearch
//    }

    private val _isSaving = mutableStateOf(false)
    val isSaving: State<Boolean> = _isSaving

    private val _saveSuccess = mutableStateOf(false)
    val saveSuccess: State<Boolean> = _saveSuccess

    fun editarExercicio(newName: String, newGrupoMuscular: String) {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                repository.updateExercicio(exercicioID, newName, newGrupoMuscular)
                _saveSuccess.value = true
            } catch (e: Exception) {
                Log.e("editarExercicioViewModel", "Erro ao atualizar: ${e.message}")
                _saveSuccess.value = false
            }
            _isSaving.value = false
        }
    }

    init {
        Log.d("editarExercicioViewModel", "exercicioID: $exercicioID")
        viewModelScope.launch {
            carregarExercicio()
        }
    }

    private suspend fun carregarExercicio() {
        val exercicio = repository.getExercicio(exercicioID)
        exercicio?.let {
            _exercicioSelecionado.value = it
            _novoNome.value = it.nome
            _novoGrupoMuscular.value = it.grupoMuscular
        }
    }

    fun onIsIncludedChange() {
        _isIncluded.value = !_isIncluded.value
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val savedStateHandle = extras.createSavedStateHandle()
                return editarExercicioViewModel(
                    savedStateHandle = savedStateHandle,
                    repository = ExercicioRepository()
                ) as T
            }
        }
    }
}
