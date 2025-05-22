//package com.example.devmobile_gym.presentation.screens.UserAluno.chatBot
//
//import androidx.compose.runtime.State
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.devmobile_gym.domain.model.Message.kt
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//class ChatBotViewModel : ViewModel() {
//    private val _message = mutableStateListOf<Message.kt>()
//    val message: List<Message.kt> = _message
//
//    private val _userInputs = mutableStateOf("")
//    val userInputs: State<String> = _userInputs
//
//    fun onUserInputChange(newInput: String) {
//        _userInputs.value = newInput
//
//    }
//
//    fun sendMessage(userMessage: String) {
//        if (userMessage.isNotEmpty()) {
//            _message.add(Message.kt(text = userMessage, isUser = true))
//            _userInputs.value = ""
//
//            // Resposta do chatbot
//            simulateChatBotResponse(userMessage)
//        }
//
//    }
//
//    private fun generateBotResponse(userMessage: String): String {
//        val msg = userMessage.lowercase()
//
//        return when {
//            msg.contains("olá") -> "Olá! Como posso ajudar?"
//            msg.contains("treino") -> "Posso te explicar sobre exercícios e máquinas!"
//            msg.contains("panturrilha em pé") -> "A panturrilha em pé na máquina foca nos músculos gastrocnêmios. Mantenha as pernas estendidas e eleve os calcanhares o máximo possível."
//            msg.contains("panturrilha sentada") -> "A panturrilha sentada ativa mais o sóleo. Sente-se, apoie o peso sobre os joelhos e eleve os calcanhares."
//            msg.contains("cadeira extensora") -> "Esse exercício trabalha o quadríceps. Sente-se na máquina e estenda os joelhos até quase travá-los."
//            msg.contains("cadeira flexora") -> "Trabalha o posterior de coxa. Sente-se e flexione os joelhos para trás, contra a resistência."
//            msg.contains("agachamento livre") -> "É um exercício composto que ativa quadríceps, glúteos e lombar. Mantenha a postura ereta, desça com controle e suba com força."
//            msg.contains("agachamento sumô") -> "Com as pernas afastadas e os pés para fora, desça mantendo o tronco reto. Trabalha interno de coxa e glúteos."
//            msg.contains("supino inclinado") -> "Foca na parte superior do peitoral. Deite-se no banco inclinado e empurre os halteres para cima, alinhando com o peito."
//            msg.contains("peck deck") -> "Máquina de peitoral. Mantenha os cotovelos levemente flexionados e feche os braços até se aproximarem no centro."
//            msg.contains("tríceps polia") -> "Trabalha os tríceps. Com os cotovelos fixos, estenda os braços para baixo na polia alta."
//            msg.contains("rosca scott") -> "Foca no bíceps. Use o banco Scott, com os braços apoiados, e flexione os cotovelos até os halteres se aproximarem dos ombros."
//            msg.contains("leg press") -> "Trabalha quadríceps, glúteos e posteriores. Com os pés na plataforma, empurre controladamente e nunca trave os joelhos."
//            msg.contains("remada curvada") -> "Foca nas costas. Com o tronco inclinado, puxe a barra em direção ao abdômen mantendo as escápulas contraídas."
//            msg.contains("remada baixa") -> "Na máquina ou polia baixa, puxe o cabo em direção ao abdômen, mantendo o tronco reto e cotovelos próximos ao corpo."
//            msg.contains("rosca direta") -> "Trabalha bíceps. Em pé, com barra ou halteres, flexione os cotovelos sem balançar o corpo."
//            msg.contains("crucifixo reto") -> "Deitado no banco reto, com halteres, abra os braços lateralmente e una acima do peito com controle."
//            msg.contains("abdominal infra") -> "Foca no abdômen inferior. Deitado, eleve as pernas com o quadril e desça controladamente."
//            else -> "Desculpe, ainda estou aprendendo. Tente perguntar de outra forma ou pergunte sobre um exercício específico!"
//        }
//    }
//
//
//    private fun simulateChatBotResponse(userMessage: String) {
//        viewModelScope.launch {
//            delay(1000)
//            val botResponse = generateBotResponse(userMessage)
//            _message.add(Message.kt(text = botResponse, isUser = false))
//        }
//    }
//}