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

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {
    private val _app = MutableStateFlow<AppInfo?>(null)
    val app: StateFlow<AppInfo?> = _app.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadApp(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val apps = repository.getApps()
                _app.value = apps.find { it.id == id }
            } catch (e: Exception) {
                _app.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}