package com.nutriflow.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class AppointmentItem(
    val id: String,
    val patientName: String,
    val dateLabel: String,
    val type: String,
    val status: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(onBackClick: () -> Unit) {
    val appointments = listOf(
        AppointmentItem("1", "Ana Silva", "Amanhã - 14:00", "Consulta Retorno", "Confirmada"),
        AppointmentItem("2", "Bruno Costa", "26/09/2026 - 10:00", "Avaliação Inicial", "Agendada"),
        AppointmentItem("3", "Carla Oliveira", "28/09/2026 - 15:30", "Acompanhamento", "Confirmada"),
        AppointmentItem("4", "Daniel Santos", "30/09/2026 - 09:00", "Consulta On-line", "Agendada")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Próximos Atendimentos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(appointments) { item ->
                    AppointmentCard(item)
                }
            }
        }
    }
}

@Composable
fun AppointmentCard(appointment: AppointmentItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Event,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = appointment.patientName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(text = appointment.type, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = appointment.dateLabel, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            }
            SuggestionChip(
                onClick = {},
                label = { Text(appointment.status) }
            )
        }
    }
}
