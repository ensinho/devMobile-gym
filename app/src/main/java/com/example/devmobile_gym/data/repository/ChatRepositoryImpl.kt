package com.example.devmobile_gym.data.repository

import com.example.devmobile_gym.domain.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ChatRepository {
    private val apiUrl = "http://<IP>:<PORT>/chat" // troque pelo seu IP real

    suspend fun sendChat(messages: List<Message>): String = withContext(Dispatchers.IO) {
        val jsonBody = JSONObject().apply {
            put("messageHistory", JSONArray().apply {
                messages.forEach {
                    put(JSONObject().apply {
                        put("role", it.role)
                        put("content", it.content)
                    })
                }
            })
        }

        val url = URL(apiUrl)
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json")
            doOutput = true
            outputStream.write(jsonBody.toString().toByteArray())
        }

        val response = connection.inputStream.bufferedReader().use { it.readText() }
        val responseObject = JSONObject(response)
        responseObject.getString("reply")
    }
}
