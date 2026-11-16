package com.example.vk_education1.domain.repository

import com.example.vk_education1.domain.model.AppInfo

interface AppRepository {
    suspend fun getApps(): List<AppInfo>
    suspend fun getRating(id: String): Float
    suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float>
    suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float>
}