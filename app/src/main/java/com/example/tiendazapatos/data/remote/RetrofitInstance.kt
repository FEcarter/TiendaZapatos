package com.example.tiendazapatos.data.remote

import com.example.tiendazapatos.data.model.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object RetrofitInstance {

    // API externa (jsonplaceholder)
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // API para tu microservicio de Zapatos
    val storeApi: StoreApiService by lazy {
        Retrofit.Builder()
            // URL actualizada con tu IP real
            .baseUrl("http://192.168.1.7:8080/") 
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

// Interfaz para los endpoints de tu microservicio
interface StoreApiService {

    @GET("api/products")
    suspend fun getProducts(): List<Product>
}

// La ApiService duplicada ha sido eliminada de aquí.
