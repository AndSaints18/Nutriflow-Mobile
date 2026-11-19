package com.nutriflow.mobile.network

import com.nutriflow.mobile.data.model.AdminSummaryResponse
import com.nutriflow.mobile.data.model.ApiResponse
import com.nutriflow.mobile.data.model.CreateAppointmentRequest
import com.nutriflow.mobile.data.model.CreateFoodRequest
import com.nutriflow.mobile.data.model.CreateMealEntryRequest
import com.nutriflow.mobile.data.model.CreateMealPlanRequest
import com.nutriflow.mobile.data.model.CreateMessageRequest
import com.nutriflow.mobile.data.model.CreateWeightEntryRequest
import com.nutriflow.mobile.data.model.LoginRequest
import com.nutriflow.mobile.data.model.LoginResponse
import com.nutriflow.mobile.data.model.NutritionistDashboardResponse
import com.nutriflow.mobile.data.model.PatientDashboardResponse
import com.nutriflow.mobile.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface NutriflowApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @GET("nutritionist/dashboard")
    suspend fun getNutritionistDashboard(
        @Header("Authorization") token: String
    ): Response<NutritionistDashboardResponse>

    @POST("nutritionist/meal-plans")
    suspend fun createMealPlan(
        @Header("Authorization") token: String,
        @Body request: CreateMealPlanRequest
    ): Response<ApiResponse>

    @POST("nutritionist/appointments")
    suspend fun createAppointment(
        @Header("Authorization") token: String,
        @Body request: CreateAppointmentRequest
    ): Response<ApiResponse>

    @PATCH("nutritionist/appointments/{id}/status")
    suspend fun updateAppointmentStatus(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: Map<String, String>
    ): Response<ApiResponse>

    @DELETE("nutritionist/{resource}/{id}")
    suspend fun deleteResource(
        @Header("Authorization") token: String,
        @Path("resource") resource: String,
        @Path("id") id: String
    ): Response<ApiResponse>

    @POST("nutritionist/messages")
    suspend fun sendNutritionistMessage(
        @Header("Authorization") token: String,
        @Body request: CreateMessageRequest
    ): Response<ApiResponse>

    @GET("patient/dashboard")
    suspend fun getPatientDashboard(
        @Header("Authorization") token: String
    ): Response<PatientDashboardResponse>

    @POST("patient/meals")
    suspend fun createMealEntry(
        @Header("Authorization") token: String,
        @Body request: CreateMealEntryRequest
    ): Response<ApiResponse>

    @POST("patient/weights")
    suspend fun createWeightEntry(
        @Header("Authorization") token: String,
        @Body request: CreateWeightEntryRequest
    ): Response<ApiResponse>

    @GET("admin/summary")
    suspend fun getAdminSummary(
        @Header("Authorization") token: String
    ): Response<AdminSummaryResponse>

    @POST("admin/foods")
    suspend fun createFood(
        @Header("Authorization") token: String,
        @Body request: CreateFoodRequest
    ): Response<ApiResponse>
}
