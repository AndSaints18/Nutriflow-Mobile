package com.nutriflow.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.nutriflow.mobile.ui.navigation.NavGraph
import com.nutriflow.mobile.ui.theme.NutriFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriFlowTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
