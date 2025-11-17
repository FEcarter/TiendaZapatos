package com.example.tiendazapatos

import com.example.tiendazapatos.data.model.Post
import com.example.tiendazapatos.data.repository.PostRepositoryInterface

class FakePostRepository : PostRepositoryInterface {
    override suspend fun getPosts(): List<Post> {
        return listOf(
            Post(1, 1, "Titulo 1", "Cuerpo 1"),
            Post(2, 2, "Titulo 2", "Cuerpo 2")
        )
    }
}