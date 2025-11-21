package com.example.tiendazapatos.data.repository

import com.example.tiendazapatos.data.remote.dao.UserDao
import com.example.tiendazapatos.data.remote.model.User

// Ahora el repositorio implementa la interfaz
class UserRepository(private val userDao: UserDao) : UserRepositoryInterface {

    override suspend fun getUserByName(name: String): User? {
        return userDao.getUserByName(name)
    }

    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
}
