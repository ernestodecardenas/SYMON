package com.example.symon.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.symon.data.model.BatchInfo
import com.example.symon.data.model.DeviceItem
import com.example.symon.data.model.SensorData
import com.example.symon.data.model.StatusLevel
import kotlin.random.Random

/**
 * ViewModel encargado del monitoreo en tiempo real de los sensores IoT ESP32,
 * el estado del lote activo y la gestión de dispositivos conectados.
 */
class MonitoringViewModel : ViewModel() {

    private val _sensorData = MutableLiveData<SensorData>()
    val sensorData: LiveData<SensorData> = _sensorData

    private val _batchInfo = MutableLiveData<BatchInfo>()
    val batchInfo: LiveData<BatchInfo> = _batchInfo

    private val _devicesList = MutableLiveData<List<DeviceItem>>()
    val devicesList: LiveData<List<DeviceItem>> = _devicesList

    private val _scanResultEvent = MutableLiveData<String?>()
    val scanResultEvent: LiveData<String?> = _scanResultEvent

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _sensorData.value = SensorData(
            temperatureCelsius = 4.8,
            humidityPercent = 88.5,
            co2Ppm = 850,
            ethylenePpm = 0.12,
            batteryPercent = 92,
            isConnected = true,
            lastUpdateTimestamp = "Hace 1 min",
            statusLevel = StatusLevel.OPTIMAL,
            deviceId = "ESP32-SYMON-04"
        )

        _batchInfo.value = BatchInfo(
            batchId = "LOTE-2026-PLT-089",
            cropName = "Palta Hass (Exportación)",
            destination = "Puerto de Rotterdam, Holanda",
            originLocation = "Fundo San José, Ica",
            assignedDeviceId = "ESP32-SYMON-04",
            status = "En Carga / Tránsito"
        )

        _devicesList.value = listOf(
            DeviceItem("ESP32-SYMON-01", "Sensor Cámara Fría 1", true, 98, -62, "LOTE-2026-ARAN-012", "Hace 30 seg"),
            DeviceItem("ESP32-SYMON-02", "Sensor Cámara Fría 2", true, 85, -71, "LOTE-2026-UVA-044", "Hace 1 min"),
            DeviceItem("ESP32-SYMON-04", "Sensor Camión Transp. T-12", true, 92, -58, "LOTE-2026-PLT-089", "Hace 1 min"),
            DeviceItem("ESP32-SYMON-07", "Sensor Almacén A-3", false, 15, -95, "Sin Lote Asignado", "Hace 4 horas")
        )
    }

    /**
     * Simula la actualización en tiempo real de datos de telemetría provenientes del ESP32.
     */
    fun refreshTelemetryData() {
        val current = _sensorData.value ?: SensorData()
        val randomTemp = (4.0 + Random.nextDouble() * 3.0) // 4.0 - 7.0 °C
        val randomHum = (84.0 + Random.nextDouble() * 7.0) // 84 - 91 %
        val randomCo2 = (780 + Random.nextInt(200))

        val level = when {
            randomTemp > 6.5 -> StatusLevel.CRITICAL
            randomTemp > 5.5 -> StatusLevel.CAUTION
            else -> StatusLevel.OPTIMAL
        }

        _sensorData.value = current.copy(
            temperatureCelsius = Math.round(randomTemp * 10.0) / 10.0,
            humidityPercent = Math.round(randomHum * 10.0) / 10.0,
            co2Ppm = randomCo2,
            lastUpdateTimestamp = "Justo ahora",
            statusLevel = level
        )
    }

    /**
     * Procesa el código QR escaneado del sensor ESP32 y vincula el dispositivo.
     */
    fun processScannedQrCode(qrCode: String) {
        _batchInfo.value = _batchInfo.value?.copy(
            assignedDeviceId = qrCode,
            status = "Sensor $qrCode Vinculado"
        )
        _scanResultEvent.value = qrCode
    }

    fun clearScanEvent() {
        _scanResultEvent.value = null
    }
}
