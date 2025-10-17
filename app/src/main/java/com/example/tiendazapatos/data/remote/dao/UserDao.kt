package com.example.tiendazapatos.data.remote.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tiendazapatos.data.remote.model.User
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsers(): Flow<List<User>>

    @Delete
    suspend fun deleteUser(user: User)

}
