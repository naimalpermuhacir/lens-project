package com.openlens.android.data.repository

import com.openlens.android.data.local.dao.ClusterDao
import com.openlens.android.data.model.*
import com.openlens.android.data.remote.KubernetesApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KubernetesRepositoryImpl @Inject constructor(
    private val clusterDao: ClusterDao,
    private val kubernetesApiService: KubernetesApiService
) : KubernetesRepository {

    override suspend fun addCluster(cluster: Cluster) {
        clusterDao.insertCluster(cluster)
    }

    override suspend fun updateCluster(cluster: Cluster) {
        clusterDao.updateCluster(cluster)
    }

    override suspend fun deleteCluster(clusterId: String) {
        clusterDao.deleteCluster(clusterId)
    }

    override suspend fun getCluster(clusterId: String): Cluster? {
        return clusterDao.getCluster(clusterId)
    }

    override fun getAllClusters(): Flow<List<Cluster>> {
        return clusterDao.getAllClusters()
    }

    override suspend fun connectToCluster(clusterId: String): Result<Unit> {
        return try {
            val cluster = clusterDao.getCluster(clusterId) ?: return Result.failure(Exception("Cluster not found"))
            kubernetesApiService.connectToCluster(cluster)
            val updatedCluster = cluster.copy(isConnected = true, lastConnected = System.currentTimeMillis())
            clusterDao.updateCluster(updatedCluster)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun disconnectFromCluster(clusterId: String) {
        kubernetesApiService.disconnectFromCluster(clusterId)
        val cluster = clusterDao.getCluster(clusterId)
        cluster?.let {
            val updatedCluster = it.copy(isConnected = false)
            clusterDao.updateCluster(updatedCluster)
        }
    }

    override suspend fun testConnection(cluster: Cluster): Result<Boolean> {
        return try {
            kubernetesApiService.testConnection(cluster)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPods(clusterId: String, namespace: String?): Result<List<Pod>> {
        return try {
            val pods = kubernetesApiService.getPods(clusterId, namespace)
            Result.success(pods)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPod(clusterId: String, namespace: String, name: String): Result<Pod> {
        return try {
            val pod = kubernetesApiService.getPod(clusterId, namespace, name)
            Result.success(pod)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePod(clusterId: String, namespace: String, name: String): Result<Unit> {
        return try {
            kubernetesApiService.deletePod(clusterId, namespace, name)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPodLogs(clusterId: String, namespace: String, podName: String, containerName: String?): Result<List<LogEntry>> {
        return try {
            val logs = kubernetesApiService.getPodLogs(clusterId, namespace, podName, containerName)
            Result.success(logs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun streamPodLogs(clusterId: String, namespace: String, podName: String, containerName: String?): Flow<LogEntry> {
        return flow {
            kubernetesApiService.streamPodLogs(clusterId, namespace, podName, containerName).collect { logEntry ->
                emit(logEntry)
            }
        }
    }

    override suspend fun execIntoPod(clusterId: String, namespace: String, podName: String, containerName: String, command: List<String>): Result<String> {
        return try {
            val result = kubernetesApiService.execIntoPod(clusterId, namespace, podName, containerName, command)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getServices(clusterId: String, namespace: String?): Result<List<Service>> {
        return try {
            val services = kubernetesApiService.getServices(clusterId, namespace)
            Result.success(services)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getService(clusterId: String, namespace: String, name: String): Result<Service> {
        return try {
            val service = kubernetesApiService.getService(clusterId, namespace, name)
            Result.success(service)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteService(clusterId: String, namespace: String, name: String): Result<Unit> {
        return try {
            kubernetesApiService.deleteService(clusterId, namespace, name)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDeployments(clusterId: String, namespace: String?): Result<List<Deployment>> {
        return try {
            val deployments = kubernetesApiService.getDeployments(clusterId, namespace)
            Result.success(deployments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDeployment(clusterId: String, namespace: String, name: String): Result<Deployment> {
        return try {
            val deployment = kubernetesApiService.getDeployment(clusterId, namespace, name)
            Result.success(deployment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteDeployment(clusterId: String, namespace: String, name: String): Result<Unit> {
        return try {
            kubernetesApiService.deleteDeployment(clusterId, namespace, name)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun scaleDeployment(clusterId: String, namespace: String, name: String, replicas: Int): Result<Unit> {
        return try {
            kubernetesApiService.scaleDeployment(clusterId, namespace, name, replicas)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getConfigMaps(clusterId: String, namespace: String?): Result<List<ConfigMap>> {
        return try {
            val configMaps = kubernetesApiService.getConfigMaps(clusterId, namespace)
            Result.success(configMaps)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getConfigMap(clusterId: String, namespace: String, name: String): Result<ConfigMap> {
        return try {
            val configMap = kubernetesApiService.getConfigMap(clusterId, namespace, name)
            Result.success(configMap)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createConfigMap(clusterId: String, configMap: ConfigMap): Result<Unit> {
        return try {
            kubernetesApiService.createConfigMap(clusterId, configMap)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateConfigMap(clusterId: String, configMap: ConfigMap): Result<Unit> {
        return try {
            kubernetesApiService.updateConfigMap(clusterId, configMap)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteConfigMap(clusterId: String, namespace: String, name: String): Result<Unit> {
        return try {
            kubernetesApiService.deleteConfigMap(clusterId, namespace, name)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSecrets(clusterId: String, namespace: String?): Result<List<Secret>> {
        return try {
            val secrets = kubernetesApiService.getSecrets(clusterId, namespace)
            Result.success(secrets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSecret(clusterId: String, namespace: String, name: String): Result<Secret> {
        return try {
            val secret = kubernetesApiService.getSecret(clusterId, namespace, name)
            Result.success(secret)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createSecret(clusterId: String, secret: Secret): Result<Unit> {
        return try {
            kubernetesApiService.createSecret(clusterId, secret)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateSecret(clusterId: String, secret: Secret): Result<Unit> {
        return try {
            kubernetesApiService.updateSecret(clusterId, secret)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteSecret(clusterId: String, namespace: String, name: String): Result<Unit> {
        return try {
            kubernetesApiService.deleteSecret(clusterId, namespace, name)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEvents(clusterId: String, namespace: String?): Result<List<Event>> {
        return try {
            val events = kubernetesApiService.getEvents(clusterId, namespace)
            Result.success(events)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNamespaces(clusterId: String): Result<List<String>> {
        return try {
            val namespaces = kubernetesApiService.getNamespaces(clusterId)
            Result.success(namespaces)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getResourceYaml(clusterId: String, kind: String, namespace: String?, name: String): Result<String> {
        return try {
            val yaml = kubernetesApiService.getResourceYaml(clusterId, kind, namespace, name)
            Result.success(yaml)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateResourceYaml(clusterId: String, yaml: String): Result<Unit> {
        return try {
            kubernetesApiService.updateResourceYaml(clusterId, yaml)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createResourceYaml(clusterId: String, yaml: String): Result<Unit> {
        return try {
            kubernetesApiService.createResourceYaml(clusterId, yaml)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}