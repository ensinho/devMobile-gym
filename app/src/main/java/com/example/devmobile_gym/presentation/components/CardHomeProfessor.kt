package com.example.devmobile_gym.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devmobile_gym.R

@Composable
fun CardHomeProfessor(
    texto: String,
    icone: Int,
    onClick: () -> Unit // ação de clique no card inteiro
) {
    Card(
        modifier = Modifier
            .padding(27.dp)
            .fillMaxWidth()
            .clickable { onClick() } // Card inteiro clicável
            .border(
                width = 0.5.dp,
                color = Color(0xFF343434),
                shape = RoundedCornerShape(7.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF343434)),
        shape = RoundedCornerShape(7.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = texto,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = icone),
                contentDescription = "ícone",
                modifier = Modifier.size(27.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}


//@Preview
//@Composable
//fun PreviewPainel(){
//    CardHomeProfessor("User", icone = R.drawable.ic_caneta)
//}