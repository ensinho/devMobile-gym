package com.example.components.ui.theme.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color = Color(0xFF5D98DD),
    fillWidth: Boolean = true,
    enabled: Boolean = true // <- novo parâmetro
) {
    val buttonModifier = Modifier
        .then(if (fillWidth) Modifier.fillMaxWidth().padding(8.dp) else modifier)

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            disabledContainerColor = Color.Gray // cor quando desabilitado
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = buttonModifier,
        enabled = enabled // <- usa o parâmetro aqui
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCustomButton() {
    CustomButton(
        text = "Aulas & Funcionais",
        onClick = { /* Handle button click */ },

        )
}