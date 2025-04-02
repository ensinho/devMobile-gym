package com.example.devmobile_gym.ui.theme.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.ui.theme.components.CustomButton

@Composable
fun CustomCard(
    treino: String,
    description: List<String>,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var desc = description.joinToString(", ")
    if (expanded && description.isNotEmpty()) {
        if (description.size <= 3) {
            desc = description.joinToString(", ")
        } else {
            desc = description.joinToString(", ")
        }
    } else if (description.isEmpty() && expanded) {
        desc = "Nenhum exercício adicionado."
    } else {
        if (description.size <= 3) {
            desc = description.joinToString(", ")
        } else {
            desc = description.take(3).joinToString(", ") + ", ..."
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E3238))
    ) {
        Column (modifier = Modifier.padding(20.dp)) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = treino,
                    fontSize = 22.sp,
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
                text = desc,
                color = Color(0xFFAAAEB6),
                fontSize = 15.sp
                )
            Spacer(modifier = Modifier.height(3.dp))
            CustomButton(buttonText, onClick = { onButtonClick() })

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCard() {
    CustomCard("Treino A", listOf("Panturrilha em pé (máquina)", "Cadeira Extensora (máquina)", "Agachamento Livre", "Exercício 4", "Exercício 5", "Exercício 6"), "Iniciar Treino", {})
}