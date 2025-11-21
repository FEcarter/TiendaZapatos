package com.example.tiendazapatos.data.repository

import com.example.tiendazapatos.data.model.Product
import com.example.tiendazapatos.data.remote.model.Order
import kotlinx.coroutines.flow.Flow

interface ProductRepositoryInterface {
    suspend fun getProducts(): List<Product>
    fun getAllOrders(): Flow<List<Order>>
    suspend fun insertOrder(order: Order)
    suspend fun clearAllOrders()
}
