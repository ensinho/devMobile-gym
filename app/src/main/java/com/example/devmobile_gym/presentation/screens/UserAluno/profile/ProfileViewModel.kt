package com.example.devmobile_gym.presentation.screens.UserAluno.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.devmobile_gym.data.repository.AlunoRepositoryMock
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.repository.AlunoRepository

class ProfileViewModel (
    private val alunoRepository: AlunoRepository = AlunoRepositoryMock()
): ViewModel()  {


    private val _aluno = mutableStateOf<Aluno?>(null)


    val aluno: State<Aluno?> = _aluno

    init {
        carregarAluno()
    }

    private fun carregarAluno() {
        val aluno = alunoRepository.getAlunoLogado()
        _aluno.value = aluno
    }


}