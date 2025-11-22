package com.example.tiendazapatos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tiendazapatos.data.repository.UserRepository

// CORRECCIÓN: La fábrica ahora no necesita ningún argumento, porque el UserRepository tampoco.
class AuthViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Crea una nueva instancia del UserRepository de red y se la pasa al ViewModel
            return AuthViewModel(UserRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
