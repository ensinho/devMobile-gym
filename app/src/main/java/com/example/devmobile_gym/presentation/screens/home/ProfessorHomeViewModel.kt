package com.example.devmobile_gym.presentation.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.devmobile_gym.data.repository.ProfessorRepositoryMock
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.ProfessorRepository

class ProfessorHomeViewModel (
    private val professorRepository: ProfessorRepository = ProfessorRepositoryMock()
) : ViewModel() {

    private val _alunos = mutableStateOf<List<Aluno>>(emptyList())
    val alunos: State<List<Aluno>> = _alunos

    init {
        carregarAlunos()
    }

    private fun carregarAlunos() {
        val listaAlunos = professorRepository.getAlunos() ?: emptyList()
        _alunos.value = listaAlunos

    }
}