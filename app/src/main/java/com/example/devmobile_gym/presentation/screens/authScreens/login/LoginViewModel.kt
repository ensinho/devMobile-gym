package com.example.devmobile_gym.presentation.screens.authScreens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.devmobile_gym.data.mock.MockData
import com.example.devmobile_gym.data.repository.AlunoRepositoryMock
import com.example.devmobile_gym.data.repository.ProfessorRepositoryMock
import com.example.devmobile_gym.domain.repository.AlunoRepository
import com.example.devmobile_gym.domain.repository.ProfessorRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginViewModel(
    private val alunoRepository: AlunoRepository = AlunoRepositoryMock(),
    private val professorRepository: ProfessorRepository = ProfessorRepositoryMock()
) : ViewModel() {

    var email = mutableStateOf("")
        private set

    var senha = mutableStateOf("")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(newEmail: String) {
        email.value = newEmail
    }

    var db = Firebase.firestore

    fun onSenhaChange(newSenha: String) {
        senha.value = newSenha
    }

    fun login(onSuccessAluno: () -> Unit, onSuccessProfessor: () -> Unit) {

        if (email.value.isBlank() || senha.value.isBlank()) {
            errorMessage = "Preencha todos os campos"
            return
        }

        when {
            alunoRepository.logar(email.value, senha.value) -> {
                errorMessage = null
                onSuccessAluno()
            }

            professorRepository.logar(email.value, senha.value) -> {
                errorMessage = null
                onSuccessProfessor()
            }

            else -> {
                errorMessage = "Credenciais inválidas."
            }
        }
    }

}