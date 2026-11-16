package com.example.vk_education1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_education1.domain.model.AppInfo
import com.example.vk_education1.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps: StateFlow<List<AppInfo>> = _apps.asStateFlow()

    private val _ratings = MutableStateFlow<Map<String, Float>>(emptyMap())
    val ratings: StateFlow<Map<String, Float>> = _ratings.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // Добавляем состояние загрузки
    private val _isLoadingRatings = MutableStateFlow(false)
    val isLoadingRatings: StateFlow<Boolean> = _isLoadingRatings.asStateFlow()

    init {
        loadApps()
    }

    private fun loadApps() {
        viewModelScope.launch {
            try {
                _apps.value = repository.getApps()
            } catch (e: Exception) {
                _snackbarMessage.value = "Ошибка загрузки данных: ${e.message}"
            }
        }
    }

    fun loadRatingsSequentially() {
        if (_isLoadingRatings.value) return // Защита от повторного нажатия
        viewModelScope.launch {
            _isLoadingRatings.value = true
            val ids = _apps.value.map { it.id }
            if (ids.isEmpty()) {
                _isLoadingRatings.value = false
                return@launch
            }

            _snackbarMessage.value = "Начата последовательная загрузка..."

            try {
                val time = measureTimeMillis {
                    _ratings.value = repository.loadRatingsSequentially(ids)
                }
                _snackbarMessage.value = "Последовательно: $time мс"
            } catch (e: Exception) {
                _snackbarMessage.value = "Ошибка: ${e.message}"
            } finally {
                _isLoadingRatings.value = false
            }
        }
    }

    fun loadRatingsInParallel() {
        if (_isLoadingRatings.value) return
        viewModelScope.launch {
            _isLoadingRatings.value = true
            val ids = _apps.value.map { it.id }
            if (ids.isEmpty()) {
                _isLoadingRatings.value = false
                return@launch
            }

            _snackbarMessage.value = "Начата параллельная загрузка..."

            try {
                val time = measureTimeMillis {
                    _ratings.value = repository.loadRatingsInParallel(ids)
                }
                _snackbarMessage.value = "Параллельно: $time мс"
            } catch (e: Exception) {
                _snackbarMessage.value = "Ошибка: ${e.message}"
            } finally {
                _isLoadingRatings.value = false
            }
        }
    }

    fun onToolbarClick() {
        _snackbarMessage.value = "Вы нажали на тулбар!"
    }

    fun onSnackbarShown() {
        _snackbarMessage.value = null
    }
}