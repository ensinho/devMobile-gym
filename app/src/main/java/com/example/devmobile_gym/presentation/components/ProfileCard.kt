package com.example.devmobile_gym.presentation.components

import AcessibilidadeIconButton
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import java.nio.file.WatchEvent
import java.time.LocalDate
import java.util.Calendar
import com.example.devmobile_gym.R

private fun calculaAnoAtual(userId: String) : String{
    val calendario = Calendar.getInstance() // Obtém uma instância do calendário com a data e hora atuais
    val anoAtual = (calendario.get(Calendar.YEAR).toInt() - 2000).toString()
    return anoAtual + (userId.substring(0, 8)).uppercase()
}

@Composable
fun ProfileCard(
    name: String,
    userId: String,
    weight: String,
    height: String,
    onEditProfileClick: () -> Unit, // <--- Adicione um callback para o clique de edição
    profileImageUri: Uri?, // Continua recebendo o Uri para exibir
) {
    val id = calculaAnoAtual(userId)

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF267FE7)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Row (
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                Text(
                    text = "Perfil",
                    fontSize = 32.sp,
                    color = Color.White,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.DarkGray, // Cor da sombra
                            offset = Offset(1f, 3f), // Posição da sombra (X, Y)
                            blurRadius = 8f // Intensidade do desfoque
                        )
                    )
                )
                // Botão de edição ao lado do ícone do perfil
                ProfileEditButton(
                    onClick = onEditProfileClick, // Usa o callback passado para o ProfileCard
                    modifier = Modifier.padding(start = 1.dp), // Adiciona um espaçamento à esquerda
                    tint = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, top = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                AcessibilidadeIconButton()
            }
        }
        Column (
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (profileImageUri != null && profileImageUri != Uri.EMPTY) {
                AsyncImage(
                    model = profileImageUri, // O Uri é diretamente o modelo
                    contentDescription = "Imagem de Perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                    // Removida a clicabilidade direta na imagem aqui (se você não quiser que ela faça nada)
                )
            } else {
                // Ícone de perfil padrão se não houver imagem
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Avatar",
                    tint = Color.White,
                    modifier = Modifier
                        .size(180.dp)
                    // Removida a clicabilidade direta no ícone aqui
                )
            }


            Spacer(modifier = Modifier.width(16.dp)) // Este Spacer era para a Row anterior, pode ser ajustado
            Text(
                text = name,
                fontSize = 32.sp,
                color = Color.White,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.DarkGray, // Cor da sombra
                        offset = Offset(1f, 3f), // Posição da sombra (X, Y)
                        blurRadius = 8f // Intensidade do desfoque
                    )
                )
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Sharp.Info,
                    contentDescription = "Matrícula",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = id,
                    fontSize = 15.sp,
                    color = Color.White,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.DarkGray, // Cor da sombra
                            offset = Offset(1f, 3f), // Posição da sombra (X, Y)
                            blurRadius = 8f // Intensidade do desfoque
                        )
                    )
                )
            }
            Row (
                modifier = Modifier.padding(20.dp)
            ){
                weightBox(weight)
                Spacer(Modifier.padding(end = 70.dp))
                heightBox(height)

            }
        }
    }
}

// ... (weightBox e heightBox permanecem inalterados)

@Composable
fun weightBox(value: String) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Card(
            modifier = Modifier.height(60.dp).width(10.dp),
            shape = RoundedCornerShape(55.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),

            ) {
            Text(".", color = Color(0xFFF0F4FF))
        }
        Spacer(modifier = Modifier.width(6.dp))
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = "Peso",
                fontSize = 14.sp,
                color = Color.White
            )
        }
    }
}
@Composable
fun heightBox(value: String) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Card(
            modifier = Modifier.height(60.dp).width(10.dp),
            shape = RoundedCornerShape(55.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),

            ) {
            Text(".", color = Color(0xFFF0F4FF))
        }
        Spacer(modifier = Modifier.width(6.dp))
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = "Altura",
                fontSize = 14.sp,
                color = Color.White
            )
        }
    }
}