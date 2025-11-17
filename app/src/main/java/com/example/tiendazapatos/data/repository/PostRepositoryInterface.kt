package com.example.tiendazapatos.data.repository

import com.example.tiendazapatos.data.model.Post

interface PostRepositoryInterface {
    suspend fun getPosts(): List<Post>
}