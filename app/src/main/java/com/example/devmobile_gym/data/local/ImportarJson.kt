package com.example.devmobile_gym.data.local

import android.content.Context
import android.util.Log
import com.example.devmobile_gym.domain.model.Exercicio
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

fun importarExerciciosParaFirestore(context: Context, onComplete: (Boolean) -> Unit) {
    try {
        val inputStream = context.assets.open("exercicios.json")
        val json = inputStream.bufferedReader().use { it.readText() }

        val gson = Gson()
        val listType = object : TypeToken<List<Exercicio>>() {}.type
        val exercicios: List<Exercicio> = gson.fromJson(json, listType)

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("exercicios")

        for (exercicio in exercicios) {
            collectionRef.add(exercicio)
        }

        onComplete(true)
    } catch (e: Exception) {
        e.printStackTrace()
        onComplete(false)
    }
}

