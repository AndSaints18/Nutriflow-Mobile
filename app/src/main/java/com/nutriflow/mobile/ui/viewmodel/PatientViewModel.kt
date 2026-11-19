package com.nutriflow.mobile.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nutriflow.mobile.data.model.PatientDashboardResponse
import com.nutriflow.mobile.data.session.SessionManager
import com.nutriflow.mobile.network.RetrofitClient
import kotlinx.coroutines.launch

sealed class PatientUiState {
    object Loading : PatientUiState()
    data class Success(val data: PatientDashboardResponse) : PatientUiState()
    data class Error(val message: String) : PatientUiState()
}

class PatientViewModel(private val sessionManager: SessionManager) : ViewModel() {
    var uiState by mutableStateOf<PatientUiState>(PatientUiState.Loading)
        private set

    fun fetchDashboard() {
        viewModelScope.launch {
            uiState = PatientUiState.Loading
            val token = sessionManager.getAuthToken()
            if (token == null) {
                uiState = PatientUiState.Error("Sessão não autenticada")
                return@launch
            }
            try {
                val response = RetrofitClient.apiService.getPatientDashboard("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    uiState = PatientUiState.Success(response.body()!!)
                } else {
                    uiState = PatientUiState.Error("Erro ao buscar dados: ${response.code()}")
                }
            } catch (e: Exception) {
                uiState = PatientUiState.Error(e.message ?: "Erro de conexão")
            }
        }
    }

    companion object {
        fun provideFactory(sessionManager: SessionManager): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PatientViewModel(sessionManager)
            }
        }
    }
}
