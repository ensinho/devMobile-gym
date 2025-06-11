package com.example.devmobile_gym.presentation.screens.UserProfessor.adicionarExercicio

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class adicionarExercicioViewModel(
    private val repository: ExercicioRepositoryModel = ExercicioRepository()
) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _novoNome = mutableStateOf("")
    val novoNome: State<String> = _novoNome

    private val _novoGrupoMuscular = mutableStateOf("")
    val novoGrupoMuscular: State<String> = _novoGrupoMuscular

    // --- NOVO: Propriedades para a descrição do exercício ---
    private val _novaDescricao = mutableStateOf("")
    val novaDescricao: State<String> = _novaDescricao

    fun onNovaDescricaoChange(novaDescricao: String) {
        _novaDescricao.value = novaDescricao
    }
    // --- Fim NOVO ---

    private val _exercicioPhotoBitmap = MutableStateFlow<Bitmap?>(null)
    val exercicioPhotoBitmap: StateFlow<Bitmap?> = _exercicioPhotoBitmap.asStateFlow()

    private val _exercicioPhotoState = MutableStateFlow<ProfilePictureState>(ProfilePictureState.Idle)
    val exercicioPhotoState: StateFlow<ProfilePictureState> = _exercicioPhotoState.asStateFlow()

    private var base64ImageForSave: String? = null

    private val _saveSuccess = mutableStateOf(false)
    val saveSuccess: State<Boolean> = _saveSuccess

    fun onNovoNomeChange(novoNome: String) {
        _novoNome.value = novoNome
    }

    fun onNovoGrupoMuscularChange(novoGrupo: String) {
        _novoGrupoMuscular.value = novoGrupo
    }

    fun uploadExercicioPhoto(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            _exercicioPhotoState.value = ProfilePictureState.Loading

            try {
                val bitmap = ImageUtils.uriToBitmap(context, imageUri)
                if (bitmap == null) {
                    _exercicioPhotoState.value = ProfilePictureState.Error("Não foi possível carregar a imagem selecionada.")
                    return@launch
                }

                val base64String = ImageUtils.bitmapToBase64(bitmap)
                Log.d("adicionarExercicioVM", "Imagem convertida para Base64. Tamanho ORIGINAL: ${base64String.length / 1024} KB")

                if (base64String.length > 1024 * 700) {
                    _exercicioPhotoState.value = ProfilePictureState.Error("A imagem é muito grande. Escolha uma menor (máx ~700KB).")
                    return@launch
                }

                val compressedBase64String = ImageUtils.gzipCompress(base64String)
                if (compressedBase64String.isEmpty()) {
                    _exercicioPhotoState.value = ProfilePictureState.Error("Erro ao comprimir a imagem.")
                    return@launch
                }
                Log.d("adicionarExercicioVM", "Imagem Base64 comprimida com GZIP. Tamanho FINAL: ${compressedBase64String.length / 1024} KB")

                base64ImageForSave = compressedBase64String
                _exercicioPhotoBitmap.value = bitmap
                _exercicioPhotoState.value = ProfilePictureState.Success(bitmap)

            } catch (e: Exception) {
                Log.e("adicionarExercicioVM", "Erro ao processar/upload foto: ${e.message}", e)
                _exercicioPhotoState.value = ProfilePictureState.Error("Falha ao adicionar foto: ${e.message}")
            }
        }
    }

    fun salvarExercicio() {
        val nome = _novoNome.value.trim()
        val grupo = _novoGrupoMuscular.value.trim()
        val descricao = _novaDescricao.value.trim() // NOVO: Obtenha a descrição

        if (nome.isNotEmpty() && grupo.isNotEmpty() && descricao.isNotEmpty()) { // Adicione validação para descrição
            val novoExercicio = Exercicio(
                nome = nome,
                grupoMuscular = grupo,
                descricao = descricao, // NOVO: Inclua a descrição
                imagem = base64ImageForSave
            )

            viewModelScope.launch {
                try {
                    repository.insertExercicio(novoExercicio)
                    _saveSuccess.value = true
                    // Resetar estados para próxima adição
                    _novoNome.value = ""
                    _novoGrupoMuscular.value = ""
                    _novaDescricao.value = "" // NOVO: Resetar a descrição
                    _exercicioPhotoBitmap.value = null
                    _exercicioPhotoState.value = ProfilePictureState.Idle
                    base64ImageForSave = null
                } catch (e: Exception) {
                    _saveSuccess.value = false
                    e.printStackTrace()
                    Log.e("adicionarExercicioVM", "Erro ao salvar exercício: ${e.message}")
                }
            }
        } else {
            // Opcional: Melhorar o feedback para o usuário se campos obrigatórios estiverem vazios
            Log.e("adicionarExercicioVM", "Nome, grupo muscular ou descrição não podem ser vazios.")
            // Você pode adicionar um StateFlow para exibir uma mensagem de erro na UI
        }
    }
}