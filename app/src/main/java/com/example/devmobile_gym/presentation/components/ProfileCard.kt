package com.example.devmobile_gym.presentation.components

import android.graphics.Bitmap // Importe Bitmap
import android.net.Uri // Mantenha se ainda usa Uri em outro lugar, mas para a foto de perfil não será mais direto
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image // Importe Image para exibir Bitmap
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
import androidx.compose.material.icons.filled.Edit // Importe o ícone de edição
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator // Importe CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton // Importe IconButton
import androidx.compose.material3.MaterialTheme // Para acessar a cor primária do tema para o CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asImageBitmap // Importe esta função para converter Bitmap para ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// import coil3.compose.AsyncImage // Remova se não for mais usar AsyncImage neste componente
import java.util.Calendar
import com.example.devmobile_gym.R

// A função calculaAnoAtual não precisa de userId como parâmetro se for só o ano atual,
// mas se o userId for para extrair partes da matrícula, mantenha.
// Ajustei o nome da função para calcularMatricula, que parece ser o uso real.
private fun calculaMatricula(userId: String) : String{
    val calendario = Calendar.getInstance()
    val anoAtual = (calendario.get(Calendar.YEAR) % 100).toString() // Pega os 2 últimos dígitos do ano
    // Adaptação: Assumindo que userId.substring(0, 8) é uma parte da matrícula.
    // Certifique-se de que userId é grande o suficiente para isso, ou trate se for menor.
    val userIdPart = if (userId.length >= 8) userId.substring(0, 8) else userId
    return "$anoAtual${userIdPart.uppercase()}"
}

@Composable
fun ProfileCard(
    name: String,
    userId: String,
    weight: String,
    height: String,
    onEditProfileClick: () -> Unit,
    profileImageBitmap: Bitmap?, // AGORA RECEBE UM BITMAP
    isLoading: Boolean // NOVO: Flag para indicar se a foto está sendo carregada/enviada
) {
    val id = calculaMatricula(userId) // Usando a função ajustada

    Card(
        modifier = Modifier.fillMaxWidth(),
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
                            color = Color.DarkGray,
                            offset = Offset(1f, 3f),
                            blurRadius = 8f
                        )
                    )
                )
                // Botão de edição ao lado do título "Perfil"
                // Posicionamento ajustado para não ficar sobre a imagem de perfil
                IconButton(
                    onClick = onEditProfileClick,
                    modifier = Modifier
                        .padding(start = 4.dp, top = 4.dp) // Ajuste padding
                        .size(32.dp) // Ajuste o tamanho do IconButton se necessário
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar perfil",
                        tint = Color.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, top = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                // Assuming AcessibilidadeIconButton is defined elsewhere and works fine
                // You might need to adjust its import if it's not in the same package
                // or remove it if it's not relevant here.
                // AcessibilidadeIconButton() // Uncomment if you have this component
            }
        }
        Column (
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(Color.Gray), // Fundo cinza enquanto a imagem não carrega
                contentAlignment = Alignment.Center
            ) {
                if (profileImageBitmap != null) {
                    // Exibir o Bitmap da foto de perfil
                    Image(
                        bitmap = profileImageBitmap.asImageBitmap(), // Converte Bitmap para ImageBitmap
                        contentDescription = "Imagem de Perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Ícone de perfil padrão se não houver imagem
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Avatar padrão",
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize() // Preenche o Box de 180dp
                    )
                }

                // Indicador de progresso se isLoading for true
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(60.dp), // Ajuste o tamanho do indicador
                        color = MaterialTheme.colorScheme.primary // Usa a cor primária do seu tema
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaçamento abaixo da imagem

            Text(
                text = name,
                fontSize = 32.sp,
                color = Color.White,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.DarkGray,
                        offset = Offset(1f, 3f),
                        blurRadius = 8f
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
                            color = Color.DarkGray,
                            offset = Offset(1f, 3f),
                            blurRadius = 8f
                        )
                    )
                )
            }
            Row (
                modifier = Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.SpaceAround, // Para distribuir melhor peso/altura
                verticalAlignment = Alignment.CenterVertically // Centraliza verticalmente na linha
            ){
                weightBox(weight)
                Spacer(Modifier.width(70.dp)) // Espaçamento entre as boxes
                heightBox(height)
            }
        }
    }
}

// @Preview foi removido aqui para evitar conflitos se ProfileCard precisa de dados que não são facilmente mockados
// Se quiser um preview, crie um composable separado para o preview que chame ProfileCard com dados mockados.

// --- weightBox e heightBox (Sem alterações) ---
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
            Text(".", color = Color(0xFFF0F4FF)) // Isto é apenas um hack para dar tamanho ao Card?
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
            Text(".", color = Color(0xFFF0F4FF)) // Isto é apenas um hack para dar tamanho ao Card?
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