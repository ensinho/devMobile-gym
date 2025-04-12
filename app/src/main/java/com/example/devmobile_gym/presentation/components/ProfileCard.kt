package com.example.devmobile_gym.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileCard(
    name: String,
    userId: String,
    weight: String,
    height: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(55.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF267FE7)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícone de perfil substituindo o avatar
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Avatar",
                tint = Color.White,
                modifier = Modifier.size(109.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Nome do Fulano",
                    fontSize = 27.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.DarkGray, // Cor da sombra
                            offset = Offset(1f, 3f), // Posição da sombra (X, Y)
                            blurRadius = 8f // Intensidade do desfoque
                        )
                    )
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Sharp.Info,
                        contentDescription = "Matrícula",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = userId,
                        fontSize = 14.sp,
                        color = Color.White,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.DarkGray, // Cor da sombra
                                offset = Offset(1f, 3f), // Posição da sombra (X, Y)
                                blurRadius = 8f // Intensidade do desfoque
                            )
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    weightBox("70Kg")
                    Spacer(Modifier.padding(end = 80.dp))
                    heightBox("170cm")

                }
            }
        }
    }
}

@Composable
fun weightBox(value: String) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Card(
            modifier = Modifier.height(28.dp).width(7.dp),
            shape = RoundedCornerShape(55.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),

        ) {
            Text(".", color = Color(0xFFF0F4FF))
        }
        Spacer(modifier = Modifier.width(3.dp))
        Column (){
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = "Peso",
                fontSize = 9.sp,
                color = Color.White
            )
        }
    }
}
@Composable
fun heightBox(value: String) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Card(
            modifier = Modifier.height(28.dp).width(7.dp),
            shape = RoundedCornerShape(55.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),

            ) {
            Text(".", color = Color(0xFFF0F4FF))
        }
        Spacer(modifier = Modifier.width(3.dp))
        Column (){
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = "Altura",
                fontSize = 9.sp,
                color = Color.White
            )
        }
    }
}

@Preview (showBackground = true)
@Composable
private fun PreviewProfileCard() {
    ProfileCard("Nome do Fulano", "12345", "69", "1,70")
//    weightOrHeightBox("70Kg")
}