package com.nutriflow.mobile.data.model

data class NutritionistDashboardResponse(
    val nutritionist: UserDto,
    val summary: DashboardSummaryDto,
    val patients: List<PatientDto> = emptyList(),
    val mealPlans: List<MealPlanDto> = emptyList()
)

data class DashboardSummaryDto(
    val activePatients: Int = 0,
    val activePlans: Int = 0,
    val monthlyAssessments: Int = 0,
    val pendingMessages: Int = 0,
    val pendingAppointments: Int = 0
)

data class PatientDto(
    val id: String,
    val userId: String? = null,
    val name: String,
    val email: String? = null,
    val age: Int? = null,
    val objective: String? = null,
    val status: String? = null,
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val restrictions: String? = null,
    val lastMeal: String? = null,
    val progress: Int = 0,
    val lastAssessment: String? = null,
    val currentPlan: String? = null,
    val bodyFat: Double = 0.0,
    val nextAppointment: String? = null,
    val lastMessagePreview: String? = null,
    val lastMessageTime: String? = null
)

data class MealPlanDto(
    val id: String,
    val patientId: String,
    val patient: String? = null,
    val title: String,
    val calories: Int = 0,
    val protein: Int = 0,
    val carbs: Int = 0,
    val fats: Int = 0,
    val notes: String? = null,
    val status: String? = null,
    val items: List<MealPlanItemDto> = emptyList()
)

data class MealPlanItemDto(
    val id: String? = null,
    val foodId: String? = null,
    val food: String,
    val quantity: Double = 0.0,
    val mealTime: String,
    val calories: Int = 0,
    val protein: Int = 0,
    val carbs: Int = 0,
    val fats: Int = 0
)
