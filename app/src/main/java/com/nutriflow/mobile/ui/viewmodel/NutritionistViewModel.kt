package com.nutriflow.mobile.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nutriflow.mobile.data.NutritionistRepository
import com.nutriflow.mobile.data.cache.OfflineCacheManager
import com.nutriflow.mobile.data.model.NutritionistDashboardResponse
import com.nutriflow.mobile.data.session.SessionManager
import kotlinx.coroutines.launch

sealed class NutritionistUiState {
    object Loading : NutritionistUiState()
    data class Success(val data: NutritionistDashboardResponse, val isOffline: Boolean = false) : NutritionistUiState()
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

    fun createChallenge(title: String, target: String, participantIds: List<String> = emptyList(), onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            repository.createChallenge(title, target, participantIds)
                .onSuccess {
                    fetchDashboard()
                    onResult(true)
                }
                .onFailure {
                    onResult(false)
                }
        }
    }

    fun addChallengeParticipant(challengeId: String, patientId: String, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            repository.addChallengeParticipant(challengeId, patientId)
                .onSuccess {
                    fetchDashboard()
                    onResult(true)
                }
                .onFailure {
                    onResult(false)
                }
        }
    }

    fun deleteResource(resource: String, id: String, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteResource(resource, id)
                .onSuccess {
                    fetchDashboard()
                    onResult(true)
                }
                .onFailure {
                    onResult(false)
                }
        }
    }

    fun updateAppointmentStatus(id: String, status: String, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(id, status)
                .onSuccess {
                    fetchDashboard()
                    onResult(true)
                }
                .onFailure {
                    onResult(false)
                }
        }
    }

    fun sendMessage(patientId: String, content: String, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            repository.sendMessage(patientId, content)
                .onSuccess {
                    fetchDashboard()
                    onResult(true)
                }
                .onFailure {
                    onResult(false)
                }
        }
    }

    companion object {
        fun provideFactory(context: Context, sessionManager: SessionManager): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val cacheManager = OfflineCacheManager(context)
                NutritionistViewModel(NutritionistRepository(sessionManager, cacheManager))
            }
        }
    }
}
