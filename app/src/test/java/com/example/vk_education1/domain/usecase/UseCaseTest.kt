package com.example.vk_education1.domain.usecase

import com.example.vk_education1.domain.model.AppInfo
import com.example.vk_education1.domain.repository.AppRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UseCaseTest {

    private lateinit var repository: AppRepository
    private lateinit var getAppsUseCase: GetAppsUseCase
    private lateinit var getRatingUseCase: GetRatingUseCase
    private lateinit var loadRatingsUseCase: LoadRatingsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getAppsUseCase = GetAppsUseCase(repository)
        getRatingUseCase = GetRatingUseCase(repository)
        loadRatingsUseCase = LoadRatingsUseCase(repository)
    }

    @Test
    fun `getAppsUseCase returns apps from repository`() = runTest {
        val apps = listOf(AppInfo("1", "App1", "D1", "C1"))
        coEvery { repository.getApps() } returns apps
        val result = getAppsUseCase()
        assertEquals(apps, result)
    }

    @Test
    fun `getAppsUseCase propagates exception`() = runTest {
        coEvery { repository.getApps() } throws RuntimeException("DB error")
        val exception = runCatching { getAppsUseCase() }.exceptionOrNull()
        assertTrue(exception is RuntimeException)
    }

    @Test
    fun `getRatingUseCase returns rating`() = runTest {
        coEvery { repository.getRating("42") } returns 4.5f
        val result = getRatingUseCase("42")
        assertEquals(4.5f, result, 0.001f)
    }

    @Test
    fun `getRatingUseCase passes correct id`() = runTest {
        coEvery { repository.getRating("abc") } returns 3.2f
        getRatingUseCase("abc")
        coVerify(exactly = 1) { repository.getRating("abc") }
    }

    @Test
    fun `loadSequentially delegates to repository`() = runTest {
        val ids = listOf("1")
        val expected = mapOf("1" to 4.5f)
        coEvery { repository.loadRatingsSequentially(ids) } returns expected
        val result = loadRatingsUseCase.loadSequentially(ids)
        assertEquals(expected, result)
    }

    @Test
    fun `loadInParallel delegates to repository`() = runTest {
        val ids = listOf("1")
        val expected = mapOf("1" to 4.5f)
        coEvery { repository.loadRatingsInParallel(ids) } returns expected
        val result = loadRatingsUseCase.loadInParallel(ids)
        assertEquals(expected, result)
    }
}