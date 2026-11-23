package com.example.vk_education1.data.mapper

import com.example.vk_education1.data.dto.AppDtoNetwork
import com.example.vk_education1.domain.model.AppInfo

fun AppDtoNetwork.toDomain(): AppInfo {
    return AppInfo(
        id = id,
        name = name,
        description = description,
        category = category,
        iconUrl = iconUrl
    )
}

fun List<AppDtoNetwork>.toDomainList(): List<AppInfo> {
    return this.map { it.toDomain() }
}