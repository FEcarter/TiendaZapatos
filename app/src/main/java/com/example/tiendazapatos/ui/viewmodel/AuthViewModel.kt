package com.example.tiendazapatos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendazapatos.data.model.AuthRequest
import com.example.tiendazapatos.data.remote.model.User
import com.example.tiendazapatos.data.repository.UserRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// CORRECCIÓN: El ViewModel ahora depende de la interfaz de red correcta
class AuthViewModel(private val userRepository: UserRepositoryInterface) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    fun login(name: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = userRepository.login(AuthRequest(username = name, password = password))
                if (response.isSuccessful) {
                    _currentUser.update { User(name = name, password = password, age = 0) }
                    onResult(true, "¡Inicio de sesión exitoso!")
                } else {
                    onResult(false, "Nombre de usuario o contraseña incorrectos.")
                }
            } catch (e: Exception) {
                onResult(false, "Error de red: ${e.message}")
            }
        }
    }

    fun register(name: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = userRepository.register(AuthRequest(username = name, password = password))
                when (response.code()) {
                    201 -> onResult(true, "¡Registro exitoso! Por favor, inicia sesión.")
                    409 -> onResult(false, "El nombre de usuario ya existe.")
                    else -> onResult(false, "Error desconocido en el registro (Código: ${response.code()})")
                }
            } catch (e: Exception) {
                onResult(false, "Error de red: ${e.message}")
            }
        }
    }

    fun logout() {
        _currentUser.update { null }
    }
}
