package com.example.devmobile_gym.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devmobile_gym.R

@Composable
fun BoxSeta() {
    Card(
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E3238)),
        modifier = Modifier
            .width(60.dp)
    ) {
        Row(

        ) {

            Button(
                onClick = {

                },
                modifier = Modifier
                    .size(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E3238),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(0.dp),

            ) {

                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Seta"
                )
            }
                Text(text = "Filtro", color = Color.White)
            }
        }
    }


@Preview
@Composable
fun BoxSetaPreview(){
    BoxSeta()
}




