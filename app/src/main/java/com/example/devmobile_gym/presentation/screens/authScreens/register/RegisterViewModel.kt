package com.example.devmobile_gym.presentation.screens.authScreens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.devmobile_gym.domain.model.Usuario.Aluno
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel() : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance() // vou deixar login/registro/log-out no authviewmodel

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _senha = MutableStateFlow("")
    val senha: StateFlow<String> = _senha.asStateFlow()

    private val _peso = MutableStateFlow("")
    val peso: StateFlow<String> = _peso.asStateFlow()

    private val _altura = MutableStateFlow("")
    val altura: StateFlow<String> = _altura.asStateFlow()

    private val _confirmSenha = MutableStateFlow("")
    val confirmSenha: StateFlow<String> = _confirmSenha.asStateFlow()

    private val _nome = MutableStateFlow("")
    val nome: StateFlow<String> = _nome.asStateFlow()

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPesoChange(newPeso: String) {
        _peso.value = newPeso

    }

    fun onAlturaChange(newAltura: String) {
        _altura.value = newAltura
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

    private fun validaEmail(email: String, onSuccess: () -> Unit) {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val isValido = emailRegex.matches(email)

        if (!isValido) {
            errorMessage = "Email inválido"
        } else {
            errorMessage = null
            onSuccess()
        }

    }

    fun validarCamposRegistro1(email: String, nome: String, onSuccess: () -> Unit) {
        if (email.isEmpty() || nome.isEmpty()) {
            errorMessage = "Preencha todos os campos."
        } else if (nome.length < 3) { // ← validação
            errorMessage = "Nome deve ter pelo menos 3 caracteres"
        } else {
            val regex = Regex("[0-9\\W]+")
            if (nome.contains(regex)) {
                errorMessage = "Nome contem números ou caracteres especiais"
                return
            }
            validaEmail(email, onSuccess)
        }
    }

}

/*
 fun verificarDominioProfessor(email: String): Boolean {
        val dominioProfessor = "professor@universidade.com" // Domínio para identificar o professor
        return email.contains(dominioProfessor)
    }

    // Função para validar e criar o objeto correto (Professor ou Aluno)
    fun validaEmail(email: String, onSuccess: (Usuario) -> Unit) {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val isValido = emailRegex.matches(email)

        if (!isValido) {
            errorMessage = "Email inválido"
        } else {
            // Verifica se o email é do domínio do professor e cria a instância correspondente
            val isProfessor = verificarDominioProfessor(email)
            val usuario: Usuario = if (isProfessor) {
                Usuario.Professor(email, _nome.value, "Área de Conhecimento") // Exemplo de área de conhecimento
            } else {
                Usuario.Aluno(email, _nome.value, "Matricula1234") // Exemplo de matrícula
            }

            errorMessage = null
            onSuccess(usuario)
        }
    }

    fun validarCamposRegistro1(email: String, nome: String, onSuccess: (Usuario) -> Unit) {
        if (email.isEmpty() || nome.isEmpty()) {
            errorMessage = "Preencha todos os campos."
        } else if (nome.length < 3) { // Validação de nome
            errorMessage = "Nome deve ter pelo menos 3 caracteres"
        } else {
            validaEmail(email, onSuccess)
        }
    }
*/
