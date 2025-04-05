package com.example.devmobile_gym.ui.theme.components

import android.widget.Space
import androidx.compose.animation.R
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import org.w3c.dom.Text


val dias: MutableList<String> = mutableListOf("SEG", "TER", "QUA", "QUI", "QUI", "SEX", "SAB", "DOM")
@Composable
fun CustomCalendario() {

        Card(
            modifier = Modifier
                .animateContentSize(),
            shape = RoundedCornerShape(9.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF3B3B3B))
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                diasSemanas(dias)
                Spacer(Modifier.height(9.dp))
                bolinha()

            }
        }
    }


@Composable
fun diasSemanas(List: MutableList<String>) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.background(Color(0xFF3B3B3B)),
    ) {

        for (i in 0..List.size - 1) {
            if (i == List.size - 1) {
                Text(List[i],
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                     )
                break
            }
            Text(List[i],
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            
            Spacer(Modifier.width(10.dp))
        }
    }
}

@Composable
fun bolinha() {
    val List =  dias
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,

        modifier = Modifier
            .background(Color(0xFF3B3B3B))
            .fillMaxWidth()

        ) {

        for(i in 0..List.size-1){
            if(i==List.size-1){
                IconeBola(true)
                break
            }
            IconeBola(true)

        }
    }
}

@Composable
fun IconeBola(checked: Boolean = false){
    val backgroundColor = null
    Box(
        modifier = Modifier
            .size(21.dp)
            .clip(CircleShape)
            .background(if(checked) Color(0xFF267FE7) else Color(0xFFEDEFF1))

    )
}


@Preview(showBackground = true)
@Composable
private fun PreviewCalendario(){
    CustomCalendario()

}

