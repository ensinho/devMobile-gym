package com.example.devmobile_gym.presentation.screens.UserAluno.profile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.AlunoRepository
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.data.repository.UserWorkoutsRepository
import com.example.devmobile_gym.domain.model.TreinoComData
import com.example.devmobile_gym.domain.model.UserWorkouts
import com.example.devmobile_gym.domain.model.Usuario.Aluno
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import com.example.devmobile_gym.domain.repository.UserWorkoutsRepositoryModel
import com.example.devmobile_gym.utils.ImageUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Definindo o estado da UI para o processo de upload/carregamento da foto de perfil
sealed class ProfilePictureState {
    object Idle : ProfilePictureState()
    object Loading : ProfilePictureState()
    data class Success(val bitmap: Bitmap) : ProfilePictureState()
    data class Error(val message: String) : ProfilePictureState()
}

class ProfileViewModel : ViewModel() {

    private val alunoRepositoryModel: AlunoRepositoryModel = AlunoRepository()
    private val userWorkoutsRepository : UserWorkoutsRepositoryModel = UserWorkoutsRepository()
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance() // Inicialização correta

    private val treinoRepository : TreinoRepositoryModel = TreinoRepository()

    private val _aluno = MutableStateFlow<Aluno?>(null)
    val aluno: StateFlow<Aluno?> = _aluno.asStateFlow()

    private val _currentUserWorkouts = MutableStateFlow<UserWorkouts?>(null)
    val currentUserWorkouts: StateFlow<UserWorkouts?> = _currentUserWorkouts.asStateFlow()

    private val _lastTreinoName: MutableStateFlow<String> = MutableStateFlow("")
    val lastTreinoName: StateFlow<String> = _lastTreinoName.asStateFlow()

    private val _profilePictureBitmap = MutableStateFlow<Bitmap?>(null)
    val profilePictureBitmap: StateFlow<Bitmap?> = _profilePictureBitmap.asStateFlow()

    private val _profilePictureState = MutableStateFlow<ProfilePictureState>(ProfilePictureState.Idle)
    val profilePictureState: StateFlow<ProfilePictureState> = _profilePictureState.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    // --- Funções de Foto de Perfil (usando Base64 com GZIP) ---

