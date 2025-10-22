package com.example.tiendazapatos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendazapatos.data.remote.dao.UserDao
import com.example.tiendazapatos.data.remote.model.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(private val dao: UserDao) : ViewModel() {
    // Este flujo de datos sigue siendo útil si en el futuro se quiere
    // mostrar una lista de todos los usuarios en la sección de admin.
    val users = dao.getAllUsers()
        .stateIn(viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList())

    // La función de añadir usuario ahora es gestionada por AuthViewModel para incluir la contraseña.
    // La eliminamos de aquí para evitar confusiones.

    // La función de borrar sigue siendo válida.
    fun deleteUser(user: User) {
        viewModelScope.launch {
            dao.deleteUser(user)
        }
    }
}
