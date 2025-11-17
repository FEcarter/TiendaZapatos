package com.example.tiendazapatos.data.model

import retrofit2.http.Url

data class UploadData (
    val url: String
)

data class UploadResponse(
    val status: String,
    val data: UploadData
)