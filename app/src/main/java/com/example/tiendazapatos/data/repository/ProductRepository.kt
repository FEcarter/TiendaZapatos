package com.example.tiendazapatos.data.repository

import com.example.tiendazapatos.data.model.Product // <-- CORREGIDO
import com.example.tiendazapatos.data.remote.RetrofitInstance
import com.example.tiendazapatos.data.remote.dao.OrderDao
import com.example.tiendazapatos.data.remote.model.Order
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val orderDao: OrderDao) {

    // --- Funciones del Microservicio ---

    suspend fun getProducts(): List<Product> {
        return RetrofitInstance.storeApi.getProducts()
    }

    // --- Funciones de la Base de Datos Local (Room) ---

    fun getAllOrders(): Flow<List<Order>> = orderDao.getAllOrders()

    suspend fun insertOrder(order: Order) {
        orderDao.insertOrder(order)
    }

    suspend fun clearAllOrders() {
        orderDao.clearAllOrders()
    }
}
