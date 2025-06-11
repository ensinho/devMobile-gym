package com.example.devmobile_gym.presentation.screens.UserAluno.detalhesTreino

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import com.example.devmobile_gym.utils.ImageUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DetalhesTreinoViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val treinoRepositoryModel: TreinoRepositoryModel = TreinoRepository()
    private val exerciciosRepository : ExercicioRepositoryModel = ExercicioRepository()
    private val treinoId: String = savedStateHandle.get<String>("treinoId") ?: "-1"

    private val _treinoSelecionado = MutableStateFlow<Treino?>(null)
    val treinoSelecionado: StateFlow<Treino?> = _treinoSelecionado

    private val _nomesExercicios = MutableStateFlow<Map<String, String>>(emptyMap())
    val nomesExercicios: StateFlow<Map<String, String>> = _nomesExercicios.asStateFlow()

    private val _exercicioImagesCache = MutableStateFlow<MutableMap<String, Bitmap?>>(mutableMapOf())
    private val imageLoadMutex = Mutex()

    private val _tempoEmHoras = MutableStateFlow(0)
    val tempoEmHoras: StateFlow<Int> = _tempoEmHoras
    private val _tempoEmMinutos = MutableStateFlow(0)
    val tempoEmMinutos: StateFlow<Int> = _tempoEmMinutos

    private val _seriesConcluidas = MutableStateFlow<MutableMap<String, Boolean>>(mutableMapOf())
    val seriesConcluidas: StateFlow<Map<String, Boolean>> = _seriesConcluidas.asStateFlow()

    private var cronometroJob: Job? = null
    private var emExecucao = false

    fun iniciarCronometro() {
        if (emExecucao) return
        emExecucao = true

        cronometroJob = viewModelScope.launch {
            while (isActive) {
                delay(60000)
                _tempoEmMinutos.value += 1

                if (_tempoEmMinutos.value == 60) {
                    _tempoEmHoras.value += 1
                    _tempoEmMinutos.value = 0
                }
            }
        }
    }

    fun onSerieCheckedChange(exercicioId: String, serieIndex: Int, isChecked: Boolean) {
        val key = "$exercicioId-$serieIndex"
        _seriesConcluidas.update { currentMap ->
            val newMap = currentMap.toMutableMap()
            newMap[key] = isChecked
            newMap
        }
    }

    fun isSerieChecked(exercicioId: String, serieIndex: Int): Boolean {
        val key = "$exercicioId-$serieIndex"
        return _seriesConcluidas.value[key] ?: false
    }

    private fun pausarCronometro() {
        emExecucao = false
        cronometroJob?.cancel()
    }

    fun getTreinoId() : String {
        return (_treinoSelecionado.value?.id ?: "Erro ao receber o ID do treino").toString()
    }

    fun finalizarTreino(): String {
        pausarCronometro()
        return "${_tempoEmHoras.value}h ${_tempoEmMinutos.value}min"
    }

    init {
        carregarTreino()
        iniciarCronometro()
    }

    fun getQuantidadeExercicios() : String {
        return (_treinoSelecionado.value?.exercicios?.size ?: "Erro ao receber a quantidade de exercício").toString()
    }

    private fun carregarTreino() {
        viewModelScope.launch {
            // 1. Carrega todos os exercícios E seus nomes.
            // É mais eficiente carregar todos uma vez e depois buscar no mapa.
            val todosExercicios = exerciciosRepository.getAllExercicios()
            _nomesExercicios.value = todosExercicios.associate { it.id.toString() to (it.nome ?: "Exercício sem nome") }
            Log.d("DetalhesTreinoVM", "Nomes de exercícios pré-carregados: ${_nomesExercicios.value.keys}")

            // 2. Carrega o treino principal
            val treino = treinoRepositoryModel.getTreino(treinoId)
            _treinoSelecionado.value = treino
            Log.d("DetalhesTreinoVM", "Treino selecionado carregado: ${treino?.id}")

            // Opcional: Se _nomesExercicios ainda não tiver o nome de algum exercício do treino,
            // você pode fazer um carregamento extra para eles aqui, mas o ideal é que getAllExercicios() seja completo.
            treino?.exercicios?.forEach { exercicioId ->
                if (!_nomesExercicios.value.containsKey(exercicioId)) {
                    Log.w("DetalhesTreinoVM", "Nome do exercício $exercicioId não encontrado no pré-carregamento. Tentando buscar individualmente.")
                    val exercicio = exerciciosRepository.getExercicio(exercicioId)
                    exercicio?.let {
                        _nomesExercicios.update { currentMap ->
                            currentMap.toMutableMap().apply { put(it.id.toString(), it.nome ?: "Exercício sem nome") }
                        }
                    }
                }
            }
        }
    }

    fun getNomeExercicio(id: String): String {
        // Acessa o nome do mapa _nomesExercicios
        val nome = _nomesExercicios.value[id]
        if (nome.isNullOrBlank()) {
            Log.w("DetalhesTreinoVM", "Nome para exercicio ID: $id não encontrado no mapa de nomes. Retornando padrão.")
            return "Exercício não encontrado"
        }
        return nome
    }

    suspend fun getImagemExercicio(exercicioId: String): Bitmap? {
        // 1. Tenta obter do cache primeiro (leitura rápida)
        val cachedBitmap = _exercicioImagesCache.value[exercicioId]
        if (cachedBitmap != null) {
            Log.d("DetalhesTreinoVM", "Imagem para $exercicioId encontrada no cache.")
            return cachedBitmap
        }

        // 2. Se não está no cache, adquire o lock para evitar operações duplicadas
        return imageLoadMutex.withLock {
            // 3. Verifica novamente dentro do lock (outra coroutine pode ter carregado enquanto esperávamos)
            val reCheckedCachedBitmap = _exercicioImagesCache.value[exercicioId]
            if (reCheckedCachedBitmap != null) {
                Log.d("DetalhesTreinoVM", "Imagem para $exercicioId encontrada no cache após lock.")
                return reCheckedCachedBitmap
            }

            // 4. Carrega o objeto Exercicio completo do repositório (operação suspend)
            Log.d("DetalhesTreinoVM", "Iniciando busca/decodificação da imagem para exercicioId: $exercicioId")
            val exercicio = exerciciosRepository.getExercicio(exercicioId)
            val imageBase64String = exercicio?.imagem // Campo 'imagem' contém a Base64

            // 5. Decodifica o Base64 para Bitmap (tentando GZIP e puro)
            val bitmap = ImageUtils.tryDecompressAndDecodeBase64ToBitmap(imageBase64String)

            // 6. Atualiza o cache do ViewModel
            _exercicioImagesCache.update { currentMap ->
                currentMap.toMutableMap().apply {
                    this[exercicioId] = bitmap
                }
            }
            Log.d("DetalhesTreinoVM", "Imagem para $exercicioId processada. Bitmap null? ${bitmap == null}")
            return bitmap
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
                return DetalhesTreinoViewModel(
                    savedStateHandle = savedStateHandle
                ) as T
            }
        }
    }
}