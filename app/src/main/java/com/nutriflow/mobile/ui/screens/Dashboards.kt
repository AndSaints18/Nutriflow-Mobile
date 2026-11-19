package com.nutriflow.mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nutriflow.mobile.data.model.PatientDashboardResponse
import com.nutriflow.mobile.ui.components.MacroDonutChart
import com.nutriflow.mobile.ui.components.NutriButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDashboard(
    patientName: String = "Maria",
    objective: String = "A definir",
    nutritionistName: String = "Sem vínculo",
    nextAppointment: String = "Sem agenda",
    dashboardData: PatientDashboardResponse? = null,
    onRefreshClick: () -> Unit = {},
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()

    var showAddMealDialog by remember { mutableStateOf(false) }
    var showAddWeightDialog by remember { mutableStateOf(false) }
    var showAddAppointmentDialog by remember { mutableStateOf(false) }
    var showChatDialog by remember { mutableStateOf(false) }

    var currentWeightState by remember { mutableStateOf("78.5 kg") }
    var nextAppointmentState by remember { mutableStateOf(nextAppointment) }
    val todayMeals = remember { mutableStateListOf<String>() }

    val activeChallenges = remember {
        mutableStateListOf(
            Pair("Comer menos fritura! 🎁 Prêmio: 1 refeição livre no sábado", true),
            Pair("Se controlar mais na hora de comer 🎁 Prêmio: 1 refeição livre no domingo", false)
        )
    }

    var mealNameInput by remember { mutableStateOf("") }
    var mealCaloriesInput by remember { mutableStateOf("") }
    var weightInput by remember { mutableStateOf("") }
    var appointmentDateInput by remember { mutableStateOf("") }

    if (showChatDialog) {
        FloatingChatPopupDialog(
            onDismissRequest = { showChatDialog = false }
        )
    }

    if (showAddMealDialog) {
        AlertDialog(
            onDismissRequest = { showAddMealDialog = false },
            title = { Text("Registrar Refeição", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = mealNameInput,
                        onValueChange = { mealNameInput = it },
                        label = { Text("Refeição / Alimento") },
                        placeholder = { Text("Ex: Almoço - Frango com Arroz") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = mealCaloriesInput,
                        onValueChange = { mealCaloriesInput = it },
                        label = { Text("Calorias (kcal)") },
                        placeholder = { Text("Ex: 450") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (mealNameInput.isNotBlank()) {
                            val cal = mealCaloriesInput.ifBlank { "0" }
                            todayMeals.add("$mealNameInput ($cal kcal)")
                            mealNameInput = ""
                            mealCaloriesInput = ""
                            showAddMealDialog = false
                        }
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMealDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showAddWeightDialog) {
        AlertDialog(
            onDismissRequest = { showAddWeightDialog = false },
            title = { Text("Registrar Novo Peso", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = { weightInput = it },
                        label = { Text("Peso Atual (kg)") },
                        placeholder = { Text("Ex: 77.8") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (weightInput.isNotBlank()) {
                            currentWeightState = "$weightInput kg"
                            weightInput = ""
                            showAddWeightDialog = false
                        }
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddWeightDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showAddAppointmentDialog) {
        AlertDialog(
            onDismissRequest = { showAddAppointmentDialog = false },
            title = { Text("Solicitar Agendamento de Consulta", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = appointmentDateInput,
                        onValueChange = { appointmentDateInput = it },
                        label = { Text("Data e Horário Pretendidos") },
                        placeholder = { Text("Ex: 15/10/2026 - 14:00") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (appointmentDateInput.isNotBlank()) {
                            nextAppointmentState = "$appointmentDateInput (Solicitada)"
                            appointmentDateInput = ""
                            showAddAppointmentDialog = false
                        }
                    }
                ) {
                    Text("Solicitar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddAppointmentDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "PAINEL DO PACIENTE",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Boa tarde, $patientName",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onRefreshClick) {
                        Icon(Icons.Default.Refresh, contentDescription = "Atualizar", tint = MaterialTheme.colorScheme.primary)
                    }
                    TextButton(onClick = onLogout) {
                        Text("Sair", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingChatFAB(onClick = { showChatDialog = true })
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
            // Badges de Status (Objetivo, Nutricionista, Consulta)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusBadge(title = "SEU OBJETIVO", value = objective, modifier = Modifier.weight(1f))
                StatusBadge(title = "NUTRICIONISTA", value = nutritionistName, modifier = Modifier.weight(1f))
                StatusBadge(
                    title = "PRÓXIMA CONSULTA",
                    value = nextAppointmentState,
                    onClick = { showAddAppointmentDialog = true },
                    modifier = Modifier.weight(1f)
                )
            }

            // Resumo Nutricional
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        ) {
                            Text(
                                text = "DASHBOARD DE CALORIAS",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Text(
                            text = if (todayMeals.isNotEmpty()) "ADERÊNCIA: 100%" else "ADERÊNCIA: 0%",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = "Resumo nutricional",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2B312B)),
                            modifier = Modifier.weight(1.2f)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.size(90.dp)
                                ) {
                                    MacroDonutChart(
                                        proteinPercent = 0.35f,
                                        carbsPercent = 0.45f,
                                        fatsPercent = 0.20f,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = if (todayMeals.isNotEmpty()) "${todayMeals.size * 450}" else "0",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text("de 2000 kcal", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Fibra", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                        Text("12g", style = MaterialTheme.typography.bodySmall, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                    Column {
                                        Text("Água", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                        Text("1.5L", style = MaterialTheme.typography.bodySmall, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MacroCard(label = "Proteínas", value = if (todayMeals.isNotEmpty()) "${todayMeals.size * 30}g" else "0g")
                            MacroCard(label = "Carboidratos", value = if (todayMeals.isNotEmpty()) "${todayMeals.size * 50}g" else "0g")
                            MacroCard(label = "Gorduras", value = if (todayMeals.isNotEmpty()) "${todayMeals.size * 15}g" else "0g")
                        }
                    }
                }
            }

            // Registro de Refeições
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ) {
                        Text(
                            text = "REGISTRO DE REFEIÇÕES",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Seu dia alimentar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedButton(onClick = { showAddMealDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Nova refeição")
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (todayMeals.isEmpty()) {
                            Text(
                                text = "Nenhuma refeição registrada hoje. Use o botão 'Nova refeição' para registrar o próximo horário.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(16.dp)
                            )
                        } else {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                todayMeals.forEachIndexed { index, meal ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = meal, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                    }
                                    if (index < todayMeals.size - 1) {
                                        HorizontalDivider()
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Plano Alimentar (Roteiro Nutricional - Condicional Dinâmico)
            val activePlanTitle = dashboardData?.plan?.title ?: "Sem plano alimentar ativo"
            val planSections = dashboardData?.plan?.sections ?: emptyList()

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ) {
                        Text(
                            text = "PLANO ALIMENTAR",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Text(
                        text = "Roteiro nutricional",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "PLANO ATUAL", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = activePlanTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (planSections.isEmpty() || activePlanTitle.contains("Sem plano", ignoreCase = true)) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Nenhum plano alimentar ativo para exibir.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        planSections.forEach { section ->
                            MealRoadmapCard(
                                mealTimeLabel = section.slotLabel.uppercase(),
                                summaryTitle = section.title,
                                description = section.description
                            )
                        }
                    }
                }
            }

            // Metas Nutricionais (Paridade 100% com a Web)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ) {
                        Text(
                            text = "METAS NUTRICIONAIS",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Text(
                        text = "Prioridades da semana",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    GoalProgressItem(
                        label = "Bater proteína mínima",
                        currentVal = if (todayMeals.isNotEmpty()) "${todayMeals.size * 30}" else "0",
                        targetVal = "150 G",
                        progress = if (todayMeals.isNotEmpty()) (todayMeals.size * 30f / 150f).coerceAtMost(1f) else 0f
                    )

                    GoalProgressItem(
                        label = "Faixa calórica do dia",
                        currentVal = if (todayMeals.isNotEmpty()) "${todayMeals.size * 450}" else "0",
                        targetVal = "2000 KCAL",
                        progress = if (todayMeals.isNotEmpty()) (todayMeals.size * 450f / 2000f).coerceAtMost(1f) else 0f
                    )

                    GoalProgressItem(
                        label = "Água ao longo do dia",
                        currentVal = "1.5",
                        targetVal = "2.5 L",
                        progress = 0.6f
                    )

                    GoalProgressItem(
                        label = "Regularidade das refeições",
                        currentVal = "${todayMeals.size}",
                        targetVal = "4",
                        progress = (todayMeals.size / 4f).coerceAtMost(1f)
                    )

                    HorizontalDivider()

                    Text(
                        text = "DESAFIOS ATIVOS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )

                    val checklistItems = dashboardData?.clinical?.checklist ?: emptyList()
                    val challengeOnlyItems = checklistItems.filter { it.isChallenge || it.label.contains("🎁") || (!it.label.startsWith("Registrar") && !it.label.startsWith("Manter") && !it.label.startsWith("Atualizar")) }
                    
                    val displayChallenges = if (challengeOnlyItems.isNotEmpty()) {
                        challengeOnlyItems.map { Pair(it.label, it.done) }
                    } else if (dashboardData != null) {
                        emptyList()
                    } else {
                        activeChallenges
                    }

                    if (displayChallenges.isEmpty()) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Nenhum desafio ativo no momento.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        displayChallenges.forEachIndexed { index, challenge ->
                            val (title, isDone) = challenge
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (index < activeChallenges.size) {
                                            activeChallenges[index] = Pair(title, !isDone)
                                        }
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isDone) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.RadioButtonUnchecked,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Medium,
                                        textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (isDone) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            shape = CircleShape
                                        ) {
                                            Text(
                                                text = "FEITO!",
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "FOCO PRINCIPAL",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Seguir o plano com constância ao longo do dia.",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Evolução de Peso ("Curva das semanas")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        ) {
                            Text(
                                text = "EVOLUÇÃO DE PESO",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        OutlinedButton(onClick = { showAddWeightDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("ADD PESO")
                        }
                    }

                    Text(
                        text = "Curva das semanas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Grid de métricas de Peso
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        WeightMetricCard("Atual", currentWeightState, modifier = Modifier.weight(1f))
                        WeightMetricCard("Meta", "72.0 kg", modifier = Modifier.weight(1f))
                        WeightMetricCard("Variação", "-2.2 kg", modifier = Modifier.weight(1f))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        WeightMetricCard("Inicial", "80.7 kg", modifier = Modifier.weight(1f))
                        WeightMetricCard("Média semanal", "78.2 kg", modifier = Modifier.weight(1f))
                        WeightMetricCard("Tendência", "Queda", modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun MealRoadmapCard(
    mealTimeLabel: String,
    summaryTitle: String,
    description: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = mealTimeLabel,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = summaryTitle,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun GoalProgressItem(
    label: String,
    currentVal: String,
    targetVal: String,
    progress: Float
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
            Text(text = "$currentVal / $targetVal", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
fun WeightMetricCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun StatusBadge(
    title: String,
    value: String,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2B312B)),
        modifier = if (onClick != null) modifier.clickable { onClick() } else modifier
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = 9.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, style = MaterialTheme.typography.bodySmall, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MacroCard(label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionistDashboard(
    nutritionistName: String = "Nutricionista",
    onLogout: () -> Unit,
    onViewPatientsClick: () -> Unit,
    onViewAppointmentsClick: () -> Unit = {},
    onCreateMealPlanClick: () -> Unit = {},
    activePatientsCount: String = "24",
    appointmentsCount: String = "5"
) {
    var showChatDialog by remember { mutableStateOf(false) }

    if (showChatDialog) {
        FloatingChatPopupDialog(
            onDismissRequest = { showChatDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "PAINEL DO NUTRICIONISTA",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Olá, $nutritionistName",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Sair", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingChatFAB(onClick = { showChatDialog = true })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Resumo do Consultório",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    SummaryCard("Pacientes", activePatientsCount, Icons.Default.Group)
                }
                Box(modifier = Modifier.weight(1f)) {
                    SummaryCard("Consultas Hoje", appointmentsCount, Icons.Default.Today)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            NutriButton(
                text = "Ver Todos os Pacientes",
                onClick = onViewPatientsClick
            )

            OutlinedButton(
                onClick = onViewAppointmentsClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Próximos Atendimentos")
            }

            OutlinedButton(
                onClick = onCreateMealPlanClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Criar Novo Plano Alimentar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(
    adminName: String = "Administrador",
    onLogout: () -> Unit,
    totalUsersCount: String = "12",
    patientsCount: String = "8",
    nutritionistsCount: String = "3",
    foodsCount: String = "15"
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "PAINEL DO ADMINISTRADOR",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Olá, $adminName",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Sair", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
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
            Text(
                text = "Gestão Geral da Plataforma",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    SummaryCard("Total Usuários", totalUsersCount, Icons.Default.Group)
                }
                Box(modifier = Modifier.weight(1f)) {
                    SummaryCard("Nutricionistas", nutritionistsCount, Icons.Default.Person)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    SummaryCard("Pacientes", patientsCount, Icons.Default.Group)
                }
                Box(modifier = Modifier.weight(1f)) {
                    SummaryCard("Alimentos", foodsCount, Icons.Default.Restaurant)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            NutriButton(
                text = "Gerenciar Usuários da Plataforma",
                onClick = {}
            )

            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tabela de Alimentos")
            }

            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Relatórios e Métricas Globais")
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, icon: ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, style = MaterialTheme.typography.labelMedium)
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
