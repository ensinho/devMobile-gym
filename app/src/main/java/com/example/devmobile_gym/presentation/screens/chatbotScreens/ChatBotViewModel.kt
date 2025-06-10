package com.example.devmobile_gym.presentation.screens.chatbotScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatbotViewModel : ViewModel() {

    // Estado da UI: lista de mensagens com tipo (usuário ou bot)
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    // Estado de carregamento (enquanto aguarda resposta)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // estado para a "mensagem temporária" do bot
    private val typingIndicator = ChatMessage("Digitando...", Sender.BOT)

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = "AIzaSyDuTz2iE4RsJTnrbmJpQgnUDVm3lEoP1zA",
        generationConfig = generationConfig {
            temperature = 0.7f
            topK = 16
            topP = 0.8f
            maxOutputTokens = 1024
        },
        systemInstruction = content {
            text("""
            Você é um instrutor da Academia Unifor especializado em fitness e musculação. 
            Suas respostas devem ser focadas em:
            - Técnicas corretas de execução de exercícios
            - Dicas de nutrição esportiva
            - Periodização de treinos
            - Respostas a dúvidas sobre equipamentos e exercícios
            - Motivação e acompanhamento de progresso

            Mantenha as respostas:
            ✔️ Baseadas em evidências científicas
            ✔️ Práticas e aplicáveis
            ✔️ Com linguagem clara e acessível
            ✔️ Adaptáveis para diferentes níveis de condicionamento

            ❌ Evite dar conselhos médicos ou sobre lesões
            ❌ Não recomende suplementos não regulamentados
            ❌ Nunca sugira práticas perigosas ou não comprovadas
            ❌ Não fale sobre outras redes de academia, fora a Academia Unifor, ou marcas de suplementos

            Sempre que relevante, inclua:
            - Variações de exercícios para diferentes níveis
            - Número de séries e repetições sugeridas
            - Dicas de segurança e prevenção de lesões
            - Alternativas para diferentes tipos de equipamentos
            
            Instruções de mensagens:
            - Busque ser descontraído, mas sempre mantendo a profissionalidade e o respeito
            - Utilize Emojis quando for possível
            - Sempre responda em português
            - Não faça textos muito grandes. O usuário quer apenas tirar dúvidas, então seja objetivo e claro
            - Tire as dúvidas dos alunos, mas sempre indique que o aluno procure um instrutor e/ou nutricionista da Academia Unifor para ajuda-lo melhor
        """.trimIndent())
        }
    )

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        val updatedMessages = _messages.value + ChatMessage(userText, Sender.USER)
        _messages.value = updatedMessages + typingIndicator
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content { text(userText) }
                )
                val botReply = response.text ?: "Desculpe, não consegui responder isso agora."

                // Remove "digitando..." e adiciona a resposta real
                _messages.value = _messages.value.dropLast(1) + ChatMessage(botReply, Sender.BOT)
            } catch (e: Exception) {
                _messages.value = _messages.value.dropLast(1) + ChatMessage("Erro: ${e.localizedMessage}", Sender.BOT)
            } finally {
                _isLoading.value = false
            }
        }
    }

}

// Representa uma mensagem no chat
data class ChatMessage(val text: String, val sender: Sender)

// Enum para diferenciar mensagens do usuário e do bot
enum class Sender { USER, BOT }
