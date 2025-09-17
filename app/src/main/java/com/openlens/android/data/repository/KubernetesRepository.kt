package com.openlens.android.data.repository

import com.openlens.android.data.model.*
import kotlinx.coroutines.flow.Flow

interface KubernetesRepository {
    // Cluster Management
    suspend fun addCluster(cluster: Cluster)
    suspend fun updateCluster(cluster: Cluster)
    suspend fun deleteCluster(clusterId: String)
    suspend fun getCluster(clusterId: String): Cluster?
    fun getAllClusters(): Flow<List<Cluster>>
    suspend fun connectToCluster(clusterId: String): Result<Unit>
    suspend fun disconnectFromCluster(clusterId: String)
    suspend fun testConnection(cluster: Cluster): Result<Boolean>
    
    // Pod Management
    suspend fun getPods(clusterId: String, namespace: String? = null): Result<List<Pod>>
    suspend fun getPod(clusterId: String, namespace: String, name: String): Result<Pod>
    suspend fun deletePod(clusterId: String, namespace: String, name: String): Result<Unit>
    suspend fun getPodLogs(clusterId: String, namespace: String, podName: String, containerName: String? = null): Result<List<LogEntry>>
    suspend fun streamPodLogs(clusterId: String, namespace: String, podName: String, containerName: String? = null): Flow<LogEntry>
    suspend fun execIntoPod(clusterId: String, namespace: String, podName: String, containerName: String, command: List<String>): Result<String>
    
    // Service Management
    suspend fun getServices(clusterId: String, namespace: String? = null): Result<List<Service>>
    suspend fun getService(clusterId: String, namespace: String, name: String): Result<Service>
    suspend fun deleteService(clusterId: String, namespace: String, name: String): Result<Unit>
    
    // Deployment Management
    suspend fun getDeployments(clusterId: String, namespace: String? = null): Result<List<Deployment>>
    suspend fun getDeployment(clusterId: String, namespace: String, name: String): Result<Deployment>
    suspend fun deleteDeployment(clusterId: String, namespace: String, name: String): Result<Unit>
    suspend fun scaleDeployment(clusterId: String, namespace: String, name: String, replicas: Int): Result<Unit>
    
    // ConfigMap Management
    suspend fun getConfigMaps(clusterId: String, namespace: String? = null): Result<List<ConfigMap>>
    suspend fun getConfigMap(clusterId: String, namespace: String, name: String): Result<ConfigMap>
    suspend fun createConfigMap(clusterId: String, configMap: ConfigMap): Result<Unit>
    suspend fun updateConfigMap(clusterId: String, configMap: ConfigMap): Result<Unit>
    suspend fun deleteConfigMap(clusterId: String, namespace: String, name: String): Result<Unit>
    
    // Secret Management
    suspend fun getSecrets(clusterId: String, namespace: String? = null): Result<List<Secret>>
    suspend fun getSecret(clusterId: String, namespace: String, name: String): Result<Secret>
    suspend fun createSecret(clusterId: String, secret: Secret): Result<Unit>
    suspend fun updateSecret(clusterId: String, secret: Secret): Result<Unit>
    suspend fun deleteSecret(clusterId: String, namespace: String, name: String): Result<Unit>
    
    // Events
    suspend fun getEvents(clusterId: String, namespace: String? = null): Result<List<Event>>
    
    // Namespaces
    suspend fun getNamespaces(clusterId: String): Result<List<String>>
    
    // Resource YAML
    suspend fun getResourceYaml(clusterId: String, kind: String, namespace: String?, name: String): Result<String>
    suspend fun updateResourceYaml(clusterId: String, yaml: String): Result<Unit>
    suspend fun createResourceYaml(clusterId: String, yaml: String): Result<Unit>
}