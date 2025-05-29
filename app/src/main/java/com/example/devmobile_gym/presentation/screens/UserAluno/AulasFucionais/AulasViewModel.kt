package com.example.devmobile_gym.presentation.screens.UserAluno.AulasFucionais


import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.AlunoRepository
import com.example.devmobile_gym.data.repository.AulaRepository
import com.example.devmobile_gym.domain.model.Aula
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel
import com.example.devmobile_gym.domain.repository.AulaRepositoryModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.log

class AulasViewModel(
    private val repository: AulaRepositoryModel = AulaRepository(),
    private val alunoRepository: AlunoRepositoryModel = AlunoRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
): ViewModel() {

    fun getUserId() : String {
        val userId = auth.currentUser?.uid
        Log.d("getUserId", "User ID: $userId")
        return userId ?: throw IllegalStateException("User ID is null. User might not be authenticated.")
    }

    private val _aulas = MutableStateFlow<List<Aula?>>(emptyList())
    val aulas : StateFlow<List<Aula?>> = _aulas.asStateFlow()

    private val _uiState = MutableStateFlow<String>("")
    val uiState : StateFlow<String> = _uiState.asStateFlow()

    private val _quantidadeMax = MutableStateFlow<Int>(0)
    val quantidadeMax : StateFlow<Int> = _quantidadeMax.asStateFlow()

    private val _novaQuantidade = MutableStateFlow<Int>(0)
    val novaQuantidade: StateFlow<Int> = _novaQuantidade

    init{
        viewModelScope.launch {
            carregaAulas()
        }
    }

    fun onNovaQuantidadeChange(novaQuantidade: Int) {
        _novaQuantidade.value = novaQuantidade
    }


    suspend fun carregaAulas(){//
        _aulas.value = repository.getAulas()


    }

    fun inscreverAluno(idAula: String, idAluno : String) {
//        viewModelScope.launch {
//            try {
//                repository.incrementQuantAlunos(idAula)
//                // Atualizar UI ou mostrar sucesso
//            } catch (e: Exception) {
//                // Tratar erro (ex: mostrar Snackbar)
//                _uiState.value = "Erro na inscrição"
//            }
//        }
        viewModelScope.launch {
            try {

                val aula = repository.getAula(idAula)
                if (aula != null) {
                    if (!aula.inscritos.contains(idAluno)){
                        repository.inserirAluno(idAula, idAluno)
                        carregaAulas()
                    }
                    else{

                    }
                }
                }catch (e: Exception) {
                    Log.e("AulaViewModel", "erro ao inscreverAluno")
            }
        }
    }
}

//    suspend fun isInscrito(aula: Aula): Boolean {
////        val userId = auth.currentUser?.uid ?: return false
////        return aula.inscritos.contains(userId)
//        val usuarioLogado = alunoRepository.getAlunoLogado()
//        if (usuarioLogado != null && ) {
//    }
