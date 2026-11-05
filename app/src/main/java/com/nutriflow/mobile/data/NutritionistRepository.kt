package com.nutriflow.mobile.data

import com.nutriflow.mobile.data.cache.OfflineCacheManager
import com.nutriflow.mobile.data.model.NutritionistDashboardResponse
import com.nutriflow.mobile.data.session.SessionManager
import com.nutriflow.mobile.network.RetrofitClient

class NutritionistRepository(
    private val sessionManager: SessionManager,
    private val cacheManager: OfflineCacheManager? = null
) {
    private val apiService = RetrofitClient.apiService

    suspend fun getDashboard(): Result<NutritionistDashboardResponse> {
        val token = sessionManager.getAuthToken()
            ?: return Result.failure(Exception("Usuário não autenticado"))
        
        return try {
            val response = apiService.getNutritionistDashboard("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                cacheManager?.saveDashboardCache(data)
                Result.success(data)
            } else {
                val cachedData = cacheManager?.getDashboardCache()
                if (cachedData != null) {
                    Result.success(cachedData)
                } else {
                    Result.failure(Exception("Erro ao buscar dashboard: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            val cachedData = cacheManager?.getDashboardCache()
            if (cachedData != null) {
                Result.success(cachedData)
            } else {
                Result.failure(Exception("Sem conexão com a internet. Nenhum dado em cache disponível."))
            }
        }
    }
}
