package com.nutriflow.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.nutriflow.mobile.ui.components.NutriButton
import com.nutriflow.mobile.ui.components.NutriTextField

@Composable
fun RegisterScreen(
    onRegisterClick: (name: String, email: String, role: String, password: String) -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("NUTRITIONIST") } // "NUTRITIONIST" or "PATIENT"

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Criar Conta",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            NutriTextField(
                value = name,
                onValueChange = { name = it },
                label = "Nome Completo"
            )

            Spacer(modifier = Modifier.height(16.dp))

            NutriTextField(
                value = email,
                onValueChange = { email = it },
                label = "E-mail"
            )

            Spacer(modifier = Modifier.height(16.dp))

            NutriTextField(
                value = password,
                onValueChange = { password = it },
                label = "Senha (mínimo 8 caracteres)",
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Selecione seu Perfil:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Start)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedRole == "NUTRITIONIST",
                    onClick = { selectedRole = "NUTRITIONIST" }
                )
                Text(
                    text = "Nutricionista",
                    modifier = Modifier.padding(end = 16.dp)
                )

                RadioButton(
                    selected = selectedRole == "PATIENT",
                    onClick = { selectedRole = "PATIENT" }
                )
                Text(text = "Paciente")
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else {
                NutriButton(
                    text = "Cadastrar",
                    onClick = { onRegisterClick(name, email, selectedRole, password) },
                    enabled = name.isNotBlank() && email.isNotBlank() && password.length >= 8
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onLoginClick) {
                Text("Já possui uma conta? Faça Login")
            }
        }
    }
}
