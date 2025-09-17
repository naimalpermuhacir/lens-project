package com.openlens.android.ui.screens.clusters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlens.android.data.model.Cluster
import com.openlens.android.data.repository.KubernetesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClustersViewModel @Inject constructor(
    private val repository: KubernetesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClustersUiState())
    val uiState: StateFlow<ClustersUiState> = _uiState.asStateFlow()

    init {
        loadClusters()
    }

    private fun loadClusters() {
        viewModelScope.launch {
            repository.getAllClusters().collect { clusters ->
                _uiState.value = _uiState.value.copy(
                    clusters = clusters,
                    isLoading = false
                )
            }
        }
    }

    fun addCluster(name: String, url: String, kubeconfig: String) {
        viewModelScope.launch {
            try {
                val cluster = Cluster(
                    id = java.util.UUID.randomUUID().toString(),
                    name = name,
                    url = url,
                    kubeconfig = kubeconfig
                )
                repository.addCluster(cluster)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to add cluster"
                )
            }
        }
    }

    fun connectToCluster(clusterId: String) {
        viewModelScope.launch {
            try {
                repository.connectToCluster(clusterId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to connect to cluster"
                )
            }
        }
    }

    fun disconnectFromCluster(clusterId: String) {
        viewModelScope.launch {
            try {
                repository.disconnectFromCluster(clusterId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to disconnect from cluster"
                )
            }
        }
    }

    fun deleteCluster(clusterId: String) {
        viewModelScope.launch {
            try {
                repository.deleteCluster(clusterId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to delete cluster"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class ClustersUiState(
    val clusters: List<Cluster> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)