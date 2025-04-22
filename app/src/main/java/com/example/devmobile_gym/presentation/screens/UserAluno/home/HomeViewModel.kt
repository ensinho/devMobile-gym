package com.example.devmobile_gym.presentation.screens.UserAluno.home


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.devmobile_gym.data.repository.AlunoRepositoryMock
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.AlunoRepository

class HomeViewModel (
    private val alunoRepository: AlunoRepository = AlunoRepositoryMock()
) : ViewModel() {

    private val _treinos = mutableStateOf<List<Treino>>(emptyList())
    val treinos: State<List<Treino>> = _treinos

    init {
        carregarTreinos()
    }

    private fun carregarTreinos() {
        val aluno = alunoRepository.getAlunoLogado()
        val treinosDaRotina = aluno.rotina?.treinos ?: emptyList()
        _treinos.value = treinosDaRotina
    }

}