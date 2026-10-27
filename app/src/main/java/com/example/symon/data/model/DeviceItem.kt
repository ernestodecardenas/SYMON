package com.example.symon.data.model

/**
 * Información del dispositivo IoT ESP32 registrado en la red SYMON.
 */
data class DeviceItem(
    val deviceId: String,
    val name: String,
    val isOnline: Boolean,
    val batteryPercent: Int,
    val signalStrengthDbm: Int,
    val assignedBatch: String,
    val lastSeen: String
)
