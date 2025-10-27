package com.example.tiendazapatos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendazapatos.data.remote.model.User
import com.example.tiendazapatos.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    fun login(name: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUserByName(name) // <-- Usamos el repositorio
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
            val existingUser = userRepository.getUserByName(name) // <-- Usamos el repositorio
            if (existingUser != null) {
                onResult(false, "El nombre de usuario ya existe.")
            } else {
                val newUser = User(name = name, password = password, age = 99)
                userRepository.insertUser(newUser) // <-- Usamos el repositorio
                onResult(true, "¡Registro exitoso! Por favor, inicia sesión.")
            }
        }
    }

    fun logout() {
        _currentUser.update { null }
    }
}
