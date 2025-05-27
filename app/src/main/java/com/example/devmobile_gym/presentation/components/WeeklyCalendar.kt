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


private val BackgroundColor = Color(0xFF3B3B3B)
private val WorkoutDayColor = Color(0xFF267FE7)
private val RestDayColor = Color(0xFFEDEFF1)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyCalendar(
    workoutDates: List<String>
) {
    val today = LocalDate.now()
    val startOfWeek = today.with(DayOfWeek.MONDAY)
    val weekDates = (0..6).map { startOfWeek.plusDays(it.toLong()) }

    val workoutDateSet = workoutDates.toSet()

    val dayOfWeekMap = mapOf(
        DayOfWeek.MONDAY to "SEG",
        DayOfWeek.TUESDAY to "TER",
        DayOfWeek.WEDNESDAY to "QUA",
        DayOfWeek.THURSDAY to "QUI",
        DayOfWeek.FRIDAY to "SEX",
        DayOfWeek.SATURDAY to "SAB",
        DayOfWeek.SUNDAY to "DOM"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundColor)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weekDates.forEach { date ->
            val isWorkoutDay = workoutDateSet.contains(date.toString())
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
            ) {
                Text(
                    text = dayOfWeekMap[date.dayOfWeek] ?: date.dayOfWeek.name.take(3), // Use mapped Portuguese name or fallback
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isWorkoutDay) WorkoutDayColor else RestDayColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = if (isWorkoutDay) Color.White else Color.Black
                    )
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
    ).map { it.toString() }

    WeeklyCalendar(workoutDates = mockWorkoutDates)
}

