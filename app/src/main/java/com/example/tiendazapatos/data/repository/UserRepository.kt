package com.example.tiendazapatos.data.repository

import com.example.tiendazapatos.data.model.AuthRequest
import com.example.tiendazapatos.data.remote.RetrofitInstance
import retrofit2.Response

// CORRECCIÓN: El repositorio ahora implementa la interfaz de red correcta.
class UserRepository : UserRepositoryInterface {

    override suspend fun register(authRequest: AuthRequest): Response<Unit> {
        return RetrofitInstance.storeApi.register(authRequest)
    }

    override suspend fun login(authRequest: AuthRequest): Response<Unit> {
        return RetrofitInstance.storeApi.login(authRequest)
    }
}
