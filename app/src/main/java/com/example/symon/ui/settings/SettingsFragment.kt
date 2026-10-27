package com.example.symon.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.symon.databinding.FragmentSettingsBinding

/**
 * Pantalla de Configuración de Umbrales por cultivo agrícola
 * y notificaciones de alerta para la cadena de frío.
 */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCropDropdown()
        setupListeners()
        observeViewModel()
    }

    private fun setupCropDropdown() {
        val cropsList = viewModel.getCropNamesList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cropsList)
        binding.actvCropSelect.setAdapter(adapter)

        binding.actvCropSelect.setOnItemClickListener { _, _, position, _ ->
            val selectedCrop = cropsList[position]
            viewModel.selectCrop(selectedCrop)
        }
    }

    private fun setupListeners() {
        binding.btnSaveSettings.setOnClickListener {
            val cropName = binding.actvCropSelect.text.toString()
            val tempMin = binding.etTempMin.text?.toString()?.toDoubleOrNull() ?: 4.0
            val tempMax = binding.etTempMax.text?.toString()?.toDoubleOrNull() ?: 6.0
            val humMin = binding.etHumidityMin.text?.toString()?.toDoubleOrNull() ?: 85.0
            val ethyleneMax = binding.etEthyleneMax.text?.toString()?.toDoubleOrNull() ?: 0.50
            val co2Max = binding.etCo2Max.text?.toString()?.toIntOrNull() ?: 900

            viewModel.saveSettings(cropName, tempMin, tempMax, humMin, ethyleneMax, co2Max)
        }
    }

    private fun observeViewModel() {
        viewModel.selectedThreshold.observe(viewLifecycleOwner) { threshold ->
            binding.etTempMin.setText(threshold.tempMinCelsius.toString())
            binding.etTempMax.setText(threshold.tempMaxCelsius.toString())
            binding.etHumidityMin.setText(threshold.humidityMinPercent.toString())
            binding.etEthyleneMax.setText(threshold.maxEthylenePpm.toString())
            binding.etCo2Max.setText(threshold.maxCo2Ppm.toString())
        }

        viewModel.saveSuccessEvent.observe(viewLifecycleOwner) { saved ->
            if (saved) {
                Toast.makeText(
                    requireContext(),
                    "¡Configuración de umbrales guardada correctamente!",
                    Toast.LENGTH_LONG
                ).show()
                viewModel.clearSaveEvent()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
