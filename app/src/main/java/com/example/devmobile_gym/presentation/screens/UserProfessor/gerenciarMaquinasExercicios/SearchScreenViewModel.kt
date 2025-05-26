package com.example.devmobile_gym.presentation.screens.UserProfessor.gerenciarMaquinasExercicios

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GerenciarMaquinasExerciciosViewModel (
    private val repository: ExercicioRepositoryModel = ExercicioRepository()
) : ViewModel(){
    private val db = FirebaseFirestore.getInstance()
    private val _search = MutableStateFlow("")
    val search: StateFlow<String> = _search.asStateFlow()

    private val _todasMaqExerc = MutableStateFlow<List<Exercicio>>(emptyList())
    private val _MaqExercFiltrados = mutableStateOf<List<Exercicio>>(emptyList())
    val MaqExercFiltrados: State<List<Exercicio>> = _MaqExercFiltrados

    fun removerExercicio(uid: String) {
        viewModelScope.launch {
            val sucesso = repository.deletarExercicio(uid)
            if (sucesso) {
                Log.d("Exercicio", "Excluído com sucesso")
                _todasMaqExerc.value = _todasMaqExerc.value.filterNot { it.id == uid }
                _MaqExercFiltrados.value = _MaqExercFiltrados.value.filterNot { it.id == uid }
            } else {
                Log.e("Exercicio", "Falha ao excluir")
            }
        }
    }
    init {
        viewModelScope.launch {
            val exercicios = repository.getAllExercicios()
            _todasMaqExerc.value = exercicios
            _MaqExercFiltrados.value = exercicios
        }
    }

    fun onSearchChange(newSearch: String) {
        _search.value = newSearch
        filtrarMaqExerc(_search.value)
    }

    private fun filtrarMaqExerc(texto: String) {
        val todos = _todasMaqExerc.value
        _MaqExercFiltrados.value = if (texto.isBlank()) {
            todos
        } else {
            todos.filter {
                it.nome.contains(texto, ignoreCase = true) ||
                        it.grupoMuscular.contains(texto, ignoreCase = true)
            }
        }
    }
}
