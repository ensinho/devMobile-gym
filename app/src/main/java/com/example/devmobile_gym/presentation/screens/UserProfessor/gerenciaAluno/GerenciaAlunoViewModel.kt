package com.example.devmobile_gym.presentation.screens.UserProfessor.gerenciaAluno

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
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.AlunoRepository
import com.example.devmobile_gym.presentation.screens.UserAluno.detalhesTreino.DetalhesTreinoViewModel

class GerenciaAlunoViewModel(
    savedStateHandle: SavedStateHandle,
    private val alunoRepository: AlunoRepository = AlunoRepositoryMock()
) : ViewModel(){

    private val _alunoId: Int = savedStateHandle.get<Int>("alunoId") ?: -1

    var alunoSelecionado by mutableStateOf<Aluno?>(null)
        private set

    private val _treinos = mutableStateOf<List<Treino>>(emptyList())
    val treinos: State<List<Treino>> = _treinos

    init {
        carregarGerenciadorDeAluno()
    }

    private fun carregarGerenciadorDeAluno() {
        // alterar para buscar o aluno pelo id
        // MockData.usuarios?.find { it.id == treinoId } acho que tem que fazer o map, n sei ao certo
        // na hora a gente ve se ta certo
        val aluno = alunoRepository.getAlunoLogado()
        alunoSelecionado = aluno

        val treinosDaRotina = aluno.rotina?.treinos ?: emptyList()
        _treinos.value = treinosDaRotina
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val savedStateHandle = extras.createSavedStateHandle()
                return GerenciaAlunoViewModel(
                    savedStateHandle = savedStateHandle,
                    alunoRepository = AlunoRepositoryMock()
                ) as T
            }
        }
    }
}