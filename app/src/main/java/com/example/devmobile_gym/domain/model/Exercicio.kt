package com.example.devmobile_gym.domain.model

data class Exercicio(
    val id: String? = "",
    val nome: String? = "",
    val grupoMuscular: String = "",
    val imagem: String? = "",
    val linkVideo: String? = "", // nao esta sendo usado, desconsiderar
    val descricao: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Exercicio) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

