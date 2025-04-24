package com.example.devmobile_gym.presentation.screens.UserAluno.AulasFucionais

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.example.devmobile_gym.data.repository.AulaRepositoryMock
import com.example.devmobile_gym.domain.model.Aula
import com.example.devmobile_gym.domain.repository.AulaRepository

class AulasProfessorViewModel(
    private val aulasRepository: AulaRepository = AulaRepositoryMock()
): ViewModel() {

    private val _aulas = mutableStateOf<List<Aula?>>(emptyList())
    val aulas : State<List<Aula?>> = _aulas


    init{
        carregaAulas()
    }

    fun carregaAulas(){
        _aulas.value = aulasRepository.getAula()
    }
}