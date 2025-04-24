package com.example.devmobile_gym.presentation.screens.UserProfessor.adicionaEditaAula

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AdicionaEditaAulaViewModel() : ViewModel() {

    private val _tipoAula = mutableStateOf("")
    val tipoAula: State<String> = _tipoAula

    private val _horario = mutableStateOf("")
    val horario: State<String> = _horario

    fun onTipoAulaChange(newSearch: String) {
        _tipoAula.value = newSearch
    }

    fun onHorarioChange(newSearch: String) {
        _horario.value = newSearch
    }
}