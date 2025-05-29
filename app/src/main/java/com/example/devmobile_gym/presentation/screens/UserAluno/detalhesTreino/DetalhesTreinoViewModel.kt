package com.example.devmobile_gym.presentation.screens.UserAluno.detalhesTreino

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update // Adicione esta linha

class DetalhesTreinoViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val treinoRepositoryModel: TreinoRepositoryModel = TreinoRepository()
    private val exerciciosRepository : ExercicioRepositoryModel = ExercicioRepository()
    private val treinoId: String = savedStateHandle.get<String>("treinoId") ?: "-1"

    private val _treinoSelecionado = MutableStateFlow<Treino?>(null)
    val treinoSelecionado: StateFlow<Treino?> = _treinoSelecionado

    private val _nomesExercicios = MutableStateFlow<Map<String, String>>(emptyMap())
    val nomesExercicios: StateFlow<Map<String, String>> = _nomesExercicios.asStateFlow()

    private val _tempoEmHoras = MutableStateFlow(0)
    val tempoEmHoras: StateFlow<Int> = _tempoEmHoras
    private val _tempoEmMinutos = MutableStateFlow(0)
    val tempoEmMinutos: StateFlow<Int> = _tempoEmMinutos

    private val _seriesConcluidas = MutableStateFlow<MutableMap<String, Boolean>>(mutableMapOf())
    val seriesConcluidas: StateFlow<Map<String, Boolean>> = _seriesConcluidas.asStateFlow()


    private var cronometroJob: Job? = null
    private var emExecucao = false

    fun iniciarCronometro() {
        if (emExecucao) return
        emExecucao = true

        cronometroJob = viewModelScope.launch {
            while (isActive) {
                delay(60000)
                _tempoEmMinutos.value += 1

                if (_tempoEmMinutos.value == 60) {
                    _tempoEmHoras.value += 1
                    _tempoEmMinutos.value = 0
                }

            }
        }
    }

    fun onSerieCheckedChange(exercicioId: String, serieIndex: Int, isChecked: Boolean) {
        val key = "$exercicioId-$serieIndex"
        _seriesConcluidas.update { currentMap ->
            val newMap = currentMap.toMutableMap() // Cria uma cópia mutável
            newMap[key] = isChecked
            newMap
        }
    }
    fun isSerieChecked(exercicioId: String, serieIndex: Int): Boolean {
        val key = "$exercicioId-$serieIndex"
        return _seriesConcluidas.value[key] ?: false // Retorna false se não houver estado
    }

    private fun pausarCronometro() {
        emExecucao = false
        cronometroJob?.cancel()
    }

    fun getTreinoId() : String {
        return (_treinoSelecionado.value?.id ?: "Erro ao receber o ID do treino").toString()
    }

    fun finalizarTreino(): String {
        pausarCronometro()
        return "${_tempoEmHoras.value}h ${_tempoEmMinutos.value}min"
    }


    init {
        carregarTreino()
        iniciarCronometro()
    }

    fun getQuantidadeExercicios() : String {
        return (_treinoSelecionado.value?.exercicios?.size ?: "Erro ao receber a quantidade de exercício").toString()
    }

    private fun carregarTreino() {
        viewModelScope.launch {

            // 1. Carrega todos os exercícios e mapeia nomes por ID
            val todosExercicios = exerciciosRepository.getAllExercicios()
            _nomesExercicios.value = todosExercicios.associateBy({ it.id.toString() }, { it.nome.toString() })

            val treino = treinoRepositoryModel.getTreino(treinoId)
            _treinoSelecionado.value = treino
        }
    }

    fun getNomeExercicio(id: String): String {
        println("Buscando nome para o exercício ID: $id")
        return _nomesExercicios.value[id] ?: "Exercício não encontrado"
    }

    suspend fun getImagemExercicio(exercicioId: String): String? {
        return exerciciosRepository.getExercicioImageUrl(exercicioId) // link da imagem
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val savedStateHandle = extras.createSavedStateHandle()
                return DetalhesTreinoViewModel(
                    savedStateHandle = savedStateHandle
                ) as T
            }
        }
    }
}
