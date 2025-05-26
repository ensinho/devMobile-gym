package com.example.devmobile_gym.presentation.screens.UserAluno.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.AlunoRepository
import com.example.devmobile_gym.domain.model.Usuario.Aluno
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.log

class ProfileViewModel (
    private val alunoRepositoryModel: AlunoRepositoryModel = AlunoRepository()
): ViewModel()  {

    private val _aluno = mutableStateOf<Aluno?>(null)


    val aluno: State<Aluno?> = _aluno

    init {
        carregarAluno()
    }

    private fun carregarAluno() {

        viewModelScope.launch {
            val aluno = alunoRepositoryModel.getAlunoLogado()
            println("🔥 Aluno carregado: $aluno")
            _aluno.value = aluno
        }
    }


}