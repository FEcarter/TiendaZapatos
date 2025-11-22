package com.example.tiendazapatos.data.model

// Este data class representa el JSON que se envía al servidor para login y registro
data class AuthRequest(
    val username: String,
    val password: String
)
