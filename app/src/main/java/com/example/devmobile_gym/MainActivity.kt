package com.example.devmobile_gym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.ui.theme.DevMobilegymTheme
import com.example.devmobile_gym.ui.theme.components.CustomCard
import com.example.devmobile_gym.ui.theme.components.CustomTextField

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

                Column(

                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFF1E1E1E))

                ) {
                    Image(

                        painter = painterResource(id = R.drawable.print),
                        contentDescription = "Descrição da imagem",
                        modifier = Modifier.size(150.dp),
                    )
                    Text(
                        text = "Acessar sua conta",
                        color = Color.White
                    )
                    Text(
                        text = "Insira suas informações e acesse sua conta" ,
                        color = Color.White
                    )
                    var text1 by remember { mutableStateOf("") }
                    CustomTextField(
                        label = "Email",
                        value = text1,
                        onValueChange = { text1 = it } ,
                        padding = 10

                    )
                    var text2 by remember { mutableStateOf("") }
                    CustomTextField(
                        label = "Senha",
                        value = text2,
                        onValueChange = { text2 = it },
                        padding = 10

                    )

                    CustomButton(
                        text = "Acessar",
                        onClick = {}

                            )

                    Text(
                        textAlign = TextAlign.Center,
                        text = " By clicking continue, you agree to our Terms of service and Privacy Policy" ,
                        color = Color.White
                    )
                }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DevMobilegymTheme {
        Greeting("Android")
    }
}
@Composable
fun CustomTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}