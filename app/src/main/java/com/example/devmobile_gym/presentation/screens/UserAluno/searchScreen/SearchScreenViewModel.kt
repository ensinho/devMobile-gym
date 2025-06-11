// presentation/screens/UserAluno/searchScreen/SearchScreenViewModel.kt
package com.example.devmobile_gym.presentation.screens.UserAluno.searchScreen

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.ExercicioRepository
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.domain.repository.ExercicioRepositoryModel
import com.example.devmobile_gym.utils.ImageUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// !!! NOVO DATA CLASS !!!: Para representar o exercício na UI com o Bitmap processado.
// Este data class deve estar no mesmo arquivo do ViewModel para facilitar.
data class ExercicioUiModel(
    val id: String? = null,
    val nome: String? = null,
    val grupoMuscular: String = "",
    val descricao: String? = null,
    val photoBitmap: Bitmap? = null // !!! APENAS BITMAP, SEM URL AQUI !!!
)

class SearchScreenViewModel (
    private val repository: ExercicioRepositoryModel = ExercicioRepository()
) : ViewModel(){

    private val _search = MutableStateFlow("")
    val search: StateFlow<String> = _search.asStateFlow()

    private val _exerciciosFiltrados = MutableStateFlow<List<ExercicioUiModel>>(emptyList())
    val exerciciosFiltrados: StateFlow<List<ExercicioUiModel>> = _exerciciosFiltrados.asStateFlow()

    private val _todosExercicios = MutableStateFlow<List<Exercicio>>(emptyList())

    init {
        viewModelScope.launch {
            getExercicios()
        }
    }

    private fun filtrarExercicios(texto: String) {
        val todos = _todosExercicios.value
        val exerciciosComFotos = todos.map { exercicio ->
            processarFotoExercicio(exercicio)
        }.filter {
            it.nome.toString().contains(texto, ignoreCase = true) ||
                    it.grupoMuscular.contains(texto, ignoreCase = true)
        }
        _exerciciosFiltrados.value = exerciciosComFotos
    }

    // Função auxiliar para processar a foto de cada exercício
    private fun processarFotoExercicio(exercicio: Exercicio): ExercicioUiModel {
        var bitmap: Bitmap? = null

        // !!! AGORA SEMPRE TENTAMOS DECODIFICAR O CAMPO 'imagem' COMO BASE64 COMPRIMIDO !!!
        if (!exercicio.imagem.isNullOrEmpty()) { // Usamos o campo 'imagem'
            val compressedBase64String = exercicio.imagem // O campo 'imagem' contém a Base64 comprimida
            val decompressedBase64String = ImageUtils.gzipDecompress(compressedBase64String)
            if (decompressedBase64String.isNotEmpty()) {
                bitmap = ImageUtils.base64ToBitmap(decompressedBase64String)
                if (bitmap == null) {
                    Log.e("SearchScreenVM", "Falha ao decodificar Base64 descomprimido para Bitmap para ${exercicio.nome} (Campo 'imagem').")
                }
            } else {
                Log.e("SearchScreenVM", "Falha ao descomprimir Base64 para ${exercicio.nome} (Campo 'imagem'): String vazia.")
            }
        } else {
            Log.d("SearchScreenVM", "Campo 'imagem' de ${exercicio.nome} está vazio.")
        }

        return ExercicioUiModel(
            id = exercicio.id,
            nome = exercicio.nome,
            grupoMuscular = exercicio.grupoMuscular,
            descricao = exercicio.descricao,
            photoBitmap = bitmap // Apenas o Bitmap é passado
        )
    }

    fun onSearchChange(newSearch: String) {
        _search.value = newSearch
        filtrarExercicios(newSearch)
    }

    private suspend fun getExercicios() {
        val exerciciosBrutos = repository.getAllExercicios()
        _todosExercicios.value = exerciciosBrutos

        val exerciciosComFotos = exerciciosBrutos.map { exercicio ->
            processarFotoExercicio(exercicio)
        }
        _exerciciosFiltrados.value = exerciciosComFotos
    }
}