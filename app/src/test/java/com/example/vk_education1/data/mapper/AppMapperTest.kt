package com.example.vk_education1.data.mapper

import com.example.vk_education1.data.dto.AppDtoNetwork
import com.example.vk_education1.domain.model.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AppMapperTest {

    @Test
    fun `toDomain maps all fields correctly`() {
        val dto = AppDtoNetwork("1", "Test App", "Desc", "Games", "url")
        val domain: AppInfo = dto.toDomain()
        assertEquals("1", domain.id)
        assertEquals("Test App", domain.name)
        assertEquals("Desc", domain.description)
        assertEquals("Games", domain.category)
        assertEquals("url", domain.iconUrl)
    }

    @Test
    fun `toDomain handles null iconUrl`() {
        val dto = AppDtoNetwork("2", "No Icon", "D", "Tools", null)
        assertNull(dto.toDomain().iconUrl)
    }

    @Test
    fun `toDomainList returns empty list for empty input`() {
        assertTrue(emptyList<AppDtoNetwork>().toDomainList().isEmpty())
    }

    @Test
    fun `toDomainList maps each element correctly`() {
        val dtos = listOf(AppDtoNetwork("1", "A", "D1", "C1", null))
        val domains = dtos.toDomainList()
        assertEquals(1, domains.size)
        assertEquals("A", domains[0].name)
    }

    @Test
    fun `toDomainList preserves order of elements`() {
        val dtos = (1..3).map { AppDtoNetwork("$it", "App$it", "D", "C", null) }
        val domains = dtos.toDomainList()
        assertEquals(listOf("1", "2", "3"), domains.map { it.id })
    }
}