package com.example.devmobile_gym.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val BackgroundColor = Color(0xFF3B3B3B)
private val WorkoutDayColor = Color(0xFF267FE7)
private val RestDayColor = Color(0xFFf9f9ff)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthlyCalendar(
    workoutDates: List<String>,
    elevation: Dp = 8.dp // você pode ajustar a altura da sombra aqui
) {
    val today = LocalDate.now()
    val currentMonth = YearMonth.from(today)
    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.atEndOfMonth()
    val workoutDateSet = workoutDates.toSet()

    val daysInMonth = (1..lastDayOfMonth.dayOfMonth).map { currentMonth.atDay(it) }

    val ptBr = Locale("pt", "BR")
    val monthTitle = currentMonth.month
        .getDisplayName(TextStyle.FULL, ptBr)
        .replaceFirstChar { it.uppercase() }

    Surface(
        color = BackgroundColor,
        shape = RoundedCornerShape(20.dp),
        tonalElevation = elevation,
        shadowElevation = elevation,
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Título do mês
            Text(
                text = monthTitle,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )

            // Dias da semana
            val diasSemana = listOf("SEG", "TER", "QUA", "QUI", "SEX", "SAB", "DOM")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                diasSemana.forEach {
                    Text(
                        text = it,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val dayOffset = if (firstDayOfMonth.dayOfWeek == DayOfWeek.SUNDAY) 6
            else firstDayOfMonth.dayOfWeek.ordinal - DayOfWeek.MONDAY.ordinal

            val calendarDays = mutableListOf<LocalDate?>()
            repeat(dayOffset) { calendarDays.add(null) }
            calendarDays.addAll(daysInMonth)
            val totalCells = (calendarDays.size + 6) / 7 * 7

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                userScrollEnabled = false
            ) {
                items(totalCells) { index ->
                    if (index < calendarDays.size) {
                        val date = calendarDays[index]
                        if (date == null) {
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                            )
                        } else {
                            val isWorkoutDay = workoutDateSet.contains(date.toString())
                            if (isWorkoutDay) {
                                Box(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .padding(4.dp)
                                        .clip(CircleShape)
                                        .background(WorkoutDayColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = date.dayOfMonth.toString(),
                                        color = Color.White
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = date.dayOfMonth.toString(),
                                        color = Color.White
                                    )
                                }
                            }

                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(4.dp)
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
fun PreviewMonthlyCalendar() {
    val today = LocalDate.now()
    val mockWorkoutDates = listOf(
        today.minusDays(1),
        today.minusDays(2),
        today.minusDays(3),
        today.minusDays(7),
        today.minusDays(14),
        today.withDayOfMonth(1),
        today.withDayOfMonth(15)
    ).map { it.toString() }

    MonthlyCalendar(workoutDates = mockWorkoutDates)
}

