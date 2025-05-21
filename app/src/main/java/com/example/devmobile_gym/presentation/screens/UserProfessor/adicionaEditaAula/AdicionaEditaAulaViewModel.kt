package com.example.devmobile_gym.presentation.screens.UserProfessor.adicionaEditaAula

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.AulaRepository
import com.example.devmobile_gym.domain.model.Aula
import com.example.devmobile_gym.domain.repository.AulaRepositoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class AdicionaEditaAulaViewModel() : ViewModel() {


    private val aulasRepository : AulaRepositoryModel = AulaRepository()
    private val _tipoAula = MutableStateFlow("")
    val tipoAula: StateFlow<String> = _tipoAula.asStateFlow()

    private val _professor = MutableStateFlow("")
    val professor: StateFlow<String> = _professor.asStateFlow()

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status.asStateFlow()

    // KAIO LEMBRAR DE TRANSFORMAR ISSO AQUI PARA INT QUANDO FOR POR NO BANCO DE DADOS PELO AMOR DE DEUS
    private val _alocacaoMax = MutableStateFlow("")
    val alocacaoMax: StateFlow<String> = _alocacaoMax.asStateFlow()

    // Novo estado: data e hora da aula
    private val _dataHoraSelecionada = MutableStateFlow<Date?>(null)
    val dataHoraSelecionada: StateFlow<Date?> = _dataHoraSelecionada.asStateFlow()

    fun atualizarDataHora(data: Date) {
        _dataHoraSelecionada.value = data
    }

    fun onProfessorChange(newText: String) {
        _professor.value = newText
    }

    fun onAlocacaoMaxChange(newAlocacaoMax: String) {
        _alocacaoMax.value = newAlocacaoMax
    }

    fun onTipoAulaChange(newSearch: String) {
        _tipoAula.value = newSearch
    }

    fun isNumeroInteiro(valor: String): Boolean {
        val regex = Regex("^\\d+$") // aceita apenas dígitos (0-9)
        return regex.matches(valor)
    }


    fun criarAula(aula: Aula?, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val aulaCriada = aula?.let { aulasRepository.createAula(it) }

            if (aulaCriada == true) {
                _status.value = "Aula criada com sucesso."
                onSuccess()
            } else {
                _status.value = "Erro ao criar a aula."
                onError()
            }
        }

    }

}