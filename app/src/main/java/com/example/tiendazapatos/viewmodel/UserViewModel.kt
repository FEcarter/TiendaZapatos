package com.example.tiendazapatos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendazapatos.data.remote.dao.UserDao
import com.example.tiendazapatos.data.remote.model.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(private val dao: UserDao) : ViewModel() {
    val users = dao.getAllUsers()
        .stateIn(viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList())

    fun addUser(name: String, age: Int) {
        viewModelScope.launch {
            dao.insert(User(name = name, age = age))
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            dao.deleteUser(user)
        }
    }

}
