package com.example.symon.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View

/**
 * Vista personalizada para simular la gráfica de evolución de temperatura
 * con áreas de rango óptimo (semáforo) y curva de lecturas del ESP32.
 */
class SimulatedChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#00A3E0") // Celeste Tecnológico
        strokeWidth = 6f
        style = Paint.Style.STROKE
    }

    private val thresholdPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#2E7D32") // Verde Óptimo
        strokeWidth = 3f
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(12f, 8f), 0f)
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#E0E2EC")
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#5C6066")
        textSize = 28f
    }

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val path = Path()
    private val fillPath = Path()

    // Datos simulados de temperatura (°C) a lo largo del tiempo
    private val samplePoints = floatArrayOf(
        4.5f, 4.8f, 5.2f, 5.0f, 4.6f, 4.9f, 5.5f, 5.8f, 5.1f, 4.7f, 4.8f
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val padding = 60f

        if (width <= 0 || height <= 0) return

        // Dibujar líneas de cuadrícula horizontales
        for (i in 1..3) {
            val y = padding + (height - 2 * padding) * i / 4
            canvas.drawLine(padding, y, width - padding, y, gridPaint)
        }

        // Líneas de umbral óptimo (4.0°C y 6.0°C)
        val upperThresholdY = padding + (height - 2 * padding) * 0.25f
        val lowerThresholdY = padding + (height - 2 * padding) * 0.75f

        canvas.drawLine(padding, upperThresholdY, width - padding, upperThresholdY, thresholdPaint)
        canvas.drawLine(padding, lowerThresholdY, width - padding, lowerThresholdY, thresholdPaint)

        canvas.drawText("6.0°C (Max)", padding + 8f, upperThresholdY - 8f, textPaint)
        canvas.drawText("4.0°C (Min)", padding + 8f, lowerThresholdY + 28f, textPaint)

        // Trazo de la curva de datos de temperatura
        path.reset()
        fillPath.reset()

        val stepX = (width - 2 * padding) / (samplePoints.size - 1)
        val minTemp = 3.0f
        val maxTemp = 7.0f

        fun getY(temp: Float): Float {
            val normalized = (temp - minTemp) / (maxTemp - minTemp)
            return height - padding - (normalized * (height - 2 * padding))
        }

        val startX = padding
        val startY = getY(samplePoints[0])

        path.moveTo(startX, startY)
        fillPath.moveTo(startX, height - padding)
        fillPath.lineTo(startX, startY)

        for (i in 1 until samplePoints.size) {
            val x = padding + i * stepX
            val y = getY(samplePoints[i])
            val prevX = padding + (i - 1) * stepX
            val prevY = getY(samplePoints[i - 1])

            val controlX1 = prevX + (x - prevX) / 2
            val controlX2 = prevX + (x - prevX) / 2

            path.cubicTo(controlX1, prevY, controlX2, y, x, y)
            fillPath.cubicTo(controlX1, prevY, controlX2, y, x, y)
        }

        val lastX = padding + (samplePoints.size - 1) * stepX
        fillPath.lineTo(lastX, height - padding)
        fillPath.close()

        fillPaint.shader = LinearGradient(
            0f, padding, 0f, height - padding,
            Color.parseColor("#3300A3E0"),
            Color.parseColor("#0000A3E0"),
            Shader.TileMode.CLAMP
        )

        canvas.drawPath(fillPath, fillPaint)
        canvas.drawPath(path, linePaint)

        // Puntos destacados
        for (i in samplePoints.indices) {
            val x = padding + i * stepX
            val y = getY(samplePoints[i])
            canvas.drawCircle(x, y, 8f, linePaint)
        }
    }
}
