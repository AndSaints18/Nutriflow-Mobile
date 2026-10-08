package com.nutriflow.mobile.data

import com.nutriflow.mobile.data.model.LoginRequest
import com.nutriflow.mobile.data.model.LoginResponse
import com.nutriflow.mobile.data.model.RegisterRequest
import com.nutriflow.mobile.data.session.SessionManager
import com.nutriflow.mobile.network.RetrofitClient

class AuthRepository(private val sessionManager: SessionManager) {
    private val apiService = RetrofitClient.apiService

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                sessionManager.saveAuthToken(loginResponse.token)
                sessionManager.saveUserRole(loginResponse.user.profile)
                Result.success(loginResponse)
            } else {
                Result.failure(Exception("Erro no login: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, role: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.register(RegisterRequest(name, email, role, password))
            if (response.isSuccessful && response.body() != null) {
                val registerResponse = response.body()!!
                sessionManager.saveAuthToken(registerResponse.token)
                sessionManager.saveUserRole(registerResponse.user.profile)
                Result.success(registerResponse)
            } else {
                Result.failure(Exception("Erro no cadastro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        sessionManager.clearSession()
    }

    fun isLoggedIn(): Boolean = sessionManager.getAuthToken() != null
    fun getStoredRole(): String? = sessionManager.getUserRole()
}
