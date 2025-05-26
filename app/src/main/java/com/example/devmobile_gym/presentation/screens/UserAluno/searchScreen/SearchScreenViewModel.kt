package com.example.devmobile_gym.presentation.screens.UserAluno.searchScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.AlunoRepository
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchScreenViewModel (
    private val repository: ExercicioRepositoryModel = ExercicioRepository()

) : ViewModel(){

    private val _search = MutableStateFlow("")
    val search: StateFlow<String> = _search.asStateFlow()

    private val _exerciciosFiltrados = mutableStateOf<List<Exercicio>>(emptyList())
    val exerciciosFiltrados: State<List<Exercicio>> = _exerciciosFiltrados

    private val _todosExercicios = MutableStateFlow<List<Exercicio>>(emptyList())

    init {
        viewModelScope.launch {
            getExercicios()
        }
    }

    private fun filtrarExercicios(texto: String) {
        val todos = _todosExercicios.value
        _exerciciosFiltrados.value = if (texto.isBlank()) {
            todos
        } else {
            todos.filter {
                it.nome.toString().contains(texto, ignoreCase = true) ||
                        it.grupoMuscular.contains(texto, ignoreCase = true)
            }
        }
    }

    fun onSearchChange(newSearch: String) {
        _search.value = newSearch
        filtrarExercicios(newSearch)
    }

    private suspend fun getExercicios() {
        _todosExercicios.value = repository.getAllExercicios()
        _exerciciosFiltrados.value = _todosExercicios.value
    }
}