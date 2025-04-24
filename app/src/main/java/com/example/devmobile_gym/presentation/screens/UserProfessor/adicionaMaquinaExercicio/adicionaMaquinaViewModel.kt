package com.example.devmobile_gym.presentation.screens.UserProfessor.adicionaMaquinaExercicio

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class adicionaMaquinaViewModel() : ViewModel() {


    private val _nomeMaquina = mutableStateOf("")
    val nomeMaquina: State<String> = _nomeMaquina

    fun onNomeMaquinaChange(newSearch: String) {
        _nomeMaquina.value = newSearch
    }

    private val _numMaquina = mutableStateOf("")
    val numMaquina: State<String> = _numMaquina

    fun onNumMaquinaChange(newSearch: String) {
        _numMaquina.value = newSearch
    }

    private val _ExerciciosPossiveis = mutableStateOf("")
    val ExerciciosPossiveis: State<String> = _ExerciciosPossiveis

    fun onExerciciosPossiveisChange(newSearch: String) {
        _ExerciciosPossiveis.value = newSearch
    }

    private val _isIncluded = mutableStateOf(false)
    val isIncluded: State<Boolean> = _isIncluded

    fun onIsIncludedChange() {
        _isIncluded.value = !_isIncluded.value
    }

}