    fun loadProfilePicture() {
        val userId = auth.currentUser?.uid ?: run {
            _profilePictureState.value = ProfilePictureState.Error("Usuário não autenticado.")
            return
        }

        viewModelScope.launch {
            try {
                _profilePictureState.value = ProfilePictureState.Loading

                val userDocRef = firestore.collection("alunos").document(userId)
                val document = userDocRef.get().await()

                // O campo no Firestore agora guarda a string Base64 COMPRIMIDA
                val compressedBase64String = document.getString("profilePictureBase64Compressed") // NOVO NOME DE CAMPO

                if (!compressedBase64String.isNullOrEmpty()) {
                    // 1. Descomprimir a string Base64 antes de decodificar para Bitmap
                    val decompressedBase64String = ImageUtils.gzipDecompress(compressedBase64String)

                    if (decompressedBase64String.isEmpty()) {
                        _profilePictureBitmap.value = null
                        _profilePictureState.value = ProfilePictureState.Error("Erro ao descomprimir foto do perfil.")
                        Log.e("ProfileViewModel", "Erro: String Base64 descomprimida está vazia.")
                        return@launch
                    }

                    // 2. Decodificar a string Base64 descomprimida para Bitmap
                    val bitmap = ImageUtils.base64ToBitmap(decompressedBase64String)
                    if (bitmap != null) {
                        _profilePictureBitmap.value = bitmap
                        _profilePictureState.value = ProfilePictureState.Success(bitmap)
                        Log.d("ProfileViewModel", "Foto de perfil Base64 carregada e decodificada (com compressão GZIP).")
                    } else {
                        _profilePictureBitmap.value = null
                        _profilePictureState.value = ProfilePictureState.Error("Erro ao decodificar a foto do perfil.")
                        Log.e("ProfileViewModel", "Erro ao decodificar Base64 descomprimida para Bitmap.")
                    }
                } else {
                    _profilePictureBitmap.value = null
                    _profilePictureState.value = ProfilePictureState.Idle
                    Log.d("ProfileViewModel", "Nenhuma foto de perfil Base64 comprimida encontrada.")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Erro ao carregar foto de perfil (GZIP): ${e.message}", e)
                _profilePictureState.value = ProfilePictureState.Error("Falha ao carregar foto: ${e.message}")
            }
        }
    }

    fun uploadProfilePicture(context: Context, imageUri: Uri) {
        val userId = auth.currentUser?.uid ?: run {
            _profilePictureState.value = ProfilePictureState.Error("Usuário não autenticado. Faça login para adicionar uma foto.")
            return
        }

        _profilePictureState.value = ProfilePictureState.Loading

        viewModelScope.launch {
            try {
                // 1. Converter Uri para Bitmap
                val bitmap = ImageUtils.uriToBitmap(context, imageUri)
                if (bitmap == null) {
                    _profilePictureState.value = ProfilePictureState.Error("Não foi possível carregar a imagem selecionada.")
                    return@launch
                }

                // 2. Converter Bitmap para String Base64 (não comprimida ainda)
                val base64String = ImageUtils.bitmapToBase64(bitmap)
                Log.d("ProfileViewModel", "Imagem convertida para Base64. Tamanho da string ORIGINAL: ${base64String.length / 1024} KB")

                // ** IMPORTANTE: Verificar o tamanho ANTES da compressão GZIP.
                // Se a imagem for muito grande *antes* do GZIP, pode ser um problema de otimização do bitmap.
                // O limite do Firestore é 1 MiB. Uma string Base64 é ~33% maior.
                // Após GZIP, ela pode reduzir um pouco, mas ainda pode ser grande.
                if (base64String.length > 1024 * 700) { // ~700KB original, para ter margem após encoding Base64
                    _profilePictureState.value = ProfilePictureState.Error("A imagem é muito grande antes da compressão. Por favor, escolha uma menor (máx ~700KB).")
                    return@launch
                }

                // 3. Comprimir a string Base64 usando GZIP
                val compressedBase64String = ImageUtils.gzipCompress(base64String)
                if (compressedBase64String.isEmpty()) {
                    _profilePictureState.value = ProfilePictureState.Error("Erro ao comprimir a imagem.")
                    Log.e("ProfileViewModel", "Erro: String Base64 comprimida está vazia.")
                    return@launch
                }
                Log.d("ProfileViewModel", "Imagem Base64 comprimida com GZIP. Tamanho FINAL da string: ${compressedBase64String.length / 1024} KB")

                // 4. Salvar a String Base64 COMPRIMIDA no Firestore
                val userDocRef = firestore.collection("alunos").document(userId)
                userDocRef.update("profilePictureBase64Compressed", compressedBase64String).await() // NOVO NOME DE CAMPO
                Log.d("ProfileViewModel", "String Base64 COMPRIMIDA da foto de perfil salva no Firestore.")

                _profilePictureBitmap.value = bitmap
                _profilePictureState.value = ProfilePictureState.Success(bitmap)

            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Erro ao converter/comprimir/salvar foto Base64 (GZIP): ${e.message}", e)
                _profilePictureState.value = ProfilePictureState.Error("Falha ao atualizar foto: ${e.message}")
            }
        }
    }

    // --- Funções Auxiliares de UI (Não relacionadas diretamente à compressão Base64) ---
    fun onImageSelectedForUpload(uri: Uri?) {
        _selectedImageUri.value = uri
        // A lógica de upload agora é chamada diretamente na ProfileScreen quando o launcher retorna um URI válido.
    }

    // --- Funções de Carregamento de Dados do Perfil ---
    init {
        carregarAluno()
        loadProfilePicture() // Carrega a foto de perfil (agora com descompressão Base64)
    }

    private fun carregarAluno() {
        viewModelScope.launch {
            val alunoCarregado = alunoRepositoryModel.getAlunoLogado()
            _aluno.value = alunoCarregado
        }
    }

    fun loadLastTreinoNameFromHistory(historico: List<TreinoComData>?) {
        viewModelScope.launch {
            if (historico.isNullOrEmpty()) {
                _lastTreinoName.value = "Nenhum treino realizado"
                return@launch
            }
            try {
                val lastTreinoId = historico.last().treinoId
                val treino = treinoRepository.getTreino(lastTreinoId)
                _lastTreinoName.value = treino?.nome ?: "Treino não encontrado"
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Erro ao carregar o nome do último treino do histórico: ${e.message}", e)
                _lastTreinoName.value = "Erro ao carregar treino"
            }
        }
    }

    fun loadCurrentUserWorkouts(userId: String) {
        viewModelScope.launch {
            try {
                val workouts = userWorkoutsRepository.obterStreakPorUserId(userId)
                _currentUserWorkouts.value = workouts
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Erro ao carregar UserWorkouts: ${e.message}", e)
                _currentUserWorkouts.value = null
            }
        }
    }
}