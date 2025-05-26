package com.example.devmobile_gym.presentation.components


import androidx.compose.foundation.layout.fillMaxWidth
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
fun ButtonPersonalizado(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color = Color(0xFF5D98DD),
    fillWidth: Boolean = true,
    enabled: Boolean = true  // Novo parâmetro com valor padrão true
) {
    val buttonModifier = Modifier
        .then(if (fillWidth) Modifier.fillMaxWidth() else modifier)
        .padding(8.dp)

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            disabledContainerColor = Color.Gray  // Cor quando desabilitado
        ),
        shape = RoundedCornerShape(60.dp),
        modifier = buttonModifier,
        enabled = enabled  // Passando o parâmetro para o Button
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCustomButton() {
    ButtonPersonalizado(
        text = "Aulas & Funcionais",
        onClick = { /* Handle button click */ },

        )
}