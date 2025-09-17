package com.openlens.android.ui.screens.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlens.android.data.model.Service
import com.openlens.android.data.repository.KubernetesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val repository: KubernetesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServicesUiState())
    val uiState: StateFlow<ServicesUiState> = _uiState.asStateFlow()

    init {
        loadServices()
    }

    private fun loadServices() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val clusterId = "default" // This should come from app state
                val result = repository.getServices(clusterId)
                result.fold(
                    onSuccess = { services ->
                        _uiState.value = _uiState.value.copy(
                            services = services,
                            isLoading = false
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            error = error.message ?: "Failed to load services",
                            isLoading = false
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to load services",
                    isLoading = false
                )
            }
        }
    }

    fun refreshServices() {
        loadServices()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class ServicesUiState(
    val services: List<Service> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)