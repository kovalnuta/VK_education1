package com.example.vk_education1.domain.repository

import com.example.vk_education1.domain.model.AppInfo

interface AppRepository {
    fun getApps(): List<AppInfo>
}