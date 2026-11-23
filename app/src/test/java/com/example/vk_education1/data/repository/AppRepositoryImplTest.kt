package com.example.vk_education1.data.repository

import com.example.vk_education1.data.dto.AppDtoNetwork
import com.example.vk_education1.data.network.CatalogApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AppRepositoryImplTest {

    private lateinit var api: CatalogApi
    private lateinit var repository: AppRepositoryImpl

    @Before
    fun setUp() {
        api = mockk()
        repository = AppRepositoryImpl(api)
    }

    @Test
    fun `getApps returns mapped domain list`() = runTest {
        val dtoList = listOf(AppDtoNetwork("1", "App1", "D1", "C1", null))
        coEvery { api.getCatalog() } returns dtoList
        val result = repository.getApps()
        assertEquals(1, result.size)
        assertEquals("App1", result[0].name)
        coVerify(exactly = 1) { api.getCatalog() }
    }

    @Test
    fun `getApps propagates exception from api`() = runTest {
        coEvery { api.getCatalog() } throws RuntimeException("Network error")
        val exception = runCatching { repository.getApps() }.exceptionOrNull()
        assertTrue(exception is RuntimeException)
    }

    @Test
    fun `getRating returns value in expected range`() = runTest {
        val rating = repository.getRating("1")
        assertTrue(rating in 3.0f..5.0f)
    }

    @Test
    fun `loadRatingsSequentially returns map`() = runTest {
        val ids = listOf("1", "2")
        val ratings = repository.loadRatingsSequentially(ids)
        assertEquals(2, ratings.size)
    }

    @Test
    fun `loadRatingsSequentially empty list`() = runTest {
        assertTrue(repository.loadRatingsSequentially(emptyList()).isEmpty())
    }

    @Test
    fun `loadRatingsInParallel returns map`() = runTest {
        val ids = listOf("1", "2")
        val ratings = repository.loadRatingsInParallel(ids)
        assertEquals(2, ratings.size)
    }

    @Test
    fun `loadRatingsInParallel empty list`() = runTest {
        assertTrue(repository.loadRatingsInParallel(emptyList()).isEmpty())
    }
}