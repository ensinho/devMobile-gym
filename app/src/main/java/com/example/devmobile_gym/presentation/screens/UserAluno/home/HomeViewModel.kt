package com.example.devmobile_gym.presentation.screens.UserAluno.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.AlunoRepository
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.model.Usuario.Aluno
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel (
) : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val treinoRepository: TreinoRepositoryModel = TreinoRepository()
    private val exerciciosRepository: ExercicioRepositoryModel = ExercicioRepository()
    private val alunoRepository : AlunoRepositoryModel = AlunoRepository()

    private val _treinos = MutableStateFlow<List<Treino>>(emptyList())
    val treinos: StateFlow<List<Treino>> = _treinos

    private val _nomesExercicios = MutableStateFlow<Map<String, String>>(emptyMap())
    val nomesExercicios: StateFlow<Map<String, String>> = _nomesExercicios.asStateFlow()

    private val _aluno = MutableStateFlow<Aluno?>(null)
    val aluno: StateFlow<Aluno?> = _aluno.asStateFlow()


    init {
        loadData()
    }

    fun getNomeExercicio(id: String): String {
        println("Buscando nome para o exercício ID: $id")
        return _nomesExercicios.value[id] ?: "Exercício não encontrado"
    }

    fun loadData() {
        viewModelScope.launch {

            // 1. Carrega todos os exercícios e mapeia nomes por ID
            val todosExercicios = exerciciosRepository.getAllExercicios()
            _nomesExercicios.value = todosExercicios.associateBy({ it.id.toString() }, { it.nome.toString() })

            // carrega os treinos
            val listaTreinos = treinoRepository.getTreinos()
            _treinos.value = listaTreinos
            println("Treinos carregados: ${_treinos.value}")


            println("Exercícios carregados: ${_nomesExercicios.value}")
        }
    }
}