package com.example.vk_education1.data.repository

import com.example.vk_education1.data.dto.AppInfoDto
import com.example.vk_education1.data.mapper.toDomainList
import com.example.vk_education1.domain.model.AppInfo
import com.example.vk_education1.domain.repository.AppRepository

class AppRepositoryImpl : AppRepository {
    private val mockData = listOf(
        AppInfoDto("VK", "Общение и мессенджер", "Социальные сети"),
        AppInfoDto("Яндекс Карты", "Навигация и пробки", "Путешествия"),
        AppInfoDto("Spotify", "Музыка и подкасты", "Музыка"),
        AppInfoDto("Telegram", "Быстрый и безопасный мессенджер", "Общение"),
        AppInfoDto("Netflix", "Фильмы и сериалы", "Развлечения"),
        AppInfoDto("Duolingo", "Изучение иностранных языков", "Образование"),
        AppInfoDto("Tinkoff", "Банк и финансовые услуги", "Финансы"),
        AppInfoDto("YouTube", "Видео и трансляции", "Развлечения"),
        AppInfoDto("Discord", "Общение и голосовые чаты", "Общение")
    )

    override fun getApps(): List<AppInfo> {
        return mockData.toDomainList()
    }
}