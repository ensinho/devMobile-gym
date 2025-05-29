package com.example.devmobile_gym.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter

@Composable
fun CustomExerciseSearchCard(
    exercicio: String,
    description: String,
    url: String
) {
    var expanded by remember { mutableStateOf(false) }
    var desc: String
    if (description.isNotEmpty()) {
        desc = description
    } else {
        desc = "Sem descrição para o exercício."
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E3238))
    ) {
        Column (modifier = Modifier.padding(top = 15.dp, bottom = 10.dp, start = 13.dp, end = 18.dp)) {

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ){
                if (url.isNotBlank()) {
                    println("🔵 Tentando carregar imagem: $url")
                    AsyncImage(
                        model = url,
                        contentDescription = "Ícone do exercício",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)  // Tamanho fixo de 64x64 dp
                            .clip(CircleShape)
                            .background(Color.Gray.copy(alpha = 0.2f))
                            .border(1.dp, Color.White, CircleShape),

                        onState = { state ->
                            when (state) {
                                is AsyncImagePainter.State.Loading -> println("🟡 Carregando imagem...")
                                is AsyncImagePainter.State.Success -> println("✅ Imagem carregada com sucesso.")
                                is AsyncImagePainter.State.Error -> println("🔴 Erro ao carregar imagem.")
                                is AsyncImagePainter.State.Empty -> println("⚪ URL da imagem vazia.")
                            }
                        }

                    )
                } else {
                    println("⚠️ URL da imagem está vazia. Exibindo fallback.")
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Fallback",
                        tint = Color.Gray,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(Modifier.width(30.dp))
                Text(
                    text = exercicio,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                IconButton(
                    onClick = {expanded = !expanded},
                    modifier = Modifier.size(21.dp)
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) "Recolher" else "Expandir",
                        tint = Color.White,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if (expanded) desc else desc.substring(0, 100) + "...",
                color = Color(0xFFAAAEB6),
                fontSize = 15.sp
            )
            Spacer(Modifier.height(10.dp))

        }
    }
}
