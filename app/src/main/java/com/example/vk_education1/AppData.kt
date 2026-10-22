
package com.example.vk_education1

data class AppInfo(
    val name: String,
    val description: String,
    val category: String
)

val mockApps = listOf(
    AppInfo("VK", "Общение и мессенджер", "Социальные сети"),
    AppInfo("Яндекс Карты", "Навигация и пробки", "Путешествия"),
    AppInfo("Spotify", "Музыка и подкасты", "Музыка"),
    AppInfo("Telegram", "Быстрый и безопасный мессенджер", "Общение"),
    AppInfo("Netflix", "Фильмы и сериалы", "Развлечения"),
    AppInfo("Duolingo", "Изучение иностранных языков", "Образование"),
    AppInfo("Tinkoff", "Банк и финансовые услуги", "Финансы"),
    AppInfo("YouTube", "Видео и трансляции", "Развлечения"),
    AppInfo("Discord", "Общение и голосовые чаты", "Общение")
)
