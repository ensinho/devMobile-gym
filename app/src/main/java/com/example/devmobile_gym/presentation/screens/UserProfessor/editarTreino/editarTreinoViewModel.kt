package com.example.devmobile_gym.presentation.screens.UserProfessor.editarTreino

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.devmobile_gym.data.repository.ExercicioRepositoryMock
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.ExercicioRepository

class EditarTreinoViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: ExercicioRepository = ExercicioRepositoryMock()
) : ViewModel() {

    private val _search = mutableStateOf("")
    val search: State<String> = _search

    private val _todosExercicios = repository.getAllExercicios()

    private val _exerciciosFiltrados = mutableStateOf(_todosExercicios)
    val exerciciosFiltrados: State<List<Exercicio>> = _exerciciosFiltrados

    fun onSearchChange(novoTexto: String) {
        _search.value = novoTexto
        filtrarExercicios(novoTexto)
    }

    private fun filtrarExercicios(texto: String) {
        _exerciciosFiltrados.value = if (texto.isBlank()) {
            _todosExercicios
        } else {
            _todosExercicios.filter {
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