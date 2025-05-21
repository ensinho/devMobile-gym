package com.example.devmobile_gym.presentation.screens.UserAluno.AulasFucionais

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.AulaRepository
import com.example.devmobile_gym.domain.model.Aula
import com.example.devmobile_gym.domain.repository.AulaRepositoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AulasViewModel(
    private val aulasRepository: AulaRepositoryModel = AulaRepository()
): ViewModel() {

    private val _aulas = MutableStateFlow<List<Aula?>>(emptyList())
    val aulas : StateFlow<List<Aula?>> = _aulas.asStateFlow()


    init{
        viewModelScope.launch {
            carregaAulas()
        }
    }

    suspend fun carregaAulas(){
        _aulas.value = aulasRepository.getAulas()
    }
}