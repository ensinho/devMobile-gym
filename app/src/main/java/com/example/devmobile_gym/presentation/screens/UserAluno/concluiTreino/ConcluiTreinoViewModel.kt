package com.example.devmobile_gym.presentation.screens.detalhesTreino

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.devmobile_gym.data.repository.AlunoRepository
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.data.repository.UserWorkoutsRepository
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.model.UserWorkouts
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import com.example.devmobile_gym.domain.repository.UserWorkoutsRepositoryModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.toString

class ConcluiTreinoViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val treinoRepositoryModel: TreinoRepositoryModel = TreinoRepository()
    private val alunoRepositoryModel: AlunoRepositoryModel = AlunoRepository()
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val userWorkoutsRepository: UserWorkoutsRepositoryModel = UserWorkoutsRepository()

    private val treinoId: String = savedStateHandle.get<String>("treinoId") ?: "-1"
    private val tempoTreino: String = savedStateHandle.get<String>("tempoTreino") ?: "0"

    private val _treinoSelecionado = MutableStateFlow<Treino?>(null)
    val treinoSelecionado: StateFlow<Treino?> = _treinoSelecionado

    private val _quantidadeExercicios = MutableStateFlow<String>("Carregando...")
    val quantidadeExercicios: StateFlow<String> = _quantidadeExercicios

    private val _nomeTreino = MutableStateFlow<String>("Carregando...")
    val nomeTreino: StateFlow<String> = _nomeTreino

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _tempoTreino = MutableStateFlow<String>(tempoTreino)
    val tempo: StateFlow<String> = _tempoTreino

    init {
        carregarTreino()
    }

    private fun carregarTreino() {
        viewModelScope.launch {
            val treino = treinoRepositoryModel.getTreino(treinoId)
            _treinoSelecionado.value = treino

            if (treino != null) {
                _nomeTreino.value = treino.nome
                _quantidadeExercicios.value = treino.exercicios.size.toString()
            } else {
                _nomeTreino.value = "Treino não encontrado"
                _quantidadeExercicios.value = "0"
            }
        }
    }

    private fun getUserId() : String {
        val userId = auth.currentUser?.uid
        Log.d("getUserId", "User ID: $userId")
        return userId ?: throw IllegalStateException("User ID is null. User might not be authenticated.")
    }

    suspend fun getCurrentUserWorkouts(): UserWorkouts? {
        val userId = getUserId()
        return userWorkoutsRepository.obterStreakPorUserId(userId)
    }

    fun addToHistory(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val treino = treinoRepositoryModel.getTreino(treinoId)
            treino?.let {
                try {
                    alunoRepositoryModel.addToHistory(it)
                    onSuccess() // chama só se der certo
                } catch (e: Exception) {
                    Log.e("AddToHistory", "Erro ao adicionar ao histórico", e)
                } finally {
                    _isLoading.value = false // garante que sempre volta pro estado normal
                }
            } ?: run {
                _isLoading.value = false // se treino for nulo
            }
        }

    }


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val savedStateHandle = extras.createSavedStateHandle()
                return ConcluiTreinoViewModel(
                    savedStateHandle = savedStateHandle
                ) as T
            }
        }
    }
}

