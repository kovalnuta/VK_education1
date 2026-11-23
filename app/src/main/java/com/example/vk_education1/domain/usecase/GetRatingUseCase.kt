package com.example.vk_education1.domain.usecase

import com.example.vk_education1.domain.repository.AppRepository
import javax.inject.Inject

class GetRatingUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend operator fun invoke(id: String): Float {
        return repository.getRating(id)
    }
}