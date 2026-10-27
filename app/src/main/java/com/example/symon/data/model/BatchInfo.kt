package com.example.symon.data.model

/**
 * Modelo de datos para la información del lote agrícola monitoreado.
 */
data class BatchInfo(
    val batchId: String = "LOTE-2026-PLT-089",
    val cropName: String = "Palta Hass (Exportación)",
    val destination: String = "Puerto de Rotterdam, Holanda",
    val originLocation: String = "Fundo San José, Ica - Perú",
    val assignedDeviceId: String = "ESP32-SYMON-04",
    val status: String = "En Carga / Tránsito"
)
