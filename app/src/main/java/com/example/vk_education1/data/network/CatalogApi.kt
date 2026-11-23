package com.example.vk_education1.data.network

import com.example.vk_education1.data.dto.AppDtoNetwork
import retrofit2.http.GET
import retrofit2.http.Path

interface CatalogApi {
    @GET("catalog")
    suspend fun getCatalog(): List<AppDtoNetwork>
    @GET("catalog/{id}")
    suspend fun getAppDetails(@Path("id") id: String): AppDtoNetwork
}