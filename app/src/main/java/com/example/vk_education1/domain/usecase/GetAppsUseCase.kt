package com.example.vk_education1.domain.usecase

import com.example.vk_education1.domain.model.AppInfo
import com.example.vk_education1.domain.repository.AppRepository
import javax.inject.Inject

class GetAppsUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend operator fun invoke(): List<AppInfo> {
        return repository.getApps()
    }
}