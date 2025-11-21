package com.example.tiendazapatos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendazapatos.data.remote.model.User
import com.example.tiendazapatos.data.repository.UserRepositoryInterface // <-- CORREGIDO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// El ViewModel ahora depende de la interfaz
class AuthViewModel(private val userRepository: UserRepositoryInterface) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    fun login(name: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUserByName(name)
            if (user != null && user.password == password) {
                _currentUser.update { user }
                onResult(true, "¡Inicio de sesión exitoso!")
            } else {
                onResult(false, "Nombre de usuario o contraseña incorrectos.")
            }
        }
    }

    fun register(name: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val existingUser = userRepository.getUserByName(name)
            if (existingUser != null) {
                onResult(false, "El nombre de usuario ya existe.")
            } else {
                val newUser = User(name = name, password = password, age = 99)
                userRepository.insertUser(newUser)
                onResult(true, "¡Registro exitoso! Por favor, inicia sesión.")
            }
        }
    }

    fun logout() {
        _currentUser.update { null }
    }
}
