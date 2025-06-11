package com.example.devmobile_gym.presentation.components

import android.graphics.Bitmap // Importar Bitmap
import androidx.compose.foundation.Image // Importar Image para exibir Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info // Ícone de fallback (ou AddAPhoto)
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap // Importar asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// coil3.compose.AsyncImage // REMOVIDO: Não mais necessário
// coil3.compose.AsyncImagePainter // REMOVIDO: Não mais necessário

// thead da tabela - Removido "ANTERIOR"
private val thead: MutableList<String> = mutableListOf("SÉRIE", "KG", "REPS")


@Composable
fun ExerciseCard(
    exercicioId: String,
    title: String,
    photoBitmap: Bitmap?, // NOVO PARÂMETRO: Recebe o Bitmap da foto
    quantSeries: Int = 3,
    quantReps: Int = 12,
    peso: Int = 20,
    seriesConcluidasState: Map<String, Boolean>,
    onSerieCheckedChange: (exercicioId: String, serieIndex: Int, isChecked: Boolean) -> Unit
) {
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
            ExerciseTitle(title, photoBitmap) // PASSA O BITMAP AQUI
            ExerciseTHead()
            for (i in 0..<quantSeries) {
                TRowCell(
                    exercicioId = exercicioId,
                    serieIndex = i,
                    trowInfo = mutableListOf((i + 1), peso, quantReps),
                    isChecked = seriesConcluidasState["$exercicioId-$i"] ?: false,
                    onCheckedChange = { checked ->
                        onSerieCheckedChange(exercicioId, i, checked)
                    }
                )
            }
        }
    }
}

@Composable
private fun TRowCell(
    exercicioId: String,
    serieIndex: Int,
    trowInfo: MutableList<Int>,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, end = 0.dp)
            .background( if (isChecked) Color(0xFF2C6111) else Color(0xFF414141)),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        for (i in 0 until thead.size) {
            Box(
                modifier = Modifier
                    .weight(if (thead[i] == "KG") 0.8f else if (thead[i] == "REPS") 1f else 1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = trowInfo[i].toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8F9092),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = (if (thead[i] == "KG" || thead[i] == "REPS") 20.dp else 0.dp))
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(0.7f),
            contentAlignment = Alignment.Center
        ) {
            // Supondo que CustomCheckbox é um composable que você tem
            CustomCheckbox(checked = isChecked, onCheckedChange = onCheckedChange)
        }
    }
}

@Composable
private fun ExerciseTitle(title: String, photoBitmap: Bitmap?) { // NOVO PARÂMETRO: Bitmap?
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
            if (photoBitmap != null) { // LÓGICA PARA EXIBIR O BITMAP
                Image(
                    bitmap = photoBitmap.asImageBitmap(), // Converte Bitmap para ImageBitmap
                    contentDescription = "Ícone do exercício",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else { // Fallback se o Bitmap for nulo
                Icon(
                    imageVector = Icons.Default.Info, // Ícone de fallback (ou outro como AddAPhoto)
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
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(if (item == "KG") 0.8f else if (item == "REPS") 1f else 1f)
                    .fillMaxWidth()
            )
        }
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Ícone de Check",
            tint = Color(0xFF8F9092),
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(bottom = 2.dp)
        )
    }
}

// Estrutura de dados para cada linha da tabela (provavelmente não usada diretamente no composable)
private data class EditableRow(val serie: String, val kg: String, val reps: String)