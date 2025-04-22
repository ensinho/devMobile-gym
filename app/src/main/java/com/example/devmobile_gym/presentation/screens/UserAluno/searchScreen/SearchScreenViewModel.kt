package com.example.devmobile_gym.presentation.screens.UserAluno.searchScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.devmobile_gym.data.repository.AlunoRepositoryMock
import com.example.devmobile_gym.domain.repository.AlunoRepository

class SearchScreenViewModel (
    private val repository: AlunoRepository = AlunoRepositoryMock()

) : ViewModel(){
    var search = mutableStateOf("")
        private set

    fun onSearchChange(newSearch: String) {
        search.value = newSearch
    }
}