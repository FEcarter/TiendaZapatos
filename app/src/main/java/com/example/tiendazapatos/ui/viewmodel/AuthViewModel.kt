package com.example.tiendazapatos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendazapatos.data.remote.dao.UserDao
import com.example.tiendazapatos.data.remote.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val userDao: UserDao) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    // Función para iniciar sesión
    fun login(name: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val user = userDao.getUserByName(name)
            if (user != null && user.password == password) {
                _currentUser.update { user }
                onResult(true, "¡Inicio de sesión exitoso!")
            } else {
                onResult(false, "Nombre de usuario o contraseña incorrectos.")
            }
        }
    }

    // Función para registrar un nuevo usuario
    fun register(name: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val existingUser = userDao.getUserByName(name)
            if (existingUser != null) {
                onResult(false, "El nombre de usuario ya existe.")
            } else {
                // La edad es un campo obligatorio, usaremos un valor por defecto.
                val newUser = User(name = name, password = password, age = 99)
                userDao.insertUser(newUser)
                onResult(true, "¡Registro exitoso! Por favor, inicia sesión.")
            }
        }
    }

    // Función para cerrar sesión
    fun logout() {
        _currentUser.update { null }
    }
}
