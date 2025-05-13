package com.example.devmobile_gym.presentation.screens.UserAluno.searchScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.devmobile_gym.data.repository.AlunoRepository
import com.example.devmobile_gym.domain.repository.AlunoRepositoryModel

class SearchScreenViewModel (
    private val repository: AlunoRepositoryModel = AlunoRepository()

) : ViewModel(){
    var search = mutableStateOf("")
        private set

    fun onSearchChange(newSearch: String) {
        search.value = newSearch
    }
}