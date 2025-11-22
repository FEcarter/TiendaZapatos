package com.example.tiendazapatos.data.remote

import com.example.tiendazapatos.data.model.AuthRequest
import com.example.tiendazapatos.data.model.Product
import com.example.tiendazapatos.data.model.Post
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

object RetrofitInstance {

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val storeApi: StoreApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.7:8080/") // ¡Recuerda cambiar esta IP si es necesario!
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StoreApiService::class.java)
    }

    val apiFiles: UploadApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://lgk3m9-ip-152-230-114-2.tunnelmole.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UploadApi::class.java)
    }
}

interface StoreApiService {

    @GET("api/products")
    suspend fun getProducts(): List<Product>

    @POST("api/auth/register")
    suspend fun register(@Body authRequest: AuthRequest): Response<Unit>

    @POST("api/auth/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<Unit>
}

// La interfaz ApiService duplicada que causaba el error ha sido eliminada de este archivo.
