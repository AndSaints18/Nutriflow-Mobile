package com.nutriflow.mobile.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object PatientDashboard : Screen("patient_dashboard")
    object NutritionistDashboard : Screen("nutritionist_dashboard")
    object PatientList : Screen("patient_list")
    object PatientDetails : Screen("patient_details/{patientId}") {
        fun createRoute(patientId: String) = "patient_details/$patientId"
    }
    object CreateMealPlan : Screen("create_meal_plan")
    object Appointments : Screen("appointments")
    object Chat : Screen("chat/{patientName}") {
        fun createRoute(patientName: String) = "chat/$patientName"
    }
}
