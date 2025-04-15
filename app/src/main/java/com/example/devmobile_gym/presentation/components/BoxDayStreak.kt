package com.example.devmobile_gym.presentation.components

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devmobile_gym.R

val icone = R.drawable.fire_svgrepo_com
val texto = ""
val texto1 = ""

@Composable
fun BoxDayStreak(texto: String, icone: Int) {
    Card(
        modifier = Modifier
            .padding(9.dp)
            .width(70.dp)
            .border(
                width =0.5.dp,
                color = Color(0xFF000000),
                shape = RoundedCornerShape(7.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF8F9092)),
        shape = RoundedCornerShape(7.dp),


    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(3.dp)

        ) {

            Image(
                painter = painterResource(id = icone),
                contentDescription = "Fogo",
                modifier = Modifier.size(9.dp)
            )


            Spacer(modifier = Modifier.width(2.dp))


            Column {
                Text(text = texto, fontSize = 6.sp, color = Color(0xFFFFFFFF))
                Text(text = texto1, fontSize = 6.sp, color = Color.White)
            }
        }
    }
}

@Preview
@Composable
fun PreviewBoxDayStreak() {
    BoxDayStreak(texto, icone)
}

