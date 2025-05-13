package com.example.devmobile_gym.presentation.screens.detalhesTreino

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConcluiTreinoViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val treinoRepositoryModel: TreinoRepositoryModel = TreinoRepository()

    private val treinoId: String = savedStateHandle.get<String>("treinoId") ?: "-1"

    private val _treinoSelecionado = MutableStateFlow<Treino?>(null)
    val treinoSelecionado: StateFlow<Treino?> = _treinoSelecionado

    init {
        carregarTreino()
    }

    private fun carregarTreino() {
        viewModelScope.launch {
            val treino = treinoRepositoryModel.getTreino(treinoId)
            _treinoSelecionado.value = treino
        }
    }



    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val savedStateHandle = extras.createSavedStateHandle()
                return ConcluiTreinoViewModel(
                    savedStateHandle = savedStateHandle
                ) as T
            }
        }
    }
}
