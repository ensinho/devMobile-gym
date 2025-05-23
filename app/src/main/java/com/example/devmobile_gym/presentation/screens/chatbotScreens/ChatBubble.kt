package com.example.devmobile_gym.presentation.screens.chatbotScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ChatBubble(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    val isTyping = message.text == "Digitando..."
    val isUserMessage = message.sender == Sender.USER
    val backgroundColor = if (isUserMessage) Color(0xFF4A6572) else Color(0xFF2E3238)
    val alignment = if (isUserMessage) Alignment.CenterEnd else Alignment.CenterStart
    val textColor = Color.White

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 2.dp),
        contentAlignment = alignment
    ) {
        Surface(
            color = backgroundColor,
            modifier = Modifier
                .widthIn(max = 280.dp) // <--- define largura máxima
                .wrapContentWidth()
                .wrapContentHeight(),
            shape =
                if (isUserMessage)
                    RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 0.dp, topStart = 16.dp, topEnd = 16.dp)
                else
                    RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 16.dp, topStart = 16.dp, topEnd = 16.dp)
        ) {
            if (isTyping) {
                TypingDotsIndicator()
            } else {
                Column(
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(
                        text = if (isUserMessage) "Você" else "BOT",
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                    Text(
                        text = message.text,
                        color = textColor,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}