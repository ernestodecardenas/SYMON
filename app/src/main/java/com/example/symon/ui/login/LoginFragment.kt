package com.example.symon.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.symon.R
import com.example.symon.databinding.FragmentLoginBinding

/**
 * Pantalla de Inicio de Sesión y Selección de Rol de Usuario.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRoleDropdown()
        setupListeners()
        observeViewModel()
    }

    private fun setupRoleDropdown() {
        val roles = arrayOf(
            getString(R.string.role_producer),
            getString(R.string.role_warehouse),
            getString(R.string.role_transporter)
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, roles)
        binding.actvRole.setAdapter(adapter)
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text?.toString() ?: ""
            val password = binding.etPassword.text?.toString() ?: ""
            val role = binding.actvRole.text.toString()

            viewModel.login(email, password, role)
        }

        binding.tvRegister.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Formulario de registro en proceso de habilitación...",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun observeViewModel() {
        viewModel.loginError.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.userSession.observe(viewLifecycleOwner) { session ->
            if (session.isLoggedIn) {
                Toast.makeText(
                    requireContext(),
                    "Bienvenido ${session.role}",
                    Toast.LENGTH_SHORT
                ).show()

                // Navigate to Dashboard
                findNavController().navigate(R.id.action_login_to_dashboard)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
