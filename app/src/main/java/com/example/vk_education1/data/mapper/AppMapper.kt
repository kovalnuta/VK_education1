package com.example.vk_education1.data.mapper

import com.example.vk_education1.data.dto.AppInfoDto
import com.example.vk_education1.domain.model.AppInfo

fun AppInfoDto.toDomain(): AppInfo {
    return AppInfo(name, description, category)
}

fun List<AppInfoDto>.toDomainList(): List<AppInfo> {
    return this.map { it.toDomain() }
}