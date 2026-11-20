package com.nutriflow.mobile.data

import com.nutriflow.mobile.data.cache.OfflineCacheManager
import com.nutriflow.mobile.data.model.CreateChallengeRequest
import com.nutriflow.mobile.data.model.CreateMessageRequest
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

    suspend fun createChallenge(title: String, target: String, participantIds: List<String>): Result<Boolean> {
        val token = sessionManager.getAuthToken()
            ?: return Result.failure(Exception("Usuário não autenticado"))
        return try {
            val response = apiService.createChallenge("Bearer $token", CreateChallengeRequest(title, target, participantIds))
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Erro ao criar desafio: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addChallengeParticipant(challengeId: String, patientId: String): Result<Boolean> {
        val token = sessionManager.getAuthToken()
            ?: return Result.failure(Exception("Usuário não autenticado"))
        return try {
            val response = apiService.addChallengeParticipant("Bearer $token", challengeId, mapOf("patientId" to patientId))
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Erro ao vincular paciente: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteResource(resource: String, id: String): Result<Boolean> {
        val token = sessionManager.getAuthToken()
            ?: return Result.failure(Exception("Usuário não autenticado"))
        return try {
            val response = apiService.deleteResource("Bearer $token", resource, id)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Erro ao excluir: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateAppointmentStatus(id: String, status: String): Result<Boolean> {
        val token = sessionManager.getAuthToken()
            ?: return Result.failure(Exception("Usuário não autenticado"))
        return try {
            val response = apiService.updateAppointmentStatus("Bearer $token", id, mapOf("status" to status))
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Erro ao atualizar status: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendMessage(patientId: String, content: String): Result<Boolean> {
        val token = sessionManager.getAuthToken()
            ?: return Result.failure(Exception("Usuário não autenticado"))
        return try {
            val response = apiService.sendNutritionistMessage("Bearer $token", CreateMessageRequest(patientId = patientId, content = content))
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Erro ao enviar mensagem: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
