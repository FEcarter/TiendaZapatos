package com.example.tiendazapatos.data.repository

import com.example.tiendazapatos.data.remote.dao.UserDao
import com.example.tiendazapatos.data.remote.model.User

class UserRepository(private val userDao: UserDao) {

    suspend fun getUserByName(name: String): User? {
        return userDao.getUserByName(name)
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
}