package com.example.devmobile_gym.presentation.screens.UserProfessor.gerenciaAluno

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.devmobile_gym.data.repository.AlunoRepository
import com.example.devmobile_gym.data.repository.ProfessorRepositoryModelMock
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel
import com.example.devmobile_gym.domain.repository.ProfessorRepositoryModel
import kotlinx.coroutines.launch

class GerenciaAlunoViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val professorRepositoryModel: ProfessorRepositoryModel = ProfessorRepositoryModelMock()

    private val _alunoId: String = savedStateHandle.get<String>("alunoId") ?: "-1"

    var alunoSelecionado by mutableStateOf<Aluno?>(null)
        private set

    private val _treinos = mutableStateOf<List<Treino>>(emptyList())
    val treinos: State<List<Treino>> = _treinos

    init {
        carregarGerenciadorDeAluno()
    }

    private fun carregarGerenciadorDeAluno() {
        viewModelScope.launch {
            val aluno = professorRepositoryModel.getAlunoById(_alunoId)
            alunoSelecionado = aluno

            val treinosDaRotina = aluno?.rotina?.treinos ?: emptyList()
            _treinos.value = treinosDaRotina
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
                return GerenciaAlunoViewModel(
                    savedStateHandle = savedStateHandle
                ) as T
            }
        }
    }
}