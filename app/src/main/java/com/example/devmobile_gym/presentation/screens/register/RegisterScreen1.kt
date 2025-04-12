import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.components.CustomTextField

@Composable
fun RegisterScreen(onNavigateToRegister2: () -> Unit) {
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
            text = "Crie sua conta",
            color = Color.White,
            fontSize = 20.sp
        )
        Text(
            text = "Digite o seu email para se registrar." ,
            color = Color.White
        )
        var text1 by remember { mutableStateOf("") }
        CustomTextField(
            label = "Email@domain.com",
            value = text1,
            onValueChange = { text1 = it },
            padding = 10

        )
        CustomButton(
            text = "Continuar",
            onClick =  onNavigateToRegister2



        )
        Text(
            textAlign = TextAlign.Center,
            text = " By clicking continue, you agree to our Terms of service and Privacy Policy" ,
            color = Color.White
        )
    }




}