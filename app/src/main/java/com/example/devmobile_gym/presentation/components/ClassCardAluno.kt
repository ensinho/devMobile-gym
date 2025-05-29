package com.example.devmobile_gym.presentation.components

import android.R.id.bold
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.components.ui.theme.components.CustomButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight.Companion.Bold

@Composable
fun ClassCardAluno(
//    onQuantidadeChange: (Int) -> Unit,
    idAula: String,
    data: String,
    aula: String,
    professor: String,
    hora: String,
    quantAtual: Int,
    quantMaxima: Int,
    enabled: Boolean = true,
    onInscricaoClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Data: $data às $hora",
                    color = Color(0xFFAAAEB6),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Arte Marcial: $aula",
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Professor: $professor",
                    color = Color(0xFFAAAEB6),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                ButtonPersonalizado(
                    text = "Tenho interesse",
                    onClick = { onInscricaoClick(idAula) },
                    backgroundColor = if (quantAtual >= quantMaxima) Color.Gray else Color(0xFF5D98DD),
                    enabled = (quantAtual < quantMaxima) || enabled,
                    fillWidth = false,
                    modifier = Modifier.width(130.dp)
                )

                Text(
                    text = "$quantAtual/$quantMaxima",
                    modifier = Modifier.width(75.dp),
                    color = Color.White,
                    fontWeight = Bold
                )
            }
        }
    }
}