package com.nutriflow.mobile.network

import com.nutriflow.mobile.data.model.LoginRequest
import com.nutriflow.mobile.data.model.LoginResponse
import com.nutriflow.mobile.data.model.NutritionistDashboardResponse
import com.nutriflow.mobile.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface NutriflowApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @GET("nutritionist/dashboard")
    suspend fun getNutritionistDashboard(
        @Header("Authorization") token: String
    ): Response<NutritionistDashboardResponse>
}
