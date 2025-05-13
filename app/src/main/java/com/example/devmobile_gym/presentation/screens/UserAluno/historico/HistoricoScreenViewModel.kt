package com.example.devmobile_gym.presentation.screens.UserAluno.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoricoScreenViewModel () : ViewModel() {

    // POR ENQUANTO TÁ FAZENDO A MESMA COISA QUE O HOME
    private val treinoRepositoryModel: TreinoRepositoryModel = TreinoRepository()

    private val _treinos = MutableStateFlow<List<Treino>>(emptyList())
    val treinos: StateFlow<List<Treino>> = _treinos

    init {
        carregarTreinos()
    }

    private fun carregarTreinos() {
        viewModelScope.launch {
            val listaTreinos = treinoRepositoryModel.getTreinos()
            _treinos.value = listaTreinos
        }
    }
}