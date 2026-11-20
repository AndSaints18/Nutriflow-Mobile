package com.nutriflow.mobile.data.model

data class NutritionistDashboardResponse(
    val nutritionist: UserDto,
    val summary: DashboardSummaryDto,
    val patients: List<PatientDto> = emptyList(),
    val mealPlans: List<MealPlanDto> = emptyList(),
    val appointments: List<AppointmentDto> = emptyList(),
    val challenges: List<ChallengeDto> = emptyList()
)

data class ChallengeDto(
    val id: String,
    val title: String,
    val target: String,
    val participants: Int = 0,
    val progress: Int = 0
)

data class AppointmentDto(
    val id: String,
    val patientId: String? = null,
    val patient: String,
    val date: String,
    val type: String,
    val status: String
)

data class PatientDashboardResponse(
    val patient: PatientDto? = null,
    val overview: PatientOverviewDto? = null,
    val goals: PatientGoalsDto? = null,
    val meals: List<PatientMealDto> = emptyList(),
    val plan: PatientPlanDto? = null,
    val weight: PatientWeightDto? = null,
    val clinical: PatientClinicalDto? = null
)

data class PatientOverviewDto(
    val adherencePercent: Int = 0,
    val caloriesConsumed: Int = 0,
    val caloriesTarget: Int = 0,
    val fiber: Int = 0,
    val waterLiters: Double = 0.0
)

data class PatientGoalsDto(
    val items: List<PatientGoalItemDto> = emptyList(),
    val focusTitle: String? = null
)

data class PatientGoalItemDto(
    val label: String,
    val valueLabel: String,
    val percent: Int = 0
)

data class PatientMealDto(
    val id: String,
    val timeLabel: String? = null,
    val mealType: String? = null,
    val title: String,
    val description: String? = null,
    val calories: Int = 0
)

data class PatientPlanDto(
    val title: String? = null,
    val sections: List<PatientPlanSectionDto> = emptyList()
)

data class PatientPlanSectionDto(
    val slotLabel: String,
    val title: String,
    val description: String
)

data class PatientWeightDto(
    val currentLabel: String? = null,
    val targetLabel: String? = null,
    val variationLabel: String? = null,
    val initialLabel: String? = null,
    val weeklyAverageLabel: String? = null,
    val trendLabel: String? = null
)

data class PatientClinicalDto(
    val nextAppointment: PatientAppointmentDto? = null,
    val checklist: List<PatientChecklistItemDto> = emptyList()
)

data class PatientAppointmentDto(
    val dateLabel: String? = null,
    val description: String? = null
)

data class PatientChecklistItemDto(
    val id: String? = null,
    val label: String,
    val done: Boolean = false,
    val isChallenge: Boolean = false
)

data class AdminSummaryResponse(
    val admin: UserDto,
    val summary: AdminSummaryDto
)

data class AdminSummaryDto(
    val totalUsers: Int = 0,
    val totalPatients: Int = 0,
    val totalNutritionists: Int = 0,
    val totalAdmins: Int = 0,
    val activeUsers: Int = 0,
    val blockedUsers: Int = 0,
    val totalFoods: Int = 0
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
    val lastMessageTime: String? = null,
    val nutritionist: UserDto? = null
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

// Request Models
data class CreateMealEntryRequest(
    val mealTime: String,
    val description: String,
    val calories: Int
)

data class CreateWeightEntryRequest(
    val weight: Double,
    val date: String? = null
)

data class CreateMealPlanRequest(
    val patientId: String,
    val title: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val notes: String? = null
)

data class CreateChallengeRequest(
    val title: String,
    val target: String,
    val participantIds: List<String> = emptyList()
)

data class CreateAppointmentRequest(
    val patientId: String,
    val date: String,
    val type: String
)

data class CreateMessageRequest(
    val patientId: String,
    val content: String
)

data class CreateFoodRequest(
    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int
)

data class ApiResponse(
    val message: String? = null
)
