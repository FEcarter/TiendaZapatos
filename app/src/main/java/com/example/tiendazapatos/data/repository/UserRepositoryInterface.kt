package com.example.tiendazapatos.data.repository

import com.example.tiendazapatos.data.remote.model.User

interface UserRepositoryInterface {
    suspend fun getUserByName(name: String): User?
    suspend fun insertUser(user: User)
}
