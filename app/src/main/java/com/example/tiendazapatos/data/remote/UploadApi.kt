package com.example.tiendazapatos.data.remote

import com.example.tiendazapatos.data.model.UploadResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Response

interface UploadApi {
    @Multipart
    @POST("api/upload")

    suspend fun uploadFile(
        @Part file: MultipartBody.Part
    ): Response<UploadResponse>
}