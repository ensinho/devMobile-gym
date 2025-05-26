package com.example.devmobile_gym.presentation.screens.UserProfessor.adicionarExercicio

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import kotlinx.coroutines.launch

class adicionarExercicioViewModel(
    private val repository: ExercicioRepositoryModel = ExercicioRepository()
) : ViewModel() {

    private val _novoNome = mutableStateOf("")
    val novoNome: State<String> = _novoNome

    private val _novoGrupoMuscular = mutableStateOf("")
    val novoGrupoMuscular: State<String> = _novoGrupoMuscular

    private val _saveSuccess = mutableStateOf(false)
    val saveSuccess: State<Boolean> = _saveSuccess

    fun onNovoNomeChange(novoNome: String) {
        _novoNome.value = novoNome
    }

    fun onNovoGrupoMuscularChange(novoGrupo: String) {
        _novoGrupoMuscular.value = novoGrupo
    }

    fun salvarExercicio() {
        val nome = _novoNome.value.trim()
        val grupo = _novoGrupoMuscular.value.trim()

        if (nome.isNotEmpty() && grupo.isNotEmpty()) {
            val novoExercicio = Exercicio(
                nome = nome,
                grupoMuscular = grupo,
                imagem = "" // Pode ser adicionado se houver upload de imagem
            )

            viewModelScope.launch {
                try {
                    repository.insertExercicio(novoExercicio)
                    _saveSuccess.value = true
                } catch (e: Exception) {
                    _saveSuccess.value = false
                    e.printStackTrace()
                }
            }
        }
    }
}
