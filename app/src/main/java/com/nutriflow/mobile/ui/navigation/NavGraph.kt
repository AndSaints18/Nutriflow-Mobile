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
import com.nutriflow.mobile.ui.screens.LoginScreen
import com.nutriflow.mobile.ui.screens.PatientDashboard
import com.nutriflow.mobile.ui.screens.NutritionistDashboard
import com.nutriflow.mobile.ui.viewmodel.AuthViewModel
import com.nutriflow.mobile.ui.viewmodel.AuthState

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
            if (authViewModel.getRole() == "PATIENT") Screen.PatientDashboard.route else Screen.NutritionistDashboard.route
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
                isLoading = state is AuthState.Loading,
                errorMessage = if (state is AuthState.Error) state.message else null
            )
            
            if (state is AuthState.Success) {
                LaunchedEffect(state) {
                    val route = if (state.user.profile == "PATIENT") {
                        Screen.PatientDashboard.route
                    } else {
                        Screen.NutritionistDashboard.route
                    }
                    navController.navigate(route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
        }
        composable(Screen.PatientDashboard.route) {
            PatientDashboard(onLogout = {
                authViewModel.logout()
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.PatientDashboard.route) { inclusive = true }
                }
            })
        }
        composable(Screen.NutritionistDashboard.route) {
            NutritionistDashboard(onLogout = {
                authViewModel.logout()
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.NutritionistDashboard.route) { inclusive = true }
                }
            })
        }
    }
}
