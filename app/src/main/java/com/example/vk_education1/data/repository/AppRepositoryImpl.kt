package com.example.vk_education1.data.repository

import com.example.vk_education1.data.mapper.toDomainList
import com.example.vk_education1.data.network.CatalogApi
import com.example.vk_education1.domain.model.AppInfo
import com.example.vk_education1.domain.repository.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val api: CatalogApi
) : AppRepository {
    override suspend fun getApps(): List<AppInfo> {

        return api.getCatalog().toDomainList()
    }
}