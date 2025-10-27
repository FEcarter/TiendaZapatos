package com.example.tiendazapatos.data.remote.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tiendazapatos.data.remote.model.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<Order>>
    
    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    suspend fun getOrderById(id: Int): Order?

    @Query("DELETE FROM orders")
    suspend fun clearAllOrders()

    @androidx.room.Delete
    suspend fun deleteOrder(order: Order)
}
