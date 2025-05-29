// com.example.devmobile_gym.domain.model.TreinoComData.kt
package com.example.devmobile_gym.domain.model

import com.google.firebase.firestore.PropertyName
import java.util.Date

// Renomeando para ser mais descritivo do que está no Firestore
// data: "29 de maio de 2025 às 10:34:01 UTC-3"
// id: "sEfwir3Gmdy0pSOmpFsh"

data class TreinoComData(
    @get:PropertyName("data") @set:PropertyName("data") var dataRealizacao: Date? = null, // Mapeia o campo 'data' do Firestore
    @get:PropertyName("id") @set:PropertyName("id") var treinoId: String = "" // Mapeia o campo 'id' do Firestore para 'treinoId'
)