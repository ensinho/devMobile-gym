package com.example.devmobile_gym.presentation.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton // Importar IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DateTimePickerInput(
    selectedDateTime: Date?, // Agora espera java.util.Date?
    onDateTimeSelected: (Date) -> Unit, // Agora envia java.util.Date
    modifier: Modifier = Modifier,
    label: String = "Selecionar Data e Hora"
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    // Se já houver uma data selecionada, inicializa o calendário com ela
    selectedDateTime?.let {
        calendar.time = it
    }

    // Estados para controlar a exibição dos dialogs
    val showDatePicker = remember { mutableStateOf(false) }
    val showTimePicker = remember { mutableStateOf(false) }

    // Listener para o DatePickerDialog
    val dateSetListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
        calendar.set(year, month, dayOfMonth)
        showDatePicker.value = false // Fecha o seletor de data
        showTimePicker.value = true  // Abre o seletor de tempo imediatamente após a data
    }

    // Listener para o TimePickerDialog
    val timeSetListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, minute: Int ->
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        showTimePicker.value = false // Fecha o seletor de tempo

        // Cria a data e hora final e chama o callback
        onDateTimeSelected(calendar.time) // Converte Calendar para Date e envia
    }

    // Exibe o DatePickerDialog se showDatePicker for true
    if (showDatePicker.value) {
        DatePickerDialog(
            context,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Exibe o TimePickerDialog se showTimePicker for true
    if (showTimePicker.value) {
        TimePickerDialog(
            context,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // true para formato 24 horas, false para AM/PM
        ).show()
    }

    // Campo de texto que o usuário clica no ícone para abrir os seletores
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            // Formata a data para exibição no campo de texto
            value = selectedDateTime?.let {
                SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(it)
            } ?: "", // Se selectedDateTime for nulo, exibe uma string vazia
            onValueChange = { /* Não é editável diretamente, apenas para exibição */ },
            label = { Text(label) },
            readOnly = true, // Torna o campo não editável, apenas clicável através do ícone
            trailingIcon = {
                // Usa IconButton para garantir que a área de toque seja clara e responsiva
                IconButton(onClick = { showDatePicker.value = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Selecionar Data e Hora",
                        tint = MaterialTheme.colorScheme.onSurface // Cor do ícone
                    )
                }
            },
            modifier = Modifier.fillMaxWidth() // Ocupa a largura total disponível
        )
    }
}
