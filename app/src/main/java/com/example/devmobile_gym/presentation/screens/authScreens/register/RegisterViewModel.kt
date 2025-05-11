package com.example.devmobile_gym.presentation.screens.authScreens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.AlunoRepositoryMock
import com.example.devmobile_gym.data.repository.AuthRepositoryImpl
import com.example.devmobile_gym.domain.repository.AlunoRepository
import com.example.devmobile_gym.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository = AuthRepositoryImpl(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _senha = MutableStateFlow("")
    val senha: StateFlow<String> = _senha.asStateFlow()

    private val _confirmSenha = MutableStateFlow("")
    val confirmSenha: StateFlow<String> = _confirmSenha.asStateFlow()

    private val _nome = MutableStateFlow("")
    val nome: StateFlow<String> = _nome.asStateFlow()

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var uiState by mutableStateOf<RegisterUiState>(RegisterUiState.Idle)
        private set

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onNomeChange(newNome: String) {
        _nome.value = newNome
    }

    fun onSenhaChange(newSenha: String) {
        _senha.value = newSenha
    }

    fun onConfirmaSenhaChange(newConfirmSenha: String) {
        _confirmSenha.value = newConfirmSenha
    }

    fun validaEmail(email: String, onSuccess: () -> Unit) {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val isValido = emailRegex.matches(email)

        if (!isValido) {
            errorMessage = "Email inválido"
        } else {
            errorMessage = null
            onSuccess()
        }

    }

}