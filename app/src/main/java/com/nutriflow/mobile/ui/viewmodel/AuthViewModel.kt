package com.nutriflow.mobile.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nutriflow.mobile.data.AuthRepository
import com.nutriflow.mobile.data.model.UserDto
import com.nutriflow.mobile.data.session.SessionManager
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    data class Success(val user: UserDto) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    var uiState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    init {
        if (repository.isLoggedIn()) {
            uiState = AuthState.Authenticated
        }
    }

    fun login(email: String, passwordHash: String) {
        viewModelScope.launch {
            uiState = AuthState.Loading
            repository.login(email, passwordHash)
                .onSuccess { response ->
                    uiState = AuthState.Success(response.user)
                }
                .onFailure { error ->
                    uiState = AuthState.Error(error.message ?: "Erro desconhecido")
                }
        }
    }

    fun logout() {
        repository.logout()
        uiState = AuthState.Idle
    }

    fun getRole(): String? = repository.getStoredRole()
    
    fun resetState() {
        uiState = if (repository.isLoggedIn()) AuthState.Authenticated else AuthState.Idle
    }

    companion object {
        fun provideFactory(sessionManager: SessionManager): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AuthViewModel(AuthRepository(sessionManager))
            }
        }
    }
}
