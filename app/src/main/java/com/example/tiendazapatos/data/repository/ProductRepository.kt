package com.example.tiendazapatos.data.repository

import com.example.tiendazapatos.data.model.Product
import com.example.tiendazapatos.data.remote.RetrofitInstance
import com.example.tiendazapatos.data.remote.dao.OrderDao
import com.example.tiendazapatos.data.remote.model.Order
import kotlinx.coroutines.flow.Flow

// Ahora, el repositorio real implementa la interfaz
class ProductRepository(private val orderDao: OrderDao) : ProductRepositoryInterface {

    // --- Funciones del Microservicio ---

    override suspend fun getProducts(): List<Product> {
        return RetrofitInstance.storeApi.getProducts()
    }

    // --- Funciones de la Base de Datos Local (Room) ---

    override fun getAllOrders(): Flow<List<Order>> = orderDao.getAllOrders()

    override suspend fun insertOrder(order: Order) {
        orderDao.insertOrder(order)
    }

    override suspend fun clearAllOrders() {
        orderDao.clearAllOrders()
    }
}
