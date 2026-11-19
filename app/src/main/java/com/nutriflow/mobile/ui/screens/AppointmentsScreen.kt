package com.nutriflow.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nutriflow.mobile.data.model.AppointmentDto

data class AppointmentItem(
    val id: String,
    val patientName: String,
    val dateLabel: String,
    val type: String,
    val status: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    appointmentsFromApi: List<AppointmentDto> = emptyList(),
    onRefreshClick: () -> Unit = {},
    onDeleteAppointmentClick: (String) -> Unit = {},
    onStatusChangeClick: (String, String) -> Unit = { _, _ -> },
    onBackClick: () -> Unit
) {
    var showScheduleDialog by remember { mutableStateOf(false) }

    val localAppointments = remember { mutableStateListOf<AppointmentItem>() }
    val deletedIds = remember { mutableStateListOf<String>() }

    val displayAppointments = remember(appointmentsFromApi, localAppointments.size, deletedIds.size) {
        val apiItems = appointmentsFromApi
            .filter { !deletedIds.contains(it.id) }
            .map {
                AppointmentItem(
                    id = it.id,
                    patientName = it.patient,
                    dateLabel = it.date,
                    type = it.type,
                    status = it.status.replaceFirstChar { char -> char.uppercase() }
                )
            }
        
        val overriddenLocal = localAppointments.filter { !deletedIds.contains(it.id) }
        
        val merged = (overriddenLocal + apiItems).distinctBy { it.id }
        merged
    }

    var selectedPatient by remember { mutableStateOf("Andrei Araripe Paciente") }
    var dateTimeInput by remember { mutableStateOf("") }
    var appointmentType by remember { mutableStateOf("Primeira Consulta") }

    if (showScheduleDialog) {
        AlertDialog(
            onDismissRequest = { showScheduleDialog = false },
            title = { Text("Agendar Consulta", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("PACIENTE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    OutlinedTextField(
                        value = selectedPatient,
                        onValueChange = { selectedPatient = it },
                        label = { Text("Nome do Paciente") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("DATA E HORA", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    OutlinedTextField(
                        value = dateTimeInput,
                        onValueChange = { dateTimeInput = it },
                        label = { Text("dd/mm/aaaa --:--") },
                        placeholder = { Text("18/11/2026 23:29") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("TIPO", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    OutlinedTextField(
                        value = appointmentType,
                        onValueChange = { appointmentType = it },
                        label = { Text("Tipo de Consulta") },
                        placeholder = { Text("Primeira Consulta") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (selectedPatient.isNotBlank() && dateTimeInput.isNotBlank()) {
                            localAppointments.add(
                                0,
                                AppointmentItem(
                                    id = System.currentTimeMillis().toString(),
                                    patientName = selectedPatient,
                                    dateLabel = dateTimeInput,
                                    type = appointmentType.ifBlank { "Primeira Consulta" },
                                    status = "Agendada"
                                )
                            )
                            dateTimeInput = ""
                            showScheduleDialog = false
                        }
                    }
                ) {
                    Text("Agendar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showScheduleDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Próximos Atendimentos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = onRefreshClick) {
                        Icon(Icons.Default.Refresh, contentDescription = "Atualizar", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showScheduleDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Agendar Consulta") },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Agenda do Consultório",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                OutlinedButton(onClick = { showScheduleDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Agendar")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (displayAppointments.isEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Nenhuma consulta agendada no momento.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(displayAppointments, key = { it.id }) { item ->
                        AppointmentCard(
                            appointment = item,
                            onStatusChange = { newStatus ->
                                val idx = localAppointments.indexOfFirst { it.id == item.id }
                                if (idx != -1) {
                                    localAppointments[idx] = item.copy(status = newStatus)
                                } else {
                                    localAppointments.add(item.copy(status = newStatus))
                                }
                                onStatusChangeClick(item.id, newStatus.lowercase())
                            },
                            onDeleteClick = {
                                deletedIds.add(item.id)
                                localAppointments.removeAll { it.id == item.id }
                                onDeleteAppointmentClick(item.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppointmentCard(
    appointment: AppointmentItem,
    onStatusChange: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = appointment.patientName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text(text = appointment.type, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = appointment.dateLabel, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "Status: ${appointment.status}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir Consulta",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { onStatusChange("Confirmada") },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2E7D32)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Confirmar", style = MaterialTheme.typography.labelSmall)
                }

                OutlinedButton(
                    onClick = { onStatusChange("Remarcada") },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE65100)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Remarcar", style = MaterialTheme.typography.labelSmall)
                }

                OutlinedButton(
                    onClick = { onStatusChange("Faltou") },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Faltou", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
