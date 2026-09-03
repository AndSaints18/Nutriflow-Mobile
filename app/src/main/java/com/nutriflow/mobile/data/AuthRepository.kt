package com.nutriflow.mobile.data

import com.nutriflow.mobile.data.model.LoginRequest
import com.nutriflow.mobile.data.model.LoginResponse
import com.nutriflow.mobile.data.session.SessionManager
import com.nutriflow.mobile.network.RetrofitClient
import retrofit2.Response

class AuthRepository(private val sessionManager: SessionManager) {
    private val apiService = RetrofitClient.apiService

    suspend fun login(email: String, passwordHash: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, passwordHash))
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

    fun logout() {
        sessionManager.clearSession()
    }

    fun isLoggedIn(): Boolean = sessionManager.getAuthToken() != null
    fun getStoredRole(): String? = sessionManager.getUserRole()
}
