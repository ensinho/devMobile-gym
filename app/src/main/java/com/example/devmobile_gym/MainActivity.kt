package com.example.devmobile_gym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.ui.theme.DevMobilegymTheme
import com.example.devmobile_gym.ui.theme.components.CustomTextField

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DevMobilegymTheme {
                var text by remember { mutableStateOf("") }
                var displayText by remember { mutableStateOf("") }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (modifier = Modifier.padding(innerPadding)){

                        CustomTextField(label = "Texto", value = text, onValueChange = { text = it })
                        CustomButton("Enviar", onClick = {
                            if (text.isNotEmpty()) {
                                displayText = text
                                text = ""
                            }
                        })
                        CustomButton("Limpar Text()", onClick = {
                            displayText = ""
                        }, backgroundColor = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        if (displayText == "") {
                            Text("")
                        } else {
                            Text("Você digitou: $displayText")
                        }
                    }
                }
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