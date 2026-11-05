package com.nutriflow.mobile.ui.utils

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.nutriflow.mobile.data.model.MealPlanDto
import java.io.File
import java.io.FileOutputStream

class PdfExporter(private val context: Context) {

    fun generateAndShareMealPlanPdf(mealPlan: MealPlanDto, patientName: String) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint()
        val titlePaint = Paint().apply {
            color = Color.BLACK
            textSize = 18f
            isFakeBoldText = true
        }
        val textPaint = Paint().apply {
            color = Color.DKGRAY
            textSize = 12f
        }

        var y = 50f

        canvas.drawText("NutriFlow - Plano Alimentar", 50f, y, titlePaint)
        y += 30f
        canvas.drawText("Paciente: $patientName", 50f, y, textPaint)
        y += 20f
        canvas.drawText("Título do Plano: ${mealPlan.title}", 50f, y, textPaint)
        y += 20f
        canvas.drawText("Calorias Totais: ${mealPlan.calories} kcal", 50f, y, textPaint)
        y += 20f
        canvas.drawText("Proteínas: ${mealPlan.protein}g | Carbos: ${mealPlan.carbs}g | Gorduras: ${mealPlan.fats}g", 50f, y, textPaint)
        y += 30f

        canvas.drawLine(50f, y, 545f, y, paint)
        y += 20f

        canvas.drawText("Refeições Prescritas:", 50f, y, titlePaint)
        y += 25f

        if (mealPlan.items.isNotEmpty()) {
            mealPlan.items.forEach { item ->
                canvas.drawText("${item.mealTime}: ${item.food} (${item.quantity}g)", 50f, y, textPaint)
                y += 20f
            }
        } else {
            canvas.drawText("- Café da Manhã: 3 Ovos mexidos + 2 pães integrais", 50f, y, textPaint)
            y += 20f
            canvas.drawText("- Almoço: 150g Frango grelhado + 100g Arroz Integral", 50f, y, textPaint)
            y += 20f
            canvas.drawText("- Jantar: 120g Patinho moído + Batata doce", 50f, y, textPaint)
            y += 20f
        }

        pdfDocument.finishPage(page)

        val file = File(context.cacheDir, "plano_alimentar_$patientName.pdf")
        try {
            FileOutputStream(file).use { out ->
                pdfDocument.writeTo(out)
            }
            pdfDocument.close()
            sharePdfFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
        }
    }

    private fun sharePdfFile(file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(shareIntent, "Compartilhar Plano Alimentar")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }
}
