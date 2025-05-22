package com.example.devmobile_gym.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devmobile_gym.domain.model.Message
import com.example.devmobile_gym.domain.model.Sender

@Composable
fun CardMessage(message: Message) {
    val isUser = message.role.lowercase() == "user"

    val backgroundColor = if (isUser) Color(0xFF343A3F) else Color(0xFFE5E5EA)
    val alignment = if (isUser) Alignment.End else Alignment.Start
    val shape = if (isUser)
        RoundedCornerShape(12.dp, 0.dp, 12.dp, 12.dp)
    else
        RoundedCornerShape(0.dp, 12.dp, 12.dp, 12.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(color = backgroundColor, shape = shape)
                .padding(12.dp)
        ) {
            Text(
                text = message.content,
                fontSize = 18.sp,
                color = if (isUser) Color.White else Color.Black
            )
        }
    }
}


@Preview
@Composable
private fun Preview() {
    CardMessage(Message(role = "user", content = "Olá, mundo!"))
}
