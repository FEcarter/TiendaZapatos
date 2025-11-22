package com.example.tiendazapatos.data.repository

import com.example.tiendazapatos.data.model.AuthRequest
import com.example.tiendazapatos.data.remote.model.User
import retrofit2.Response

// CORRECCIÓN: La interfaz ahora define las operaciones de RED, no de la base de datos local.
interface UserRepositoryInterface {
    // Ya no necesitamos getUserByName e insertUser aquí, porque eso lo maneja el backend.

    suspend fun register(authRequest: AuthRequest): Response<Unit>
    suspend fun login(authRequest: AuthRequest): Response<Unit>
}
