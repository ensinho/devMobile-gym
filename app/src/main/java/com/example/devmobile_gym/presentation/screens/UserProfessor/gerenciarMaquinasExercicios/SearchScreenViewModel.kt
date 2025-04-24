package com.example.devmobile_gym.presentation.screens.UserProfessor.gerenciarMaquinasExercicios

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.devmobile_gym.data.repository.AlunoRepositoryMock
import com.example.devmobile_gym.data.repository.ExercicioRepositoryMock
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.AlunoRepository
import com.example.devmobile_gym.domain.repository.ExercicioRepository

class GerenciarMaquinasExerciciosViewModel (
    private val repository: ExercicioRepository = ExercicioRepositoryMock()
) : ViewModel(){
    private val _search = mutableStateOf("")
    val search: State<String> = _search

    private val _todasMaqExerc = repository.getAllExercicios()
    private val _MaqExercFiltrados = mutableStateOf(_todasMaqExerc)
    val MaqExercFiltrados: State<List<Exercicio>> = _MaqExercFiltrados

    fun onSearchChange(newSearch: String) {
        _search.value = newSearch
        filtrarMaqExerc(_search.value)
    }

    private fun filtrarMaqExerc(texto: String) {
        _MaqExercFiltrados.value = if (texto.isBlank()) {
                _todasMaqExerc
            } else {
                _todasMaqExerc.filter {
                    it.nome.contains(texto, ignoreCase = true) ||
                            it.grupoMuscular.contains(texto, ignoreCase = true)
                }
            }
    }
}
