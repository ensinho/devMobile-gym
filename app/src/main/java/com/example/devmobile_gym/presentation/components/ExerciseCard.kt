package com.example.devmobile_gym.presentation.components

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Check
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter

// thead da tabela
//deixei global pq tem vários métodos que precisam acessar essa lista
private val thead: MutableList<String> = mutableListOf("SÉRIE", "ANTERIOR", "KG", "REPS")


@Composable
fun ExerciseCard(title: String, url : String, quantSeries: Int = 3, quantReps: Int = 12, peso: Int = 20) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            ExerciseTitle(title, url)
            ExerciseTHead()
            for (i in 0..<quantSeries) {

                TRowCell(mutableListOf((i+1), quantReps, quantReps, peso))
            }
        }
    }
}

@Composable
private fun TRowCell(trowInfo: MutableList<Int>) {
    var isChecked by remember { mutableStateOf(false) }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, end = 0.dp)
            .background( if (isChecked) Color(0xFF2C6111) else Color(0xFF414141)),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        for (i in 0..thead.size) {
            Box(
                modifier = Modifier
                    .weight(if (i < thead.size) 1f else 0.7f),
                contentAlignment = Alignment.Center
            ) {
                if (i < thead.size) {
                    
                    Text(
                        text = trowInfo[i].toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8F9092),
                        textAlign = TextAlign.Center, // Centraliza o texto dentro do espaço
                        modifier = Modifier.padding(start = (if (thead[i] == "KG" || thead[i] == "REPS") 20.dp else if (thead[i] == "ANTERIOR") 10.dp else 0.dp))
                    )
                } else if (i == thead.size){
                    CustomCheckbox(checked = isChecked, onCheckedChange = { isChecked = it })
                }
            }
        }
    }
}
@Composable
private fun ExerciseTitle(title: String, url: String) {
    Row(
        modifier = Modifier
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.2f))
                .border(1.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
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
        }
        Spacer(Modifier.width(20.dp))

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF267FE7),
            fontSize = 18.sp,
            modifier = Modifier.weight(4f)
        )
    }
}



@Composable
private fun ExerciseTHead() {
    Row (
        modifier = Modifier
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        for (item in thead) {
            Text(
                text = item,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8F9092),
                fontSize = 18.sp,
                textAlign = TextAlign.Center, // Centraliza o texto dentro do espaço
                modifier = Modifier
                    .weight(if (item == "ANTERIOR") 1.2f else if (item == "KG") 0.8f else if (item == "REPS") 1f else 1f)
                    .fillMaxWidth() // Garante que o Text ocupe todo o espaço do weight()
            )
        }
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Ícone de Check",
            tint = Color(0xFF8F9092),
            modifier = Modifier
                .weight(0.5f) // Pode adicionar modificadores extras aqui
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(bottom = 2.dp)
        )
    }

}



// Estrutura de dados para cada linha da tabela
private data class EditableRow(val serie: String, val kg: String, val reps: String)




