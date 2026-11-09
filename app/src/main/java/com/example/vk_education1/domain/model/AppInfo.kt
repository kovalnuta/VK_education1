package com.example.vk_education1.domain.model

data class AppInfo(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val iconUrl: String? = null
)