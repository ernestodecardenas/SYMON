package com.example.symon.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.symon.data.model.UserSession

/**
 * ViewModel para autenticación y selección de roles de usuario.
 */
class LoginViewModel : ViewModel() {

    private val _userSession = MutableLiveData<UserSession>()
    val userSession: LiveData<UserSession> = _userSession

    private val _loginError = MutableLiveData<String?>()
    val loginError: LiveData<String?> = _loginError

    fun login(email: String, pass: String, role: String) {
        if (email.isBlank() || !email.contains("@")) {
            _loginError.value = "Por favor ingrese un correo electrónico válido"
            return
        }
        if (pass.length < 4) {
            _loginError.value = "La contraseña debe tener al menos 4 caracteres"
            return
        }

        _loginError.value = null
        _userSession.value = UserSession(
            email = email,
            role = role,
            isLoggedIn = true
        )
    }

    fun logout() {
        _userSession.value = UserSession(email = "", role = "", isLoggedIn = false)
    }
}
