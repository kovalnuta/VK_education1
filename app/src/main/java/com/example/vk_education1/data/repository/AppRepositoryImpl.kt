package com.example.vk_education1.data.repository

import com.example.vk_education1.data.mapper.toDomainList
import com.example.vk_education1.data.network.CatalogApi
import com.example.vk_education1.domain.model.AppInfo
import com.example.vk_education1.domain.repository.AppRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val api: CatalogApi
) : AppRepository {
    override suspend fun getApps(): List<AppInfo> {
        return api.getCatalog().toDomainList()
    }

    override suspend fun getRating(id: String): Float {
        delay(1500) // Эмуляция сетевого запроса
        return (30..50).random() / 10f
    }

    override suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float> {
        val result = mutableMapOf<String, Float>()
        for (id in ids) {
            result[id] = getRating(id)
        }
        return result
    }

    override suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float> = coroutineScope {
        ids.map { id ->
            async { id to getRating(id) }
        }.awaitAll().toMap()
    }
}