package com.example.devmobile_gym.data.repository

import android.util.Log
import com.example.devmobile_gym.domain.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ChatRepository {

    private val apiKey = "AIzaSyA9eMRWfMCXTKB15vVOz9r9SJ3hCjxA_8I"
    // URL da API corrigida para usar v1beta e o modelo gemini-2.0-flash
    // A chave da API será adicionada separadamente na URL
    private val baseUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent"



    suspend fun sendChat(messages: List<Message>): String = withContext(Dispatchers.IO) {
        val jsonBody = JSONObject().apply {
            put("contents", JSONArray().apply {
                messages.forEach { msg ->
                    put(JSONObject().apply {
                        put("role", msg.role) // "user" ou "model"
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", msg.content)
                            })
                        })
                    })
                }
            })
        }

        val url = URL("$baseUrl?key=$apiKey")
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json")
            doOutput = true
        }

        OutputStreamWriter(connection.outputStream).use { writer ->
            writer.write(jsonBody.toString())
            writer.flush()
        }

        val responseCode = connection.responseCode
        val stream = if (responseCode in 200..299) connection.inputStream else connection.errorStream
        val response = stream.bufferedReader().use { it.readText() }

        if (responseCode !in 200..299) {
            Log.e("ChatRepository", "Erro ao enviar mensagem. Código: $responseCode\nResposta: $response\nEnviado: ${jsonBody.toString(2)}")
            throw Exception("Erro da API ($responseCode): $response")
        }

        val jsonResponse = JSONObject(response)
        val contentArray = jsonResponse
            .getJSONArray("candidates")
            .getJSONObject(0)
            .getJSONObject("content")
            .getJSONArray("parts")

        contentArray.getJSONObject(0).getString("text")
    }

}