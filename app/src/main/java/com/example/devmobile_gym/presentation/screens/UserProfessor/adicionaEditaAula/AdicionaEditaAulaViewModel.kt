package com.example.devmobile_gym.presentation.screens.UserProfessor.adicionaEditaAula

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.AulaRepository
import com.example.devmobile_gym.domain.model.Aula
import com.example.devmobile_gym.domain.repository.AulaRepositoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date // Importa java.util.Date

class AdicionaEditaAulaViewModel() : ViewModel() {

    private val aulasRepository : AulaRepositoryModel = AulaRepository()

    // Estados para os campos do formulário
    private val _tipoAula = MutableStateFlow("")
    val tipoAula: StateFlow<String> = _tipoAula.asStateFlow()

    private val _professor = MutableStateFlow("")
    val professor: StateFlow<String> = _professor.asStateFlow()

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status.asStateFlow()

    // Estado para a alocação máxima (ainda como String para validação de entrada)
    private val _alocacaoMax = MutableStateFlow("")
    val alocacaoMax: StateFlow<String> = _alocacaoMax.asStateFlow()

    // Estado para a data e hora da aula (usando java.util.Date)
    private val _dataHoraSelecionada = MutableStateFlow<Date?>(null)
    val dataHoraSelecionada: StateFlow<Date?> = _dataHoraSelecionada.asStateFlow()

    // Função para atualizar a data e hora selecionada
    fun atualizarDataHora(data: Date) { // Recebe um Date não nulo do DateTimePickerInput
        _dataHoraSelecionada.value = data
    }

    // Funções para atualizar os outros campos
    fun onProfessorChange(newText: String) {
        _professor.value = newText
    }

    fun onAlocacaoMaxChange(newAlocacaoMax: String) {
        _alocacaoMax.value = newAlocacaoMax
    }

    fun onTipoAulaChange(newSearch: String) {
        _tipoAula.value = newSearch
    }

    // Função de validação para número inteiro
    fun isNumeroInteiro(valor: String): Boolean {
        val regex = Regex("^\\d+$") // aceita apenas dígitos (0-9)
        return regex.matches(valor)
    }

    // Função para criar uma nova aula
    fun criarAula(aula: Aula?, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            // Tenta criar a aula usando o repositório
            val aulaCriada = aula?.let { aulasRepository.createAula(it) }

            if (aulaCriada == true) {
                _status.value = "Aula criada com sucesso."
                onSuccess() // Chama o callback de sucesso
            } else {
                _status.value = "Erro ao criar a aula."
                onError() // Chama o callback de erro
            }
        }
    }
}
