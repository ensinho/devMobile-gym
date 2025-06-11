package com.example.devmobile_gym.presentation.components

import android.graphics.Bitmap // Importar Bitmap
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image // Importar Image para exibir Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment // Importar Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap // Importar asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// coil3.compose.AsyncImage // REMOVIDO: Não mais necessário para exibir o Bitmap diretamente
// coil3.compose.AsyncImagePainter // REMOVIDO: Não mais necessário

@Composable
fun CustomExerciseSearchCard(
    exercicio: String,
    description: String,
    photoBitmap: Bitmap? // NOVO PARÂMETRO: Agora recebe o Bitmap da foto
) {
    var expanded by remember { mutableStateOf(false) }
    val desc: String = if (description.isNotEmpty()) {
        description
    } else {
        "Sem descrição para o exercício."
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
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically // Centraliza verticalmente os itens da Row
            ){
                // Lógica para exibir a imagem ou o fallback
                Box(
                    modifier = Modifier
                        .size(50.dp) // Tamanho fixo do Box para a imagem/ícone
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.2f))
                        .border(1.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center // Centraliza o conteúdo dentro do Box
                ) {
                    if (photoBitmap != null) {
                        // Se um Bitmap for fornecido, exiba-o
                        Image(
                            bitmap = photoBitmap.asImageBitmap(), // Converte Bitmap para ImageBitmap
                            contentDescription = "Imagem do exercício",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize() // Preenche o Box
                        )
                    } else {
                        // Se não houver Bitmap, exiba o ícone de fallback
                        Icon(
                            imageVector = Icons.Default.Info, // Seu ícone de fallback
                            contentDescription = "Sem imagem",
                            tint = Color.Gray,
                            modifier = Modifier.fillMaxSize(0.7f) // Ocupa 70% do Box para não ficar muito grande
                        )
                    }
                }

                Spacer(Modifier.width(30.dp))

                // Texto do exercício e botão de expandir/recolher
                Row(
                    modifier = Modifier.weight(1f), // Faz o texto e o ícone ocuparem o espaço restante
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = exercicio,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f) // Faz o texto ocupar o máximo de espaço possível
                    )
                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.size(21.dp) // Tamanho do botão
                    ) {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = if (expanded) "Recolher" else "Expandir",
                            tint = Color.White,
                            modifier = Modifier.size(35.dp) // Tamanho do ícone dentro do botão
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            // Descrição expandida ou recolhida
            Text(
                text = if (expanded) desc else if (desc.length > 100) desc.substring(0, 100) + "..." else desc,
                color = Color(0xFFAAAEB6),
                fontSize = 15.sp
            )
            Spacer(Modifier.height(10.dp))

        }
    }
}