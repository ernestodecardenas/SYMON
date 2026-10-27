package com.example.symon.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.symon.data.model.CropThreshold

/**
 * ViewModel para gestionar los umbrales de conservación por cultivo agrícola
 * y las configuraciones de alerta.
 */
class SettingsViewModel : ViewModel() {

    private val cropPresets = mapOf(
        "Palta Hass" to CropThreshold("Palta Hass", 4.0, 6.0, 85.0, 0.5, 900),
        "Arándano Biloxi / Ventura" to CropThreshold("Arándano", 0.0, 2.0, 90.0, 0.1, 800),
        "Uva Red Globe" to CropThreshold("Uva Red Globe", -0.5, 1.0, 90.0, 0.2, 850),
        "Espárrago Verde" to CropThreshold("Espárrago Verde", 2.0, 4.0, 95.0, 0.3, 1000),
        "Fresa Albion" to CropThreshold("Fresa Albion", 0.0, 2.0, 90.0, 0.1, 750)
    )

    private val _selectedThreshold = MutableLiveData<CropThreshold>()
    val selectedThreshold: LiveData<CropThreshold> = _selectedThreshold

    private val _saveSuccessEvent = MutableLiveData<Boolean>()
    val saveSuccessEvent: LiveData<Boolean> = _saveSuccessEvent

    init {
        // Cargar por defecto Palta Hass
        selectCrop("Palta Hass")
    }

    fun selectCrop(cropName: String) {
        cropPresets[cropName]?.let {
            _selectedThreshold.value = it
        }
    }

    fun saveSettings(
        cropName: String,
        tempMin: Double,
        tempMax: Double,
        humidityMin: Double,
        ethyleneMax: Double,
        co2Max: Int
    ) {
        _selectedThreshold.value = CropThreshold(
            cropName = cropName,
            tempMinCelsius = tempMin,
            tempMaxCelsius = tempMax,
            humidityMinPercent = humidityMin,
            maxEthylenePpm = ethyleneMax,
            maxCo2Ppm = co2Max
        )
        _saveSuccessEvent.value = true
    }

    fun getCropNamesList(): List<String> = cropPresets.keys.toList()

    fun clearSaveEvent() {
        _saveSuccessEvent.value = false
    }
}
