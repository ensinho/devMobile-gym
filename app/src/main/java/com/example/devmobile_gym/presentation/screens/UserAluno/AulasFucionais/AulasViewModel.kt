package com.example.devmobile_gym.presentation.screens.UserAluno.AulasFucionais

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.AulaRepository
import com.example.devmobile_gym.domain.model.Aula
import com.example.devmobile_gym.domain.repository.AulaRepositoryModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AulasViewModel(
    private val repository: AulaRepositoryModel = AulaRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
): ViewModel() {

    private val _aulas = MutableStateFlow<List<Aula?>>(emptyList())
    val aulas : StateFlow<List<Aula?>> = _aulas.asStateFlow()

    private val _uiState = MutableStateFlow<String>("")
    val uiState : StateFlow<String> = _uiState.asStateFlow()

    private val _quantidadeMax = MutableStateFlow<Int>(0)
    val quantidadeMax : StateFlow<Int> = _quantidadeMax.asStateFlow()


    init{
        viewModelScope.launch {
            carregaAulas()
        }
    }

    suspend fun carregaAulas(){
        _aulas.value = repository.getAulas()
    }

    fun inscreverAluno(idAula: String) {
        viewModelScope.launch {
            try {
                repository.incrementQuantAlunos(idAula)
                // Atualizar UI ou mostrar sucesso
            } catch (e: Exception) {
                // Tratar erro (ex: mostrar Snackbar)
                _uiState.value = "Erro na inscrição"
            }
        }
    }

    fun isInscrito(aula: Aula): Boolean {
        val userId = auth.currentUser?.uid ?: return false
        return aula.inscritos.contains(userId)
    }
}