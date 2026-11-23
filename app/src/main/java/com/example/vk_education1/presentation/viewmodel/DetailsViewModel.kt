package com.example.vk_education1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_education1.domain.model.AppInfo
import com.example.vk_education1.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface InstallState {
    data object NotInstalled : InstallState
    data class Installing(val progress: Int) : InstallState
    data object Installed : InstallState
}

sealed interface AppDetailsState {
    data object Loading : AppDetailsState
    data class Content(
        val app: AppInfo,
        val installState: InstallState = InstallState.NotInstalled
    ) : AppDetailsState
    data object Error : AppDetailsState
}

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _state = MutableStateFlow<AppDetailsState>(AppDetailsState.Loading)
    val state: StateFlow<AppDetailsState> = _state.asStateFlow()

    private var installJob: Job? = null

    fun loadApp(id: String) {
        viewModelScope.launch {
            _state.value = AppDetailsState.Loading
            try {
                val apps = repository.getApps()
                val app = apps.find { it.id == id }
                if (app != null) {
                    _state.value = AppDetailsState.Content(app)
                } else {
                    _state.value = AppDetailsState.Error
                }
            } catch (e: Exception) {
                _state.value = AppDetailsState.Error
            }
        }
    }

    fun onInstallClick() {
        val current = _state.value
        if (current !is AppDetailsState.Content) return

        if (current.installState is InstallState.Installing) {
            // Отмена установки
            installJob?.cancel()
            installJob = null
        } else {
            // Запуск установки
            installJob = viewModelScope.launch {
                emulateInstall()
            }
        }
    }

    private suspend fun emulateInstall() {
        try {
            _state.update {
                if (it is AppDetailsState.Content) it.copy(installState = InstallState.Installing(0)) else it
            }
            for (i in 0..100 step 5) {
                delay(150)
                _state.update {
                    if (it is AppDetailsState.Content) it.copy(installState = InstallState.Installing(i)) else it
                }
            }
            _state.update {
                if (it is AppDetailsState.Content) it.copy(installState = InstallState.Installed) else it
            }
        } catch (e: CancellationException) {
            _state.update {
                if (it is AppDetailsState.Content) it.copy(installState = InstallState.NotInstalled) else it
            }
            throw e
        }
    }

    override fun onCleared() {
        super.onCleared()
        installJob?.cancel()
    }
}