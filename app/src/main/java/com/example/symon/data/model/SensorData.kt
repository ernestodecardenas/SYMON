package com.example.symon.data.model

/**
 * Representa la lectura de datos de telemetría provenientes del sensor IoT ESP32 de SYMON.
 */
data class SensorData(
    val temperatureCelsius: Double = 4.8,
    val humidityPercent: Double = 88.5,
    val co2Ppm: Int = 850,
    val ethylenePpm: Double = 0.12,
    val batteryPercent: Int = 92,
    val isConnected: Boolean = true,
    val lastUpdateTimestamp: String = "Hace 2 min",
    val statusLevel: StatusLevel = StatusLevel.OPTIMAL,
    val deviceId: String = "ESP32-SYMON-04"
)

enum class StatusLevel {
    OPTIMAL,   // Verde (#2E7D32)
    CAUTION,   // Amarillo (#F57C00)
    CRITICAL   // Rojo (#C62828)
}
