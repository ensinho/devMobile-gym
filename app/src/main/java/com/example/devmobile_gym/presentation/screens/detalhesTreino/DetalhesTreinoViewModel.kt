package com.example.devmobile_gym.presentation.screens.detalhesTreino

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.devmobile_gym.data.repository.AlunoRepositoryMock
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.AlunoRepository

class DetalhesTreinoViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: AlunoRepository = AlunoRepositoryMock()
) : ViewModel() {

    // pegar o valor usado de parâmetro para a rota
    private val treinoId: Int = checkNotNull(savedStateHandle["treinoId"])

    var treinoSelecionado by mutableStateOf<Treino?>(null)
        private set

    init {
        carregarTreino()
    }

    private fun carregarTreino() {
        val aluno = repository.getAlunoLogado()
        treinoSelecionado = aluno.rotina?.treinos?.find { it.id == treinoId }
    }
}
