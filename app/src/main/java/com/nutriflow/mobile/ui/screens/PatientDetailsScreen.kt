package com.nutriflow.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailsScreen(patientId: String, onBackClick: () -> Unit) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ficha do Paciente", fontWeight = FontWeight.Bold) },
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
            // Card de Info do Paciente (MOB-13)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Paciente ID: #$patientId",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Objetivo: Emagrecimento e Ganho de Massa",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Histórico de Evolução (MOB-14)
            Text(
                text = "Evolução do Paciente",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    EvolutionRow(date = "10/09/2026", weight = "78.5 kg", bf = "18.2%")
                    HorizontalDivider()
                    EvolutionRow(date = "10/08/2026", weight = "80.2 kg", bf = "19.5%")
                    HorizontalDivider()
                    EvolutionRow(date = "10/07/2026", weight = "82.0 kg", bf = "21.0%")
                }
            }

            // Plano Alimentar Atual (MOB-15)
            Text(
                text = "Plano Alimentar Ativo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    MealItem(mealName = "Café da Manhã (07:00)", description = "3 Ovos mexidos + 2 fatias de pão integral + 150ml de café sem açúcar.")
                    HorizontalDivider()
                    MealItem(mealName = "Almoço (12:00)", description = "150g de Filé de Frango Grelhado + 100g de Arroz Integral + Salada verde à vontade.")
                    HorizontalDivider()
                    MealItem(mealName = "Lanche da Tarde (16:00)", description = "30g de Whey Protein + 1 Banana picada + 20g de aveia em flocos.")
                    HorizontalDivider()
                    MealItem(mealName = "Jantar (20:00)", description = "120g de Patinho moído + 150g de Batata Doce cozida + legumes no vapor.")
                }
            }
        }
    }
}

@Composable
fun EvolutionRow(date: String, weight: String, bf: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = date, fontWeight = FontWeight.Medium)
        Text(text = "Peso: $weight", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = "BF: $bf", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun MealItem(mealName: String, description: String) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Restaurant,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = mealName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
