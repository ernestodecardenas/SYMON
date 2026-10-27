package com.example.symon.ui.dashboard

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.symon.R
import com.example.symon.data.model.StatusLevel
import com.example.symon.databinding.FragmentDashboardBinding

/**
 * Pantalla Principal / Dashboard con indicadores en tiempo real estilo semáforo
 * para temperatura, humedad, concentración de gases y estado del ESP32.
 */
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MonitoringViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.fabScanQR.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_scanQR)
        }

        binding.btnRefreshTelemetry.setOnClickListener {
            viewModel.refreshTelemetryData()
            Toast.makeText(requireContext(), "Sincronizando datos con ESP32...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewModel.sensorData.observe(viewLifecycleOwner) { sensor ->
            // Telemetría
            binding.tvTemperatureValue.text = "${sensor.temperatureCelsius} °C"
            binding.tvHumidityValue.text = "${sensor.humidityPercent} %"
            binding.tvCo2Value.text = "${sensor.co2Ppm} ppm"
            binding.tvEthyleneValue.text = "${sensor.ethylenePpm} ppm"
            binding.tvBatteryPercent.text = "Batería: ${sensor.batteryPercent}%"
            binding.tvLastUpdate.text = "Última sincro: ${sensor.lastUpdateTimestamp}"

            // Semáforo visual para Temperatura
            when (sensor.statusLevel) {
                StatusLevel.OPTIMAL -> {
                    val green = ContextCompat.getColor(requireContext(), R.color.status_green)
                    val greenBg = ContextCompat.getColor(requireContext(), R.color.status_green_bg)

                    binding.cardTemperature.strokeColor = green
                    binding.tvTemperatureValue.setTextColor(green)
                    binding.tvTempStatusBadge.apply {
                        text = "Óptimo (Verde)"
                        setTextColor(green)
                        backgroundTintList = ColorStateList.valueOf(greenBg)
                    }
                }
                StatusLevel.CAUTION -> {
                    val yellow = ContextCompat.getColor(requireContext(), R.color.status_yellow)
                    val yellowBg = ContextCompat.getColor(requireContext(), R.color.status_yellow_bg)

                    binding.cardTemperature.strokeColor = yellow
                    binding.tvTemperatureValue.setTextColor(yellow)
                    binding.tvTempStatusBadge.apply {
                        text = "Precaución (Amarillo)"
                        setTextColor(yellow)
                        backgroundTintList = ColorStateList.valueOf(yellowBg)
                    }
                }
                StatusLevel.CRITICAL -> {
                    val red = ContextCompat.getColor(requireContext(), R.color.status_red)
                    val redBg = ContextCompat.getColor(requireContext(), R.color.status_red_bg)

                    binding.cardTemperature.strokeColor = red
                    binding.tvTemperatureValue.setTextColor(red)
                    binding.tvTempStatusBadge.apply {
                        text = "Alerta Crítica (Rojo)"
                        setTextColor(red)
                        backgroundTintList = ColorStateList.valueOf(redBg)
                    }
                }
            }

            // Conexión Dispositivo
            if (sensor.isConnected) {
                binding.tvWifiStatus.text = "Wi-Fi Mesh / Conectado"
                binding.ivWifiIcon.setImageResource(R.drawable.ic_wifi)
            } else {
                binding.tvWifiStatus.text = "Offline / Memoria SD Local"
                binding.ivWifiIcon.setImageResource(R.drawable.ic_wifi_off)
            }
        }

        viewModel.batchInfo.observe(viewLifecycleOwner) { batch ->
            binding.tvCropName.text = batch.cropName
            binding.tvBatchId.text = "Código: ${batch.batchId}"
            binding.tvDestination.text = batch.destination
            binding.tvAssignedDevice.text = batch.assignedDeviceId
            binding.tvBatchStatusBadge.text = batch.status
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
