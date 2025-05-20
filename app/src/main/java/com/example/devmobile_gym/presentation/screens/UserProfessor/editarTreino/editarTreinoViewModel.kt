package com.example.devmobile_gym.presentation.screens.UserProfessor.editarTreino

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditarTreinoViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val exercicioRepository : ExercicioRepositoryModel = ExercicioRepository()
    private val treinoRepository : TreinoRepositoryModel = TreinoRepository()

    private var _status = MutableStateFlow("")
    val status: StateFlow<String> = _status.asStateFlow()

    private val treinoId: String = savedStateHandle["id"] ?: throw IllegalArgumentException("ID não encontrado")

    private val _search = MutableStateFlow("")
    val search: StateFlow<String> = _search.asStateFlow()

    private val _titulo = MutableStateFlow("")
    val titulo: StateFlow<String> = _titulo.asStateFlow()

    // todos os exercicios do banco
    private val _todosExercicios = MutableStateFlow<List<Exercicio>>(emptyList())

    // filtrados pela busca
    private val _exerciciosFiltrados = mutableStateOf<List<Exercicio>>(emptyList())
    val exerciciosFiltrados: State<List<Exercicio>> = _exerciciosFiltrados

    // exercicios ja adicionados naquele treino
    private val _exerciciosAdicionados = MutableStateFlow<Set<Exercicio>>(emptySet())
    val exerciciosAdicionados: StateFlow<Set<Exercicio>> = _exerciciosAdicionados.asStateFlow()

    init {
        viewModelScope.launch {
            val exercicios = exercicioRepository.getAllExercicios()
            _todosExercicios.value = exercicios
            val adicionados = exercicioRepository.getExerciciosOfTreino(treinoId)
            _exerciciosAdicionados.value = adicionados.toSet()
            // pegar os exercicios do treino
            _exerciciosFiltrados.value = exercicios

            // pegar o titulo do treino
            val treino = treinoRepository.getTreino(treinoId)
            _titulo.value = treino?.nome ?: ""
        }
    }

    fun onSearchChange(novoTexto: String) {
        _search.value = novoTexto
        filtrarExercicios(novoTexto)
    }

    fun contemExercicio(exercicioId : String) : Boolean {
        return exerciciosAdicionados.value.any { it.id == exercicioId }
        // retorna true se pelo menos um exercicio tiver o id de busca
    }

    private fun filtrarExercicios(texto: String) {
        val todos = _todosExercicios.value
        _exerciciosFiltrados.value = if (texto.isBlank()) {
            todos
        } else {
            todos.filter {
                it.nome.contains(texto, ignoreCase = true) ||
                        it.grupoMuscular.contains(texto, ignoreCase = true)
            }
        }
    }

    fun adicionarExercicio(novoExercicio: Exercicio) {
        val conjuntoAtual = _exerciciosAdicionados.value
        if (conjuntoAtual.contains(novoExercicio)) return
        _exerciciosAdicionados.value = conjuntoAtual + novoExercicio
    }

    fun editarTreino(onSuccess: () -> Unit) {
        if (_titulo.value.isBlank()) {
            _status.value = "Por favor, insira um título para o treino"
            return
        }

        if (_exerciciosAdicionados.value.isEmpty()) {
            _status.value = "Por favor, adicione pelo menos um exercício no treino"
            return
        }

        viewModelScope.launch {
            _status.value = "" // limpa mensagens anteriores
            try {
                treinoRepository.updateTreino(
                    treinoId = treinoId,
                    nome = _titulo.value,
                    exercicios = _exerciciosAdicionados.value.map { it.id } // pega só os IDs
                )
                _status.value = "Treino atualizado com sucesso"
                onSuccess()
            } catch (e: Exception) {
                _status.value = "Erro ao atualizar treino: ${e.message}"
            }
        }
    }

    fun onTituloChange(novoTitulo: String) {
        _titulo.value = novoTitulo
    }


    fun deletarTreino(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _status.value = "" // limpa mensagens anteriores
            try {
                treinoRepository.deleteTreino(treinoId)
                _status.value = "Treino deletado com sucesso"
                onSuccess()
            } catch (e: Exception) {
                _status.value = "Erro ao deletar treino: ${e.message}"
            }
        }
    }





    fun removerExercicio(exercicio: Exercicio) {
        _exerciciosAdicionados.value = _exerciciosAdicionados.value - exercicio
    }


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val savedStateHandle = extras.createSavedStateHandle()
                return EditarTreinoViewModel(savedStateHandle) as T
            }
        }
    }
}