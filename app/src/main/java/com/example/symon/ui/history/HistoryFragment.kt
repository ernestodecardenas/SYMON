package com.example.symon.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.symon.databinding.FragmentHistoryBinding

/**
 * Pantalla de Historial y Reportes de la cadena de frío.
 * Muestra la gráfica simulada de evolución y exportación de reportes PDF/Excel.
 */
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnExportReport.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Generando reporte PDF 'SYMON_Reporte_LOTE-2026-PLT-089.pdf'...",
                Toast.LENGTH_LONG
            ).show()
        }

        binding.chipGroupRange.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Filtrando historial de lecturas de telemetría...",
                    Toast.LENGTH_SHORT
                ).show()
                binding.chartView.invalidate() // Redibujar gráfica
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
