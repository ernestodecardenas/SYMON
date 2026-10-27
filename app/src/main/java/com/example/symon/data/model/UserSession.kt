package com.example.symon.data.model

/**
 * Modelo para la sesión del usuario en la aplicación.
 */
data class UserSession(
    val email: String,
    val role: String,
    val isLoggedIn: Boolean = false
)
