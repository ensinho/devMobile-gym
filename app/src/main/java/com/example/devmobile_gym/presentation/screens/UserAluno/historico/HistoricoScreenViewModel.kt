package com.example.devmobile_gym.presentation.screens.UserAluno.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.AlunoRepository
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.model.TreinoComData
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoricoScreenViewModel () : ViewModel() {

    private val alunoRepository : AlunoRepositoryModel = AlunoRepository()
    private val exerciciosRepository : ExercicioRepositoryModel = ExercicioRepository()

    private val _treinos = MutableStateFlow<List<TreinoComData>>(emptyList())
    val treinos: StateFlow<List<TreinoComData>> = _treinos.asStateFlow()

    private val _nomesExercicios = MutableStateFlow<Map<String, String>>(emptyMap())

    init {
        carregarHistorico()
    }

    private fun carregarHistorico() {
        viewModelScope.launch {
            // 1. Carrega todos os exercícios e mapeia nomes por ID
            val todosExercicios = exerciciosRepository.getAllExercicios()
            _nomesExercicios.value = todosExercicios.associateBy({ it.id }, { it.nome })

            val listaTreinos = alunoRepository.getHistory()
            _treinos.value = listaTreinos
        }
    }

    fun getNomeExercicio(id: String): String {
        println("Buscando nome para o exercício ID: $id")
        return _nomesExercicios.value[id] ?: "Exercício não encontrado"
    }
}