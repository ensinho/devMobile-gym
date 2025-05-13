package com.example.devmobile_gym.presentation.screens.UserAluno.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.AlunoRepository
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel
import kotlinx.coroutines.launch

class ProfileViewModel (
    private val alunoRepositoryModel: AlunoRepositoryModel = AlunoRepository()
): ViewModel()  {


    private val _aluno = mutableStateOf<Aluno?>(null)


    val aluno: State<Aluno?> = _aluno

    init {
        carregarAluno()
    }

    private fun carregarAluno() {
        viewModelScope.launch {
            val aluno = alunoRepositoryModel.getAlunoLogado()
            _aluno.value = aluno
        }
    }


}