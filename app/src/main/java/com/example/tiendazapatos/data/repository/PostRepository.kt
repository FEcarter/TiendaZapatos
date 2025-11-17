package com.example.tiendazapatos.data.repository

import com.example.tiendazapatos.data.model.Post
import com.example.tiendazapatos.data.remote.RetrofitInstance

class PostRepository : PostRepositoryInterface{
    override suspend fun getPosts(): List<Post> {
        return RetrofitInstance.api.getPosts()
    }
}