package com.example.vk_education1

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {


    private val _apps = MutableStateFlow(mockApps)
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