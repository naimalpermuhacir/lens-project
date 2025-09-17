package com.openlens.android.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun setTheme(theme: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(theme = theme)
            // TODO: Save theme preference
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
            // TODO: Save notification preference
        }
    }
}

data class SettingsUiState(
    val theme: String = "system",
    val notificationsEnabled: Boolean = true
)