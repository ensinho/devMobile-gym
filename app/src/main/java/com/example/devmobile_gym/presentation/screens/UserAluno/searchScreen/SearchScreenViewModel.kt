package com.example.devmobile_gym.presentation.screens.UserAluno.searchScreen

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
    var search = mutableStateOf("")
        private set
    private val _exercicios = MutableStateFlow<List<Exercicio>>(emptyList())
    val exercicios : StateFlow<List<Exercicio>> = _exercicios.asStateFlow()

    init {
        viewModelScope.launch {
            getExercicios()
        }
    }

    fun onSearchChange(newSearch: String) {
        search.value = newSearch
    }

    private suspend fun getExercicios() {
        _exercicios.value = repository.getAllExercicios()
    }
}