package com.nutriflow.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nutriflow.mobile.ui.components.NutriButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMealPlanScreen(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedPatient by remember { mutableStateOf("Ana Silva") }
    var calories by remember { mutableStateOf("2000") }
    var protein by remember { mutableStateOf("150") }
    var carbs by remember { mutableStateOf("200") }
    var fats by remember { mutableStateOf("60") }
    var notes by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Novo Plano Alimentar", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título do Plano") },
                placeholder = { Text("Ex: Plano Hipertrofia Fase 1") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = selectedPatient,
                onValueChange = { selectedPatient = it },
                label = { Text("Paciente") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Calorias (kcal)") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = protein,
                    onValueChange = { protein = it },
                    label = { Text("Proteínas (g)") },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = carbs,
                    onValueChange = { carbs = it },
                    label = { Text("Carbos (g)") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = fats,
                    onValueChange = { fats = it },
                    label = { Text("Gorduras (g)") },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Observações / Orientações") },
                placeholder = { Text("Ex: Beber 3L de água por dia.") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            if (isSubmitted) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Plano alimentar salvo com sucesso!", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            NutriButton(
                text = "Salvar Plano Alimentar",
                onClick = {
                    if (title.isNotBlank()) {
                        isSubmitted = true
                        onSaveSuccess()
                    }
                },
                enabled = title.isNotBlank()
            )
        }
    }
}
