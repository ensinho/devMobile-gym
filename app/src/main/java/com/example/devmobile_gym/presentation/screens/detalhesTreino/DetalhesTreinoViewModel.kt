package com.example.devmobile_gym.presentation.screens.detalhesTreino

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.devmobile_gym.data.repository.AlunoRepositoryMock
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.AlunoRepository

class DetalhesTreinoViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: AlunoRepository = AlunoRepositoryMock()
) : ViewModel() {

    private val treinoId: Int = savedStateHandle.get<Int>("treinoId") ?: -1

    var treinoSelecionado by mutableStateOf<Treino?>(null)
        private set

    init {
        carregarTreino()
    }

    private fun carregarTreino() {
        val aluno = repository.getAlunoLogado()
        treinoSelecionado = aluno.rotina?.treinos?.find { it.id == treinoId }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val savedStateHandle = extras.createSavedStateHandle()
                return DetalhesTreinoViewModel(
                    savedStateHandle = savedStateHandle,
                    repository = AlunoRepositoryMock()
                ) as T
            }
        }
    }
}
