package com.nutriflow.mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nutriflow.mobile.data.session.SessionManager
import com.nutriflow.mobile.ui.screens.AdminDashboard
import com.nutriflow.mobile.ui.screens.AppointmentsScreen
import com.nutriflow.mobile.ui.screens.ChatScreen
import com.nutriflow.mobile.ui.screens.CreateMealPlanScreen
import com.nutriflow.mobile.ui.screens.LoginScreen
import com.nutriflow.mobile.ui.screens.NutritionistDashboard
import com.nutriflow.mobile.ui.screens.PatientDashboard
import com.nutriflow.mobile.ui.screens.PatientDetailsScreen
import com.nutriflow.mobile.ui.screens.PatientListScreen
import com.nutriflow.mobile.ui.screens.RegisterScreen
import com.nutriflow.mobile.ui.viewmodel.AuthState
import com.nutriflow.mobile.ui.viewmodel.AuthViewModel

fun isPatientProfile(profile: String?): Boolean {
    if (profile == null) return false
    val p = profile.trim()
    return p.equals("PATIENT", ignoreCase = true) || p.equals("Paciente", ignoreCase = true)
}

fun isAdminProfile(profile: String?): Boolean {
    if (profile == null) return false
    val p = profile.trim()
    return p.equals("ADMIN", ignoreCase = true) || p.equals("Administrador", ignoreCase = true)
}

fun getDestinationRouteForProfile(profile: String?): String {
    return when {
        isAdminProfile(profile) -> Screen.AdminDashboard.route
        isPatientProfile(profile) -> Screen.PatientDashboard.route
        else -> Screen.NutritionistDashboard.route
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.provideFactory(sessionManager)
    )
    
    NavHost(
        navController = navController,
        startDestination = if (authViewModel.uiState is AuthState.Authenticated) {
            getDestinationRouteForProfile(authViewModel.getRole())
        } else {
            Screen.Login.route
        }
    ) {
        composable(Screen.Login.route) {
            val state = authViewModel.uiState
            
            LoginScreen(
                onLoginClick = { email, password ->
                    authViewModel.login(email, password)
                },
                onRegisterClick = {
                    authViewModel.resetState()
                    navController.navigate(Screen.Register.route)
                },
                isLoading = state is AuthState.Loading,
                errorMessage = if (state is AuthState.Error) state.message else null
            )
            
            if (state is AuthState.Success) {
                LaunchedEffect(state) {
                    val route = getDestinationRouteForProfile(state.user.profile)
                    navController.navigate(route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
        }
        composable(Screen.Register.route) {
            val state = authViewModel.uiState

            RegisterScreen(
                onRegisterClick = { name, email, role, password ->
                    authViewModel.register(name, email, role, password)
                },
                onLoginClick = {
                    authViewModel.resetState()
                    navController.popBackStack()
                },
                isLoading = state is AuthState.Loading,
                errorMessage = if (state is AuthState.Error) state.message else null
            )

            if (state is AuthState.Success) {
                LaunchedEffect(state) {
                    val route = getDestinationRouteForProfile(state.user.profile)
                    navController.navigate(route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            }
        }
        composable(Screen.PatientDashboard.route) {
            val currentUserName = authViewModel.getUserName() ?: "Paciente"
            PatientDashboard(
                patientName = currentUserName,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.PatientDashboard.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.NutritionistDashboard.route) {
            val currentUserName = authViewModel.getUserName() ?: "Nutricionista"
            NutritionistDashboard(
                nutritionistName = currentUserName,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.NutritionistDashboard.route) { inclusive = true }
                    }
                },
                onViewPatientsClick = {
                    navController.navigate(Screen.PatientList.route)
                },
                onViewAppointmentsClick = {
                    navController.navigate(Screen.Appointments.route)
                },
                onCreateMealPlanClick = {
                    navController.navigate(Screen.CreateMealPlan.route)
                },
                onOpenChatClick = {
                    navController.navigate(Screen.Chat.createRoute("Amanda Rocha"))
                }
            )
        }
        composable(Screen.AdminDashboard.route) {
            val currentUserName = authViewModel.getUserName() ?: "Administrador"
            AdminDashboard(
                adminName = currentUserName,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.AdminDashboard.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.PatientList.route) {
            PatientListScreen(
                onBackClick = { navController.popBackStack() },
                onPatientClick = { patientId ->
                    navController.navigate(Screen.PatientDetails.createRoute(patientId))
                }
            )
        }
        composable(Screen.PatientDetails.route) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            PatientDetailsScreen(
                patientId = patientId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.Appointments.route) {
            AppointmentsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.CreateMealPlan.route) {
            CreateMealPlanScreen(
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }
        composable(Screen.Chat.route) { backStackEntry ->
            val patientName = backStackEntry.arguments?.getString("patientName") ?: "Paciente"
            ChatScreen(
                patientName = patientName,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
