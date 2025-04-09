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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.ui.theme.DevMobilegymTheme
import com.example.devmobile_gym.ui.theme.components.CustomCard
import com.example.devmobile_gym.ui.theme.components.CustomCheckbox
import com.example.devmobile_gym.ui.theme.components.CustomTextField
import com.example.devmobile_gym.ui.theme.components.ExerciseCard
import com.example.devmobile_gym.ui.theme.components.ScreenScaffold

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DevMobilegymTheme {
                var text by remember { mutableStateOf("") }
                var displayText by remember { mutableStateOf("") }
                var isChecked by remember { mutableStateOf(false) }
                val scrollState = rememberScrollState()

                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

                    ScreenScaffold (

                        title = "Unifor GYM",
                        onBackClick = { /* PASSAR A FUNCAO COM A ROTA DA PAG */ },
                        onMenuClick = { /* PASSAR A FUNCAO COM A ROTA DA PAG */ },
                        needToGoBack = false,


                    ) { innerModifier ->
                        Column (
                            modifier = innerModifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)){

                            CustomTextField(label = "Texto", value = text, onValueChange = { text = it })
                            CustomButton(text ="Enviar", onClick = {
                                if (text.isNotEmpty()) {
                                    displayText = text
                                    text = ""
                                }
                            })
                            CustomButton(text ="Limpar Text()", onClick = {
                                displayText = ""
                            }, backgroundColor = Color.Red)
                            Spacer(modifier = Modifier.height(16.dp))
                            if (displayText == "") {
                                Text("")
                            } else {
                                Text("Você digitou: $displayText")
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            CustomCard(isAdm = true, treino ="Quadríceps e panturrilha", description = listOf("Panturrilha em pé (máquina)", "Cadeira Extensora (máquina)", "Agachamento Livre", "Exercício 4", "Exercício 5", "Exercício 6"), buttonText ="Iniciar Treino", onButtonClick ={})
                            CustomCard(needButton = false, treino ="Bíceps e Costas", description = listOf("Rosca Direta (Halteres)", "Rosca Scott (Halteres)", "Rosca Concentrada (Halteres)", "Exercício 4", "Exercício 5", "Exercício 6"), buttonText ="Iniciar Treino", onButtonClick ={})


                            ExerciseCard("Rosca Scott (Halteres)", 3, 12, 15)
                            CustomCheckbox(checked = isChecked, onCheckedChange = { isChecked = it })

                            for (i in 1..99) {
                                Text("valor $i")
                            }

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