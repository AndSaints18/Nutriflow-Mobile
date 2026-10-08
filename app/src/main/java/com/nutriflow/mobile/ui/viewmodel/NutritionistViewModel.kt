package com.nutriflow.mobile.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nutriflow.mobile.data.NutritionistRepository
import com.nutriflow.mobile.data.model.NutritionistDashboardResponse
import com.nutriflow.mobile.data.session.SessionManager
import kotlinx.coroutines.launch

sealed class NutritionistUiState {
    object Loading : NutritionistUiState()
    data class Success(val data: NutritionistDashboardResponse) : NutritionistUiState()
    data class Error(val message: String) : NutritionistUiState()
}

class NutritionistViewModel(private val repository: NutritionistRepository) : ViewModel() {
    var uiState by mutableStateOf<NutritionistUiState>(NutritionistUiState.Loading)
        private set

    fun fetchDashboard() {
        viewModelScope.launch {
            uiState = NutritionistUiState.Loading
            repository.getDashboard()
                .onSuccess { response ->
                    uiState = NutritionistUiState.Success(response)
                }
                .onFailure { error ->
                    uiState = NutritionistUiState.Error(error.message ?: "Erro ao carregar dados")
                }
        }
    }

    companion object {
        fun provideFactory(sessionManager: SessionManager): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                NutritionistViewModel(NutritionistRepository(sessionManager))
            }
        }
    }
}
