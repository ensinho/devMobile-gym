package com.example.devmobile_gym.presentation.screens.authScreens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.devmobile_gym.data.repository.AlunoRepository
import com.example.devmobile_gym.data.repository.ProfessorRepository
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel
import com.example.devmobile_gym.domain.repository.ProfessorRepositoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginViewModel(
    private val alunoRepositoryModel: AlunoRepositoryModel = AlunoRepository(),
    private val professorRepositoryModel: ProfessorRepositoryModel = ProfessorRepository()
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


}