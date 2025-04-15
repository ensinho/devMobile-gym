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

    // por enquanto é basicamente um login com confirmação de senha.
    fun registrar(onSuccess: () -> Unit) {
        val user = alunoRepository
        if (email.value.isNotEmpty() && senha.value.isNotEmpty()) {
            if (senha.value == confirmSenha.value) {

                val success = user.logar(email.value, senha.value)
                if (success) {
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

}