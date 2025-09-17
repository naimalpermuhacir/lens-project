package com.openlens.android.data.remote

import com.openlens.android.data.model.*
import kotlinx.coroutines.flow.Flow

interface KubernetesApiService {
    suspend fun connectToCluster(cluster: Cluster)
    suspend fun disconnectFromCluster(clusterId: String)
    suspend fun testConnection(cluster: Cluster): Boolean
    
    // Pod operations
    suspend fun getPods(clusterId: String, namespace: String? = null): List<Pod>
    suspend fun getPod(clusterId: String, namespace: String, name: String): Pod
    suspend fun deletePod(clusterId: String, namespace: String, name: String)
    suspend fun getPodLogs(clusterId: String, namespace: String, podName: String, containerName: String? = null): List<LogEntry>
    suspend fun streamPodLogs(clusterId: String, namespace: String, podName: String, containerName: String? = null): Flow<LogEntry>
    suspend fun execIntoPod(clusterId: String, namespace: String, podName: String, containerName: String, command: List<String>): String
    
    // Service operations
    suspend fun getServices(clusterId: String, namespace: String? = null): List<Service>
    suspend fun getService(clusterId: String, namespace: String, name: String): Service
    suspend fun deleteService(clusterId: String, namespace: String, name: String)
    
    // Deployment operations
    suspend fun getDeployments(clusterId: String, namespace: String? = null): List<Deployment>
    suspend fun getDeployment(clusterId: String, namespace: String, name: String): Deployment
    suspend fun deleteDeployment(clusterId: String, namespace: String, name: String)
    suspend fun scaleDeployment(clusterId: String, namespace: String, name: String, replicas: Int)
    
    // ConfigMap operations
    suspend fun getConfigMaps(clusterId: String, namespace: String? = null): List<ConfigMap>
    suspend fun getConfigMap(clusterId: String, namespace: String, name: String): ConfigMap
    suspend fun createConfigMap(clusterId: String, configMap: ConfigMap)
    suspend fun updateConfigMap(clusterId: String, configMap: ConfigMap)
    suspend fun deleteConfigMap(clusterId: String, namespace: String, name: String)
    
    // Secret operations
    suspend fun getSecrets(clusterId: String, namespace: String? = null): List<Secret>
    suspend fun getSecret(clusterId: String, namespace: String, name: String): Secret
    suspend fun createSecret(clusterId: String, secret: Secret)
    suspend fun updateSecret(clusterId: String, secret: Secret)
    suspend fun deleteSecret(clusterId: String, namespace: String, name: String)
    
    // Event operations
    suspend fun getEvents(clusterId: String, namespace: String? = null): List<Event>
    
    // Namespace operations
    suspend fun getNamespaces(clusterId: String): List<String>
    
    // YAML operations
    suspend fun getResourceYaml(clusterId: String, kind: String, namespace: String?, name: String): String
    suspend fun updateResourceYaml(clusterId: String, yaml: String)
    suspend fun createResourceYaml(clusterId: String, yaml: String)
}