package com.example.devmobile_gym.presentation.screens.UserProfessor.criarTreino

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.plus

class CriarTreinoViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: ExercicioRepositoryModel = ExercicioRepository(),
    private val treinoRepository: TreinoRepositoryModel = TreinoRepository()
) : ViewModel() {

    private val alunoId: String = savedStateHandle.get<String>("uid") ?: "-1"

    private var _status = MutableStateFlow("")
    val status: StateFlow<String> = _status.asStateFlow()

    private val _titulo = MutableStateFlow("")
    val titulo: StateFlow<String> = _titulo.asStateFlow()

    private val _search = MutableStateFlow("")
    val search: StateFlow<String> = _search.asStateFlow()

    private val _todosExercicios = MutableStateFlow<List<Exercicio>>(emptyList())

    private val _exerciciosAdicionados = MutableStateFlow<List<String>>(emptyList())
    val exerciciosAdicionados: StateFlow<List<String>> = _exerciciosAdicionados.asStateFlow()

    private val _exerciciosFiltrados = mutableStateOf<List<Exercicio>>(emptyList())
    val exerciciosFiltrados: State<List<Exercicio>> = _exerciciosFiltrados

    init {
        viewModelScope.launch {
            val exercicios = repository.getAllExercicios()
            _todosExercicios.value = exercicios
            _exerciciosFiltrados.value = exercicios
        }
    }

    fun onSearchChange(novoTexto: String) {
        _search.value = novoTexto
        filtrarExercicios(novoTexto)
    }

    fun onTituloChange(novoTitulo: String) {
        _titulo.value = novoTitulo
    }

    fun criarNovoTreino(onSucces: () -> Unit) {
        if (_titulo.value.isBlank()) {
            _status.value = "Por favor, insira um título para o treino"
            return
        }

        if (_exerciciosAdicionados.value.isEmpty()) {
            _status.value = "Por favor, adicione pelo menos um exercício no treino"
            return
        }
        // criar treino
        viewModelScope.launch {
            treinoRepository.criarTreino(
                exercicios = _exerciciosAdicionados.value,
                nome = _titulo.value,
                alunoId = alunoId
            )

            onSucces()
        }
    }

    fun adicionarExercicio(novoExercicio: Exercicio) {
        val listaAtual = _exerciciosAdicionados.value
        val novoId = novoExercicio.id
        if (novoId == null) {
            // Handle the case where id is null, e.g., log an error, show a message, or simply return
            _status.value = "Erro: Exercício com ID nulo não pode ser adicionado."
            return
        }
        if (listaAtual.contains(novoId)) return
        val novaLista = listaAtual + novoId // novoId is now smart-cast to String
        _exerciciosAdicionados.value = novaLista
    }

    fun removerExercicio(exercicio: Exercicio) {
        val idParaRemover = exercicio.id
        _exerciciosAdicionados.value = _exerciciosAdicionados.value.filter { it != idParaRemover }
    }




    private fun filtrarExercicios(texto: String) {
        val todos = _todosExercicios.value
        _exerciciosFiltrados.value = if (texto.isBlank()) {
            todos
        } else {
            todos.filter {
                it.nome.toString().contains(texto, ignoreCase = true) ||
                        it.grupoMuscular.contains(texto, ignoreCase = true)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val savedStateHandle = extras.createSavedStateHandle()
                return CriarTreinoViewModel(savedStateHandle) as T
            }
        }
    }
}

