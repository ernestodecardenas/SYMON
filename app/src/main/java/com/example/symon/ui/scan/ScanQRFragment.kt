package com.example.symon.ui.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.symon.R
import com.example.symon.databinding.FragmentScanQrBinding
import com.example.symon.ui.dashboard.MonitoringViewModel

/**
 * Fragmento de Escaneo de Código QR para sincronizar el sensor ESP32 SYMON.
 * Diseñado con contenedor para CameraX / ZXing y simulación de escaneo.
 */
class ScanQRFragment : Fragment() {

    private var _binding: FragmentScanQrBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MonitoringViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanQrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        // Botón de simulación para pruebas de lectura QR
        binding.btnSimulateScan.setOnClickListener {
            val sampleDeviceId = "ESP32-SYMON-08"
            showDeviceConfirmationDialog(sampleDeviceId)
        }

        // Botón de vinculación manual por texto
        binding.btnManualLink.setOnClickListener {
            val deviceId = binding.etManualDevice.text?.toString()?.trim()
            if (!deviceId.isNullOrBlank()) {
                showDeviceConfirmationDialog(deviceId)
            } else {
                Toast.makeText(requireContext(), "Ingrese un ID de sensor válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeviceConfirmationDialog(deviceId: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Sensor Detectado")
            .setMessage("¿Desea vincular el dispositivo $deviceId al lote activo LOTE-2026-PLT-089?")
            .setPositiveButton("Vincular Dispositivo") { _, _ ->
                viewModel.processScannedQrCode(deviceId)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun observeViewModel() {
        viewModel.scanResultEvent.observe(viewLifecycleOwner) { scannedId ->
            scannedId?.let {
                Toast.makeText(
                    requireContext(),
                    "Dispositivo $it vinculado exitosamente al lote",
                    Toast.LENGTH_LONG
                ).show()

                viewModel.clearScanEvent()

                // Regresar al Dashboard con los datos actualizados
                findNavController().navigate(R.id.action_scanQR_to_dashboard)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
