package com.nutriflow.mobile.data.model

data class LoginRequest(
    val email: String,
    val passwordHash: String // The backend expects passwordHash based on Prisma schema
)

data class LoginResponse(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val profile: String // "PATIENT", "NUTRITIONIST", "ADMIN"
)
