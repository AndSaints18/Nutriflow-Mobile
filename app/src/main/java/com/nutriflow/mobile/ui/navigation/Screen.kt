package com.nutriflow.mobile.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object PatientDashboard : Screen("patient_dashboard")
    object NutritionistDashboard : Screen("nutritionist_dashboard")
}
