package com.example.devmobile_gym.presentation.screens.UserProfessor.criarTreino

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.domain.model.Treino
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CriarTreinoViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: ExercicioRepositoryModel = ExercicioRepository()
) : ViewModel() {

    private val _titulo = MutableStateFlow("")
    val titulo: StateFlow<String> = _titulo.asStateFlow()

    private val _search = MutableStateFlow("")
    val search: StateFlow<String> = _search.asStateFlow()

    private val _todosExercicios = repository.getAllExercicios()

    private val _exercicioos = MutableStateFlow<List<Exercicio>>(emptyList())
    val exercicioos: StateFlow<List<Exercicio>> = _exercicioos.asStateFlow()

    private val _exerciciosFiltrados = mutableStateOf(_todosExercicios)
    val exerciciosFiltrados: State<List<Exercicio>> = _exerciciosFiltrados

    fun onSearchChange(novoTexto: String) {
        _search.value = novoTexto
        filtrarExercicios(novoTexto)
    }

    fun onTituloChange(novoTitulo: String) {
        _titulo.value = novoTitulo
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
                return CriarTreinoViewModel(savedStateHandle) as T
            }
        }
    }
}
