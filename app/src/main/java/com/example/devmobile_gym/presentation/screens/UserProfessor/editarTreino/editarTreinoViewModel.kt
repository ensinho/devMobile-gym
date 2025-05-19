package com.example.devmobile_gym.presentation.screens.UserProfessor.editarTreino

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditarTreinoViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: ExercicioRepositoryModel = ExercicioRepository()
) : ViewModel() {

    private val _search = MutableStateFlow("")
    val search: StateFlow<String> = _search.asStateFlow()

    private val _todosExercicios = MutableStateFlow<List<Exercicio>>(emptyList())

    private val _exerciciosFiltrados = mutableStateOf<List<Exercicio>>(emptyList())
    val exerciciosFiltrados: State<List<Exercicio>> = _exerciciosFiltrados

    init {
        viewModelScope.launch {
            val exercicios = repository.getAllExercicios()
            _todosExercicios.value = exercicios
            _exerciciosFiltrados.value = exercicios
        }
    }

    fun onSearchChange(novoTexto: String) {
        _search.value = novoTexto
        filtrarExercicios(novoTexto)
    }

    private fun filtrarExercicios(texto: String) {
        val todos = _todosExercicios.value
        _exerciciosFiltrados.value = if (texto.isBlank()) {
            todos
        } else {
            todos.filter {
                it.nome.contains(texto, ignoreCase = true) ||
                        it.grupoMuscular.contains(texto, ignoreCase = true)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val savedStateHandle = extras.createSavedStateHandle()
                return EditarTreinoViewModel(savedStateHandle) as T
            }
        }
    }
}