// presentation/screens/UserProfessor/adicionaMaquinaExercicio/editarExercicioViewModel.kt
package com.example.devmobile_gym.presentation.screens.UserProfessor.adicionaMaquinaExercicio

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.example.devmobile_gym.presentation.screens.UserAluno.profile.ProfilePictureState
import com.example.devmobile_gym.utils.ImageUtils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class editarExercicioViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: ExercicioRepositoryModel = ExercicioRepository()
) : ViewModel() {

    private val exercicioID: String = savedStateHandle["MaqExerc.id"] ?: ""
    private val firestore = FirebaseFirestore.getInstance()

    private val _exercicioSelecionado = mutableStateOf<Exercicio?>(null)
    val exercicioSelecionado: State<Exercicio?> = _exercicioSelecionado

    private val _isIncluded = mutableStateOf(false)
    val isIncluded: State<Boolean> = _isIncluded

    private val _numExercicio = mutableStateOf("")
    val numExercicio: State<String> = _numExercicio

    // Propriedades para a foto do exercício
    private val _exercicioPhotoBitmap = MutableStateFlow<Bitmap?>(null)
    val exercicioPhotoBitmap: StateFlow<Bitmap?> = _exercicioPhotoBitmap.asStateFlow()

    private val _exercicioPhotoState = MutableStateFlow<ProfilePictureState>(ProfilePictureState.Idle)
    val exercicioPhotoState: StateFlow<ProfilePictureState> = _exercicioPhotoState.asStateFlow()

    // Campos de edição que **serão preenchidos com os dados existentes**
    private val _novoNome = mutableStateOf("")
    val novoNome: MutableState<String> = _novoNome

    private val _novoGrupoMuscular = mutableStateOf("")
    val novoGrupoMuscular: State<String> = _novoGrupoMuscular

    private val _novaDescricao = mutableStateOf("")
    val novaDescricao: MutableState<String> = _novaDescricao

    // --- Funções de onValueChange para os campos de texto ---
    fun onNovaDescricaoChange(novaDescricao: String) {
        _novaDescricao.value = novaDescricao
    }

    fun onNovoNomeChange(novoNome: String) {
        _novoNome.value = novoNome
    }

    fun onNovoGrupoMuscularChange(novoGrupo: String) {
        _novoGrupoMuscular.value = novoGrupo
    }

    private val _isSaving = mutableStateOf(false)
    val isSaving: State<Boolean> = _isSaving

    private val _saveSuccess = mutableStateOf(false)
    val saveSuccess: State<Boolean> = _saveSuccess

    // Função para editar o exercício (nome, grupo muscular e descrição)
    fun editarExercicio(newName: String, newGrupoMuscular: String, newDescricao: String) {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                // Atualiza nome e grupo muscular via repositório
                repository.updateExercicio(exercicioID, newName, newGrupoMuscular)

                // Atualiza a descrição diretamente no Firestore (como o repositório não tem esse método)
                firestore.collection("exercicios").document(exercicioID)
                    .update("descricao", newDescricao)
                    .await()

                _saveSuccess.value = true
                Log.d("editarExercicioViewModel", "Exercício atualizado com sucesso!")
            } catch (e: Exception) {
                Log.e("editarExercicioViewModel", "Erro ao atualizar: ${e.message}")
                _saveSuccess.value = false
            } finally {
                _isSaving.value = false
            }
        }
    }

    // --- Funções de Foto do Exercício (usando Base64 com GZIP) ---

    // Carrega a foto do exercício do Firestore
    fun loadExercicioPhoto() {
        if (exercicioID.isEmpty()) {
            _exercicioPhotoState.value = ProfilePictureState.Error("ID do exercício não disponível para carregar foto.")
            return
        }

        viewModelScope.launch {
            try {
                _exercicioPhotoState.value = ProfilePictureState.Loading

                val exercicioDocRef = firestore.collection("exercicios").document(exercicioID)
                val document = exercicioDocRef.get().await()

                // Lendo do campo 'imagem', que agora conterá a Base64 comprimida ou não
                val imageBase64String = document.getString("imagem")

                if (!imageBase64String.isNullOrEmpty()) {
                    // Tenta descomprimir e decodificar Base64
                    val bitmap = ImageUtils.tryDecompressAndDecodeBase64ToBitmap(imageBase64String)

                    if (bitmap != null) {
                        _exercicioPhotoBitmap.value = bitmap
                        _exercicioPhotoState.value = ProfilePictureState.Success(bitmap)
                        Log.d("editarExercicioViewModel", "Foto do exercício carregada e decodificada (GZIP ou puro).")
                    } else {
                        _exercicioPhotoBitmap.value = null
                        _exercicioPhotoState.value = ProfilePictureState.Error("Não foi possível decodificar a foto do exercício.")
                        Log.e("editarExercicioViewModel", "Falha ao decodificar a imagem do exercício.")
                    }
                } else {
                    _exercicioPhotoBitmap.value = null
                    _exercicioPhotoState.value = ProfilePictureState.Idle
                    Log.d("editarExercicioViewModel", "Nenhuma foto do exercício Base64 encontrada.")
                }
            } catch (e: Exception) {
                Log.e("editarExercicioViewModel", "Erro ao carregar foto do exercício: ${e.message}", e)
                _exercicioPhotoState.value = ProfilePictureState.Error("Falha ao carregar foto: ${e.message}")
            }
        }
    }

    // Função para fazer o upload/atualização da imagem do exercício
    fun uploadExercicioPhoto(context: Context, imageUri: Uri) {
        if (exercicioID.isEmpty()) {
            _exercicioPhotoState.value = ProfilePictureState.Error("ID do exercício não disponível para upload da foto.")
            return
        }

        viewModelScope.launch {
            _exercicioPhotoState.value = ProfilePictureState.Loading

            try {
                val bitmap = ImageUtils.uriToBitmap(context, imageUri)
                if (bitmap == null) {
                    _exercicioPhotoState.value = ProfilePictureState.Error("Não foi possível carregar a imagem selecionada.")
                    return@launch
                }

                val base64String = ImageUtils.bitmapToBase64(bitmap)
                Log.d("editarExercicioViewModel", "Imagem convertida para Base64. Tamanho da string ORIGINAL: ${base64String.length / 1024} KB")

                if (base64String.length > 1024 * 700) {
                    _exercicioPhotoState.value = ProfilePictureState.Error("A imagem é muito grande antes da compressão. Por favor, escolha uma menor (máx ~700KB).")
                    return@launch
                }

                val compressedBase64String = ImageUtils.gzipCompress(base64String)
                if (compressedBase64String.isEmpty()) {
                    _exercicioPhotoState.value = ProfilePictureState.Error("Erro ao comprimir a imagem.")
                    return@launch
                }
                Log.d("editarExercicioViewModel", "Imagem Base64 comprimida com GZIP. Tamanho FINAL da string: ${compressedBase64String.length / 1024} KB")

                // Atualiza o campo 'imagem' no Firestore com a nova Base64 comprimida
                val exercicioDocRef = firestore.collection("exercicios").document(exercicioID)
                exercicioDocRef.update("imagem", compressedBase64String).await()
                Log.d("editarExercicioViewModel", "String Base64 COMPRIMIDA da foto do exercício salva no campo 'imagem' no Firestore.")

                _exercicioPhotoBitmap.value = bitmap // Atualiza o Bitmap para exibição imediata
                _exercicioPhotoState.value = ProfilePictureState.Success(bitmap)

            } catch (e: Exception) {
                Log.e("editarExercicioViewModel", "Erro ao converter/comprimir/salvar foto Base64 (GZIP): ${e.message}", e)
                _exercicioPhotoState.value = ProfilePictureState.Error("Falha ao atualizar foto: ${e.message}")
            }
        }
    }

    // --- Initializer Block ---
    init {
        Log.d("editarExercicioViewModel", "exercicioID: $exercicioID")
        viewModelScope.launch {
            carregarExercicio() // Carrega os dados do exercício existente
            loadExercicioPhoto() // Carrega a foto do exercício existente
        }
    }

    // Carrega os dados do exercício e preenche os campos de State
    private suspend fun carregarExercicio() {
        val exercicio = repository.getExercicio(exercicioID)
        exercicio?.let {
            _exercicioSelecionado.value = it // Mantém o objeto de exercício original se necessário
            _novoNome.value = it.nome.toString() // Preenche o campo de nome
            _novoGrupoMuscular.value = it.grupoMuscular // Preenche o campo de grupo muscular
            _novaDescricao.value = it.descricao // Preenche o campo de descrição
        }
    }

    fun onIsIncludedChange() {
        _isIncluded.value = !_isIncluded.value
    }

    // --- Companion object com Factory para instanciar o ViewModel ---
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val savedStateHandle = extras.createSavedStateHandle()
                return editarExercicioViewModel(
                    savedStateHandle = savedStateHandle,
                    repository = ExercicioRepository()
                ) as T
            }
        }
    }
}