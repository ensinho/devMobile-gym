package com.example.devmobile_gym.presentation.screens.UserProfessor.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.devmobile_gym.data.repository.ProfessorRepositoryModelMock
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.repository.ProfessorRepositoryModel

class ProfessorHomeViewModel (
    private val professorRepositoryModel: ProfessorRepositoryModel = ProfessorRepositoryModelMock()
) : ViewModel() {

    private val _alunos = mutableStateOf<List<Aluno>>(emptyList())
    val alunos: State<List<Aluno>> = _alunos

    init {
        carregarAlunos()
    }

    private fun carregarAlunos() {
        val listaAlunos = professorRepositoryModel.getAlunos() ?: emptyList()
        _alunos.value = listaAlunos

    }
}