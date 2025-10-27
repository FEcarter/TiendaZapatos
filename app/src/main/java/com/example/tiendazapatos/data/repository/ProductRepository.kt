package com.example.tiendazapatos.data.repository

import com.example.tiendazapatos.data.remote.dao.OrderDao
import com.example.tiendazapatos.data.remote.model.Order
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val orderDao: OrderDao) {

    fun getAllOrders(): Flow<List<Order>> = orderDao.getAllOrders()

    suspend fun insertOrder(order: Order) {
        orderDao.insertOrder(order)
    }

    suspend fun clearAllOrders() {
        orderDao.clearAllOrders()
    }
}