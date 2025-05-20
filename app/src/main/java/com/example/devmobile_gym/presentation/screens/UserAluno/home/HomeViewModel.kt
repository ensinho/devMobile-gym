package com.example.devmobile_gym.presentation.screens.UserAluno.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.TreinoRepository
import com.example.devmobile_gym.domain.model.Treino
import com.example.devmobile_gym.domain.repository.TreinoRepositoryModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel (
) : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val treinoRepository: TreinoRepositoryModel = TreinoRepository()

    private val _treinos = MutableStateFlow<List<Treino>>(emptyList())
    val treinos: StateFlow<List<Treino>> = _treinos

    init {
        carregarTreinos()
    }

    private fun carregarTreinos() {
        viewModelScope.launch {
            val listaTreinos = treinoRepository.getTreinos()
            _treinos.value = listaTreinos
        }
    }
}