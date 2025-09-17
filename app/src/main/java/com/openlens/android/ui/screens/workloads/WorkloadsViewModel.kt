package com.openlens.android.ui.screens.workloads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlens.android.data.model.Pod
import com.openlens.android.data.repository.KubernetesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkloadsViewModel @Inject constructor(
    private val repository: KubernetesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkloadsUiState())
    val uiState: StateFlow<WorkloadsUiState> = _uiState.asStateFlow()

    init {
        loadPods()
    }

    private fun loadPods() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                // TODO: Get current cluster ID from preferences or state
                val clusterId = "default" // This should come from app state
                val result = repository.getPods(clusterId)
                result.fold(
                    onSuccess = { pods ->
                        _uiState.value = _uiState.value.copy(
                            pods = pods,
                            isLoading = false
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            error = error.message ?: "Failed to load pods",
                            isLoading = false
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to load pods",
                    isLoading = false
                )
            }
        }
    }

    fun refreshPods() {
        loadPods()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class WorkloadsUiState(
    val pods: List<Pod> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)