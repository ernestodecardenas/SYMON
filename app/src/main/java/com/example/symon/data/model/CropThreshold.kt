package com.example.symon.data.model

/**
 * Configuración de umbrales óptimos por cultivo agrícola para la cadena de frío.
 */
data class CropThreshold(
    val cropName: String,
    val tempMinCelsius: Double,
    val tempMaxCelsius: Double,
    val humidityMinPercent: Double,
    val maxEthylenePpm: Double,
    val maxCo2Ppm: Int
)
