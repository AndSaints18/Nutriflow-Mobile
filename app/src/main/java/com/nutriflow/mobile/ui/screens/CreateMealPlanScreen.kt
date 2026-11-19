package com.nutriflow.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nutriflow.mobile.ui.components.NutriButton

data class FoodBaseItem(
    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int
)

val DEFAULT_FOOD_DATABASE = listOf(
    FoodBaseItem("Arroz integral cozido", 124, 3, 26, 1),
    FoodBaseItem("Feijão carioca cozido", 76, 5, 14, 1),
    FoodBaseItem("Peito de frango grelhado", 165, 31, 0, 4),
    FoodBaseItem("Ovo inteiro", 143, 13, 1, 10),
    FoodBaseItem("Aveia em flocos", 389, 17, 66, 7),
    FoodBaseItem("Banana prata", 89, 1, 23, 0),
    FoodBaseItem("Batata doce cozida", 86, 2, 20, 0),
    FoodBaseItem("Pão integral", 247, 13, 41, 4),
    FoodBaseItem("Whey protein", 400, 80, 8, 6)
)

class SelectedMealItemState(
    val id: String,
    initialFood: FoodBaseItem,
    initialQuantity: String = "100",
    initialMealTime: String = "Almoço"
) {
    var selectedFood by mutableStateOf(initialFood)
    var quantityGrams by mutableStateOf(initialQuantity)
    var mealTime by mutableStateOf(initialMealTime)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMealPlanScreen(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedPatient by remember { mutableStateOf("Andrei Araripe Paciente") }
    var notes by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    val selectedFoods = remember {
        mutableStateListOf(
            SelectedMealItemState(
                id = "1",
                initialFood = DEFAULT_FOOD_DATABASE[0],
                initialQuantity = "100",
                initialMealTime = "Almoço"
            )
        )
    }

    val totalCalories = selectedFoods.sumOf { item ->
        val qty = item.quantityGrams.toDoubleOrNull() ?: 0.0
        ((item.selectedFood.calories * qty) / 100.0).toInt()
    }

    val totalProtein = selectedFoods.sumOf { item ->
        val qty = item.quantityGrams.toDoubleOrNull() ?: 0.0
        ((item.selectedFood.protein * qty) / 100.0).toInt()
    }

    val totalCarbs = selectedFoods.sumOf { item ->
        val qty = item.quantityGrams.toDoubleOrNull() ?: 0.0
        ((item.selectedFood.carbs * qty) / 100.0).toInt()
    }

    val totalFats = selectedFoods.sumOf { item ->
        val qty = item.quantityGrams.toDoubleOrNull() ?: 0.0
        ((item.selectedFood.fat * qty) / 100.0).toInt()
    }

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
                .padding(12.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = selectedPatient,
                onValueChange = { selectedPatient = it },
                label = { Text("PACIENTE") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("TÍTULO DO PLANO") },
                placeholder = { Text("Ex: Plano Hipertrofia e Definição") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("OBSERVAÇÕES") },
                placeholder = { Text("Estratégia, substituições e orientações do plano") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ALIMENTOS DO PLANO",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Valores calculados automaticamente por 100g.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                selectedFoods.add(
                                    SelectedMealItemState(
                                        id = System.currentTimeMillis().toString(),
                                        initialFood = DEFAULT_FOOD_DATABASE[0],
                                        initialQuantity = "100",
                                        initialMealTime = "Almoço"
                                    )
                                )
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Adicionar alimento", style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    selectedFoods.forEachIndexed { index, item ->
                        FoodSelectionCard(
                            item = item,
                            onRemoveClick = {
                                if (selectedFoods.size > 1) {
                                    selectedFoods.removeAt(index)
                                }
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MacroTotalCard("CALORIAS", "$totalCalories kcal", modifier = Modifier.weight(1f))
                MacroTotalCard("PROTEÍNAS", "${totalProtein}g", modifier = Modifier.weight(1f))
                MacroTotalCard("CARBOS", "${totalCarbs}g", modifier = Modifier.weight(1f))
                MacroTotalCard("GORDURAS", "${totalFats}g", modifier = Modifier.weight(1f))
            }

            if (isSubmitted) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Plano alimentar salvo com sucesso no banco!", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }

                NutriButton(
                    text = "Salvar no Banco",
                    onClick = {
                        if (title.isNotBlank()) {
                            isSubmitted = true
                            onSaveSuccess()
                        }
                    },
                    enabled = title.isNotBlank(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSelectionCard(
    item: SelectedMealItemState,
    onRemoveClick: () -> Unit
) {
    var foodMenuExpanded by remember { mutableStateOf(false) }
    var mealTimeMenuExpanded by remember { mutableStateOf(false) }

    val mealTimes = listOf("Café da Manhã", "Almoço", "Lanche da Tarde", "Jantar")

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = foodMenuExpanded,
                onExpandedChange = { foodMenuExpanded = !foodMenuExpanded }
            ) {
                OutlinedTextField(
                    value = item.selectedFood.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Alimento", style = MaterialTheme.typography.labelSmall) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = foodMenuExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    singleLine = true
                )
                ExposedDropdownMenu(
                    expanded = foodMenuExpanded,
                    onDismissRequest = { foodMenuExpanded = false }
                ) {
                    DEFAULT_FOOD_DATABASE.forEach { food ->
                        DropdownMenuItem(
                            text = { Text(food.name) },
                            onClick = {
                                item.selectedFood = food
                                foodMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = item.quantityGrams,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= 4) {
                            item.quantityGrams = newValue
                        }
                    },
                    label = { Text("Qtd (g)", style = MaterialTheme.typography.labelSmall) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                ExposedDropdownMenuBox(
                    expanded = mealTimeMenuExpanded,
                    onExpandedChange = { mealTimeMenuExpanded = !mealTimeMenuExpanded },
                    modifier = Modifier.weight(1.5f)
                ) {
                    OutlinedTextField(
                        value = item.mealTime,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Horário", style = MaterialTheme.typography.labelSmall) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = mealTimeMenuExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        singleLine = true
                    )
                    ExposedDropdownMenu(
                        expanded = mealTimeMenuExpanded,
                        onDismissRequest = { mealTimeMenuExpanded = false }
                    ) {
                        mealTimes.forEach { time ->
                            DropdownMenuItem(
                                text = { Text(time) },
                                onClick = {
                                    item.mealTime = time
                                    mealTimeMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                IconButton(onClick = onRemoveClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Remover", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun MacroTotalCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}
