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
class MainViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps: StateFlow<List<AppInfo>> = _apps.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

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

    fun onToolbarClick() {
        _snackbarMessage.value = "Вы нажали на тулбар!"
    }

    fun onSnackbarShown() {
        _snackbarMessage.value = null
    }
}