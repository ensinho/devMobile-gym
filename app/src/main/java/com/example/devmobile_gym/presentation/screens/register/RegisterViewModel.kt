package com.example.devmobile_gym.presentation.screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.devmobile_gym.data.repository.AlunoRepositoryMock
import com.example.devmobile_gym.domain.repository.AlunoRepository

class RegisterViewModel(
    private val alunoRepository: AlunoRepository = AlunoRepositoryMock()

) : ViewModel() {

    var email = mutableStateOf("")
        private set

    var senha = mutableStateOf("")
        private set

    var confirmSenha = mutableStateOf("")
        private set

    var nome = mutableStateOf("")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(newEmail: String) {
        email.value = newEmail
    }

    fun onSenhaChange(newSenha: String) {
        senha.value = newSenha
    }

    fun onConfirmaSenhaChange(newConfirmSenha: String) {
        confirmSenha.value = newConfirmSenha
    }

    // por enquanto é basicamente um login com confirmação de senha.
    fun registrar(onSuccess: () -> Unit) {
        val user = alunoRepository
        if (confirmSenha.value.isNotEmpty() && senha.value.isNotEmpty()) {
            if (senha.value == confirmSenha.value) {

                val success = user.registrar(email.value, senha.value, confirmSenha.value)
                if (success != null) {
                    errorMessage = null
                    onSuccess()
                } else {
                    errorMessage = "Credenciais inválidas."
                }
            } else {
                errorMessage = "As senhas não coincidem."
            }

        } else {
            errorMessage = "Preencha todos os campos"
        }

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