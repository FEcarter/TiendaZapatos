package com.example.tiendazapatos.data.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUri: String,
    val stock: Int = 0
)
