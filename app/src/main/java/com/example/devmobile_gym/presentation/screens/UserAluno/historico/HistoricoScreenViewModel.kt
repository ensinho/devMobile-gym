package com.example.devmobile_gym.presentation.screens.UserAluno.historico

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.AlunoRepository
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.model.TreinoComData
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

// Novo modelo para passar os detalhes completos para a UI
data class TreinoHistoricoUi(
    val dataRealizacao: String, // Data já formatada para exibição
    val nomeTreino: String,
    val nomesExercicios: List<String>
)

class HistoricoScreenViewModel () : ViewModel() {

    private val alunoRepository : AlunoRepositoryModel = AlunoRepository()
    private val exerciciosRepository : ExercicioRepositoryModel = ExercicioRepository()
    private val treinoRepository : TreinoRepositoryModel = TreinoRepository()

    // Não precisamos mais de _treinos e _exercicios separadamente para a UI do histórico
    // private val _treinos = MutableStateFlow<List<TreinoComData>>(emptyList())
    // val treinos: StateFlow<List<TreinoComData>> = _treinos.asStateFlow()

    // private val _exercicios = MutableStateFlow<List<Exercicio>>(emptyList())
    // val exercicios: StateFlow<List<Exercicio>> = _exercicios.asStateFlow()

    private val _nomesExerciciosCache = MutableStateFlow<Map<String, String>>(emptyMap()) // Cache de nomes de exercícios
    val nomesExerciciosCache: StateFlow<Map<String, String>> = _nomesExerciciosCache.asStateFlow()

    private val _treinosHistoricoUi = MutableStateFlow<List<TreinoHistoricoUi>>(emptyList())
    val treinosHistoricoUi: StateFlow<List<TreinoHistoricoUi>> = _treinosHistoricoUi.asStateFlow()

    init {
        carregarHistoricoCompleto()
    }

    private fun carregarHistoricoCompleto() {
        viewModelScope.launch {
            // 1. Carrega todos os exercícios e mapeia nomes por ID (para cache)
            // Isso evita múltiplas leituras do Firestore para cada exercício
            val todosExercicios = exerciciosRepository.getAllExercicios()
            _nomesExerciciosCache.value = todosExercicios.associateBy({ it.id.toString() }, { it.nome.toString() })

            // 2. Carrega o histórico do aluno (lista de TreinoComData)
            val listaTreinosComData = alunoRepository.getHistory()

            // 3. Para cada TreinoComData, busca o nome do treino e os nomes dos exercícios
            val historicoDetalhado = mutableListOf<TreinoHistoricoUi>()
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Para formatar a data

            for (treinoComData in listaTreinosComData) {
                try {
                    // Busca o objeto Treino completo usando o treinoId
                    val treino = treinoRepository.getTreino(treinoComData.treinoId)

                    val nomeDoTreino = treino?.nome ?: "Nome do Treino Não Encontrado"

                    // Obtém os nomes dos exercícios do Treino usando o cache
                    val nomesDosExerciciosDoTreino = treino?.exercicios?.mapNotNull { exercicioId ->
                        // Verifica se o ID do exercício não é nulo antes de buscar no cache
                        if (exercicioId != null) {
                            _nomesExerciciosCache.value[exercicioId]
                        } else {
                            null
                        }
                    } ?: emptyList()

                    val dataFormatada = treinoComData.dataRealizacao?.let { formatter.format(it) } ?: "Data Desconhecida"

                    historicoDetalhado.add(
                        TreinoHistoricoUi(
                            dataRealizacao = dataFormatada,
                            nomeTreino = nomeDoTreino,
                            nomesExercicios = nomesDosExerciciosDoTreino
                        )
                    )
                } catch (e: Exception) {
                    Log.e("HistoricoScreenViewModel", "Erro ao processar item do histórico: ${e.message}", e)
                    // Adiciona um item de erro para visualização ou loga
                    historicoDetalhado.add(
                        TreinoHistoricoUi(
                            dataRealizacao = treinoComData.dataRealizacao?.let { formatter.format(it) } ?: "Erro de Data",
                            nomeTreino = "Erro ao carregar treino",
                            nomesExercicios = listOf("Erro ao carregar exercícios")
                        )
                    )
                }
            }
            _treinosHistoricoUi.value = historicoDetalhado.reversed() // Exibir do mais recente para o mais antigo, se desejar
        }
    }

    // Estas funções auxiliares não são mais necessárias para a UI principal do histórico
    // já que os detalhes são pré-carregados em _treinosHistoricoUi
    /*
    fun getNomeExercicio(id: String): String {
        return _nomesExerciciosCache.value[id] ?: "Exercício não encontrado"
    }

    fun getExercicio(exercicioId: String) {
        viewModelScope.launch {
            val exercicio = exerciciosRepository.getExercicio(exercicioId)
            if (exercicio != null) {
                // Se ainda precisar popular _exercicios para outra finalidade
                // _exercicios.update { currentList -> currentList + exercicio }
            }
        }
    }
    */
}