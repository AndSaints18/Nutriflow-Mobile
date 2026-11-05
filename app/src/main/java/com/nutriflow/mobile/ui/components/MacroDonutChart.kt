package com.nutriflow.mobile.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun MacroDonutChart(
    proteinPercent: Float = 0.35f,
    carbsPercent: Float = 0.45f,
    fatsPercent: Float = 0.20f,
    modifier: Modifier = Modifier.size(100.dp)
) {
    val proteinColor = Color(0xFF4CAF50)
    val carbsColor = Color(0xFF2196F3)
    val fatsColor = Color(0xFFFF9800)

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 14.dp.toPx()

            val sweepProtein = proteinPercent * 360f
            val sweepCarbs = carbsPercent * 360f
            val sweepFats = fatsPercent * 360f

            var startAngle = -90f

            drawArc(
                color = proteinColor,
                startAngle = startAngle,
                sweepAngle = sweepProtein,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            startAngle += sweepProtein

            drawArc(
                color = carbsColor,
                startAngle = startAngle,
                sweepAngle = sweepCarbs,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            startAngle += sweepCarbs

            drawArc(
                color = fatsColor,
                startAngle = startAngle,
                sweepAngle = sweepFats,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }
}
