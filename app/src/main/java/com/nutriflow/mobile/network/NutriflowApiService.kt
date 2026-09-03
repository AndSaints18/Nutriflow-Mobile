package com.nutriflow.mobile.network

import com.nutriflow.mobile.data.model.LoginRequest
import com.nutriflow.mobile.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NutriflowApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
