package com.example.vk_education1.domain.usecase

import com.example.vk_education1.domain.repository.AppRepository
import javax.inject.Inject

class LoadRatingsUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend fun loadSequentially(ids: List<String>): Map<String, Float> {
        return repository.loadRatingsSequentially(ids)
    }

    suspend fun loadInParallel(ids: List<String>): Map<String, Float> {
        return repository.loadRatingsInParallel(ids)
    }
}