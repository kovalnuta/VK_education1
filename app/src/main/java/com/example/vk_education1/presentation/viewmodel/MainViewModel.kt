package com.example.vk_education1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.vk_education1.data.repository.AppRepositoryImpl
import com.example.vk_education1.domain.model.AppInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val repository = AppRepositoryImpl()

    private val _apps = MutableStateFlow(repository.getApps())
    val apps: StateFlow<List<AppInfo>> = _apps.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    fun onToolbarClick() {
        _snackbarMessage.value = "Вы нажали на тулбар!"
    }

    fun onSnackbarShown() {
        _snackbarMessage.value = null
    }
}