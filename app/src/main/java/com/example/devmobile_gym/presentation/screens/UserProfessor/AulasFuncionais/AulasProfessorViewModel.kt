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
import java.text.SimpleDateFormat
import java.util.Locale

class AulasProfessorViewModel(
    private val aulasRepository: AulaRepositoryModel = AulaRepository()
): ViewModel() {

    private val _aulas = MutableStateFlow<List<Aula?>>(emptyList())
    val aulas : StateFlow<List<Aula?>> = _aulas.asStateFlow()

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status.asStateFlow()


    init{
        viewModelScope.launch {
            carregaAulas()
        }
    }

    suspend fun carregaAulas(){
        _aulas.value = aulasRepository.getAulas()
    }

    fun deleteAula(aula: Aula, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val aulaDeletada = aulasRepository.deleteAula(aula.id)

            if (aulaDeletada) {
                _status.value = "Aula deletada com sucesso."
                carregaAulas()
                onSuccess()
            } else {
                _status.value = "Erro ao deletar a aula."
                onError()
            }
        }
    }
}