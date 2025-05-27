package com.example.devmobile_gym.presentation.screens.UserProfessor.gerenciaAluno

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.model.Usuario
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.example.devmobile_gym.domain.repository.ProfessorRepositoryModel
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GerenciaAlunoViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val db = FirebaseFirestore.getInstance()
    private val alunoId: String = savedStateHandle["uid"] ?: throw IllegalArgumentException("UID não encontrado")

    private val treinoRepository: TreinoRepositoryModel = TreinoRepository()
    private val exerciciosRepository : ExercicioRepositoryModel = ExercicioRepository()

    private val _alunoSelecionado = MutableStateFlow<Usuario.Aluno?>(null)
    val alunoSelecionado: StateFlow<Usuario.Aluno?> = _alunoSelecionado

    private val _treinos = MutableStateFlow<List<Treino?>>(emptyList())
    val treinos: StateFlow<List<Treino?>> = _treinos.asStateFlow()

    private var _status = MutableStateFlow("")
    val status: StateFlow<String> = _status.asStateFlow()

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _nomesExercicios = MutableStateFlow<Map<String, String>>(emptyMap())
    val nomesExercicios: StateFlow<Map<String, String>> = _nomesExercicios.asStateFlow()

    init {
        viewModelScope.launch {
            loadData()
        }
    }

    suspend fun loadData() {
        try {
            // Carrega os nomes dos exercícios
            val todos = exerciciosRepository.getAllExercicios()
            _nomesExercicios.value = todos.associateBy({ it.id.toString() }, { it.nome.toString() })

            _isLoading.value = true

            // Busca o aluno
            val aluno = getAlunoById(alunoId)
            _alunoSelecionado.value = aluno

            // Se rotina for não nula e não vazia, escuta os treinos e carrega os dados
            val rotina = aluno?.rotina

            if (!rotina.isNullOrEmpty()) {
                // Escuta atualizações em tempo real
                db.collection("treinos")
                    .whereIn(FieldPath.documentId(), rotina)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            _error.value = "Erro ao escutar treinos: ${e.message}"
                            return@addSnapshotListener
                        }

                        val novosTreinos = snapshot?.toObjects(Treino::class.java) ?: emptyList()
                        _treinos.value = novosTreinos
                    }

                // Carrega os treinos inicialmente
                val treinosCarregados = treinoRepository.getTreinosByIds(rotina)
                _treinos.value = treinosCarregados
            } else {
                _treinos.value = emptyList() // Garante que a UI saiba que não há treinos
            }

        } catch (e: Exception) {
            _error.value = "Erro ao carregar dados: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }



    private suspend fun getAlunoById(uid: String): Usuario.Aluno? {
        val snap = db.collection("alunos").document(uid).get().await()
        return snap.toObject(Usuario.Aluno::class.java)
    }

    fun deletarTreino(treinoId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _status.value = "" // limpa mensagens anteriores
            try {
                treinoRepository.deleteTreino(treinoId)
                onSuccess()
            } catch (e: Exception) {
                _status.value = "Erro ao deletar treino: ${e.message}"
            }
        }
    }

    fun getNomeExercicio(id: String): String {
        return _nomesExercicios.value[id] ?: "Exercício não encontrado"
    }


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val savedStateHandle = extras.createSavedStateHandle()
                return GerenciaAlunoViewModel(
                    savedStateHandle = savedStateHandle
                ) as T
            }
        }
    }
}