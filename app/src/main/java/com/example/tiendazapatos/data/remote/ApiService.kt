package com.example.tiendazapatos.data.remote

import com.example.tiendazapatos.data.model.Post
import retrofit2.http.GET

interface ApiService {

    @GET("/posts")
    suspend fun getPosts(): List<Post>

}