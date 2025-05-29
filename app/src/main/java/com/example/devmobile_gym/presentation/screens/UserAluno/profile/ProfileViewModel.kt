package com.example.devmobile_gym.presentation.screens.UserAluno.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.AlunoRepository
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.data.repository.UserWorkoutsRepository
import com.example.devmobile_gym.domain.model.UserWorkouts
import com.example.devmobile_gym.domain.model.Usuario.Aluno
import com.example.devmobile_gym.domain.model.TreinoComData // Importe TreinoComData (que agora está adaptado)
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import com.example.devmobile_gym.domain.repository.UserWorkoutsRepositoryModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {

    private val alunoRepositoryModel: AlunoRepositoryModel = AlunoRepository()
    private val userWorkoutsRepository : UserWorkoutsRepositoryModel = UserWorkoutsRepository()
    private val treinoRepository : TreinoRepositoryModel = TreinoRepository() // Continua necessário para buscar o Treino real

    private val _aluno = MutableStateFlow<Aluno?>(null)
    val aluno: StateFlow<Aluno?> = _aluno

    private val _currentUserWorkouts = MutableStateFlow<UserWorkouts?>(null)
    val currentUserWorkouts: StateFlow<UserWorkouts?> = _currentUserWorkouts.asStateFlow()

    private val _lastTreinoName: MutableStateFlow<String> = MutableStateFlow("")
    val lastTreinoName: StateFlow<String> = _lastTreinoName.asStateFlow()

    private val _imagemGaleria = MutableStateFlow<Uri?>(Uri.EMPTY)
    val imagemGaleria: StateFlow<Uri?> = _imagemGaleria.asStateFlow()

    // NOVA FUNÇÃO: Recebe o Uri selecionado da UI e atualiza o StateFlow
    fun onImageSelected(uri: Uri?) {
        _imagemGaleria.value = uri
        // Opcional: Você pode adicionar lógica para salvar a imagem no Firebase Storage aqui
        // ou chamar outro repository para isso.
        Log.d("ProfileViewModel", "Imagem selecionada: $uri")
    }

    private val storage = FirebaseStorage.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Função para fazer upload da imagem e atualizar o Firestore
    fun uploadImageAndUpdateProfile(uri: Uri) {
        viewModelScope.launch {
            try {
                val userId = aluno.value?.uid ?: return@launch
                val storageRef = storage.reference.child("profile_images/$userId.jpg")

                storageRef.putFile(uri).await() // Usando corrotinas
                val downloadUri = storageRef.downloadUrl.await()

                // Atualiza o Firestore com a nova URL
                firestore.collection("alunos").document(userId)
                    .update("fotoUrl", downloadUri.toString())
                    .await()

                // Atualiza o estado local
                _aluno.value = aluno.value?.copy(fotoUrl = downloadUri.toString())
                _imagemGaleria.value = downloadUri
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Erro ao atualizar imagem", e)
            }
        }
    }

    init {
        carregarAluno()
    }

    private fun carregarAluno() {
        viewModelScope.launch {
            val aluno = alunoRepositoryModel.getAlunoLogado().apply {
                // Carrega a URL da imagem se existir
                _imagemGaleria.value = this?.fotoUrl?.let { Uri.parse(it) }
            }
            _aluno.value = aluno
        }
    }

    // A função agora recebe List<TreinoComData> como esperado, mas acessa o 'treinoId'
    fun loadLastTreinoNameFromHistory(historico: List<TreinoComData>?) {
        viewModelScope.launch {
            if (historico.isNullOrEmpty()) {
                _lastTreinoName.value = "Nenhum treino realizado"
                return@launch
            }
            try {
                // Acessa o 'treinoId' da última entrada de TreinoComData
                val lastTreinoId = historico.last().treinoId // <-- MUDANÇA AQUI
                val treino = treinoRepository.getTreino(lastTreinoId) // Busca o objeto Treino completo pelo ID
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