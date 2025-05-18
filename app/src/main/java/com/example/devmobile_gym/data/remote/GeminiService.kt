package com.example.devmobile_gym.data.remote

import com.google.ai.client.generativeai.GenerativeModel

class GeminiService(apiKey: String) {



    private val generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = apiKey
        )

     suspend fun sendMessage(prompt: String) : String{
        val response =  generativeModel.generateContent(prompt)
        return response.text ?: "Erro na resposta"
    }
}