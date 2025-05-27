package com.example.devmobile_gym.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate

// Define as cores fora da função Composable para evitar recriação desnecessária
private val BackgroundColor = Color(0xFF3B3B3B)
private val WorkoutDayColor = Color(0xFF267FE7)
private val RestDayColor = Color(0xFFEDEFF1)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyCalendar(
    workoutDates: List<String> // Recebe a lista de datas de treino como String
) {
    val today = LocalDate.now()
    val startOfWeek = today.with(DayOfWeek.MONDAY) // Assume que a semana começa na segunda
    val weekDates = (0..6).map { startOfWeek.plusDays(it.toLong()) } // Gera as 7 datas da semana

    // Converte a lista de strings para um Set para buscas mais eficientes
    val workoutDateSet = workoutDates.toSet()

    // Mapeamento de DayOfWeek para nomes em português
    val dayOfWeekMap = mapOf(
        DayOfWeek.MONDAY to "SEG",
        DayOfWeek.TUESDAY to "TER",
        DayOfWeek.WEDNESDAY to "QUA",
        DayOfWeek.THURSDAY to "QUI",
        DayOfWeek.FRIDAY to "SEX",
        DayOfWeek.SATURDAY to "SAB",
        DayOfWeek.SUNDAY to "DOM"
    )

    // O Box externo é para centralizar o calendário horizontalmente
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), // Padding ao redor de todo o componente
        contentAlignment = Alignment.Center // Centraliza o conteúdo dentro deste Box
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth() // Permite que o Row interno ocupe a largura disponível do Box
                .background(BackgroundColor, MaterialTheme.shapes.medium) // Fundo com bordas arredondadas
                .clip(MaterialTheme.shapes.medium) // Garante que o clip aplique as bordas
                .padding(8.dp), // Padding interno para o conteúdo do calendário
            horizontalArrangement = Arrangement.SpaceBetween, // Espaça os dias igualmente
            verticalAlignment = Alignment.CenterVertically // Centraliza verticalmente o conteúdo de cada dia
        ) {
            weekDates.forEach { date ->
                // Verifica se a data atual da semana está na lista de dias de treino
                val isWorkoutDay = workoutDateSet.contains(date.toString())
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f) // Faz com que cada coluna ocupe espaço igual
                        .padding(horizontal = 2.dp, vertical = 4.dp) // Ajuste fino do padding interno
                ) {
                    Text(
                        text = dayOfWeekMap[date.dayOfWeek] ?: date.dayOfWeek.name.take(3),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                    Box(
                        modifier = Modifier
                            .size(36.dp) // Tamanho do círculo do dia
                            .clip(CircleShape) // Borda circular para o dia
                            .background(if (isWorkoutDay) WorkoutDayColor else RestDayColor), // Cor de fundo baseada se é dia de treino
                        contentAlignment = Alignment.Center // Centraliza o texto do número do dia
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            color = if (isWorkoutDay) Color.White else Color.Black // Cor do número do dia
                        )
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewWeeklyCalendar() {
    val today = LocalDate.now()
    val mockWorkoutDates = listOf(
        today,
        today.minusDays(1),
        today.minusDays(5)
    ).map { it.toString() } // Converta para String para o preview

    WeeklyCalendar(workoutDates = mockWorkoutDates)
}