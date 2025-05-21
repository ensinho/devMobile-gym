package com.example.devmobile_gym.presentation.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DateTimePickerInput(
    label: String = "Data e Hora",
    selectedDateTime: Date?,
    onDateTimeSelected: (Date) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Estado da data formatada para exibir no campo
    val formattedDateTime = remember(selectedDateTime) {
        selectedDateTime?.let {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            sdf.format(it)
        } ?: ""
    }

    OutlinedTextField(
        value = formattedDateTime,
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color(0xFF1E1E1E),
            unfocusedContainerColor = Color(0xFF1E1E1E),

            ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                // Primeiro escolhe a data
                val currentDate = Calendar.getInstance()
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        // Depois escolhe a hora
                        TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                                onDateTimeSelected(calendar.time) // <-- envia o Date completo
                            },
                            currentDate.get(Calendar.HOUR_OF_DAY),
                            currentDate.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH),
                    currentDate.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
    )
}
