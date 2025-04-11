package com.example.devmobile_gym.ui.theme.components

import android.graphics.drawable.Icon
import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTarget
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devmobile_gym.R


val semana: MutableList<String> = mutableListOf("SEG", "TER", "QUA", "QUI", "SEX", "SAB", "DOM")


@Composable
fun CardCalendario() {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E3238)),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(10.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = "Janeiro", fontSize = 22.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(5.dp))
            Box(
            ){
                Divider(color = Color(0xFF5D98DD), thickness = 0.8.dp, modifier =  Modifier.width(242.dp))
            }
            Spacer(modifier = Modifier.height(5.dp))

            diasdaSemana(semana)
            bolinhas()
        }

    }
}



@Composable
fun diasdaSemana(itens: MutableList<String>) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        itens.forEachIndexed { index, item ->
            Text(text = item, fontSize = 10.sp, color = Color.White)

            if(index != itens.lastIndex){
                Spacer(Modifier.width(19.dp))
            }else{
                Spacer(Modifier.width(2.dp))
            }
        }
    }
}

@Composable
fun bolinhas() {
    val dias: MutableList<String> = mutableListOf("SEG", "TER", "QUA", "QUI", "SEX", "SAB", "DOM")

    for (i in 0 until 4) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            for (i in 0 until 7) {
                icon_bolinhas()
                Spacer(Modifier.width(1.dp))
            }
        }
    }
}

@Composable
fun icon_bolinhas(checked: Boolean = true) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(20.dp)
            .clip(CircleShape)
            .background(if (checked) Color(0xFF267FE7) else Color(0xFFEDEFF1)),
    )
}
//Color(0xFF267FE7) else Color(0xFFEDEFF1)
//colocar icone bola azul
// usar o forEach
// ajustar

@Preview
@Composable
fun PreviewCardCalendario(){
    CardCalendario()
}