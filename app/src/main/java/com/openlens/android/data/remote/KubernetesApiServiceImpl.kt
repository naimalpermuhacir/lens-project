package com.openlens.android.data.remote

import com.openlens.android.data.model.*
import io.kubernetes.client.openapi.ApiClient
import io.kubernetes.client.openapi.ApiException
import io.kubernetes.client.openapi.Configuration
import io.kubernetes.client.openapi.apis.*
import io.kubernetes.client.openapi.models.*
import io.kubernetes.client.util.Config
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.ByteArrayInputStream
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KubernetesApiServiceImpl @Inject constructor() : KubernetesApiService {

    private val apiClients = mutableMapOf<String, ApiClient>()
    private val coreV1Apis = mutableMapOf<String, CoreV1Api>()
    private val appsV1Apis = mutableMapOf<String, AppsV1Api>()

    override suspend fun connectToCluster(cluster: Cluster) {
        try {
            val kubeconfigBytes = cluster.kubeconfig.toByteArray()
            val kubeconfigStream = ByteArrayInputStream(kubeconfigBytes)
            val apiClient = Config.fromConfig(kubeconfigStream)
            apiClient.basePath = cluster.url
            Configuration.setDefaultApiClient(apiClient)
            
            apiClients[cluster.id] = apiClient
            coreV1Apis[cluster.id] = CoreV1Api(apiClient)
            appsV1Apis[cluster.id] = AppsV1Api(apiClient)
        } catch (e: Exception) {
            throw Exception("Failed to connect to cluster: ${e.message}")
        }
    }

    override suspend fun disconnectFromCluster(clusterId: String) {
        apiClients.remove(clusterId)
        coreV1Apis.remove(clusterId)
        appsV1Apis.remove(clusterId)
    }

    override suspend fun testConnection(cluster: Cluster): Boolean {
        return try {
            connectToCluster(cluster)
            val coreV1Api = coreV1Apis[cluster.id] ?: throw Exception("API not initialized")
            coreV1Api.listNamespace()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getPods(clusterId: String, namespace: String?): List<Pod> {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        return try {
            val podList = if (namespace != null) {
                coreV1Api.listNamespacedPod(namespace)
            } else {
                coreV1Api.listPodForAllNamespaces()
            }
            
            podList.items?.map { pod ->
                Pod(
                    name = pod.metadata?.name ?: "",
                    namespace = pod.metadata?.namespace ?: "",
                    status = pod.status?.phase ?: "Unknown",
                    ready = "${pod.status?.containerStatuses?.count { it.ready } ?: 0}/${pod.status?.containerStatuses?.size ?: 0}",
                    restarts = pod.status?.containerStatuses?.sumOf { it.restartCount?.toInt() ?: 0 } ?: 0,
                    age = calculateAge(pod.metadata?.creationTimestamp),
                    node = pod.spec?.nodeName ?: "",
                    ip = pod.status?.podIP ?: "",
                    containers = pod.status?.containerStatuses?.map { container ->
                        Container(
                            name = container.name ?: "",
                            image = container.image ?: "",
                            ready = container.ready ?: false,
                            restartCount = container.restartCount?.toInt() ?: 0,
                            state = when {
                                container.state?.running != null -> "Running"
                                container.state?.waiting != null -> "Waiting"
                                container.state?.terminated != null -> "Terminated"
                                else -> "Unknown"
                            }
                        )
                    } ?: emptyList(),
                    labels = pod.metadata?.labels ?: emptyMap(),
                    annotations = pod.metadata?.annotations ?: emptyMap(),
                    creationTimestamp = pod.metadata?.creationTimestamp?.time ?: 0L,
                    uid = pod.metadata?.uid ?: ""
                )
            } ?: emptyList()
        } catch (e: ApiException) {
            throw Exception("Failed to get pods: ${e.message}")
        }
    }

    override suspend fun getPod(clusterId: String, namespace: String, name: String): Pod {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        return try {
            val pod = coreV1Api.readNamespacedPod(name, namespace)
            Pod(
                name = pod.metadata?.name ?: "",
                namespace = pod.metadata?.namespace ?: "",
                status = pod.status?.phase ?: "Unknown",
                ready = "${pod.status?.containerStatuses?.count { it.ready } ?: 0}/${pod.status?.containerStatuses?.size ?: 0}",
                restarts = pod.status?.containerStatuses?.sumOf { it.restartCount?.toInt() ?: 0 } ?: 0,
                age = calculateAge(pod.metadata?.creationTimestamp),
                node = pod.spec?.nodeName ?: "",
                ip = pod.status?.podIP ?: "",
                containers = pod.status?.containerStatuses?.map { container ->
                    Container(
                        name = container.name ?: "",
                        image = container.image ?: "",
                        ready = container.ready ?: false,
                        restartCount = container.restartCount?.toInt() ?: 0,
                        state = when {
                            container.state?.running != null -> "Running"
                            container.state?.waiting != null -> "Waiting"
                            container.state?.terminated != null -> "Terminated"
                            else -> "Unknown"
                        }
                    )
                } ?: emptyList(),
                labels = pod.metadata?.labels ?: emptyMap(),
                annotations = pod.metadata?.annotations ?: emptyMap(),
                creationTimestamp = pod.metadata?.creationTimestamp?.time ?: 0L,
                uid = pod.metadata?.uid ?: ""
            )
        } catch (e: ApiException) {
            throw Exception("Failed to get pod: ${e.message}")
        }
    }

    override suspend fun deletePod(clusterId: String, namespace: String, name: String) {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        try {
            coreV1Api.deleteNamespacedPod(name, namespace)
        } catch (e: ApiException) {
            throw Exception("Failed to delete pod: ${e.message}")
        }
    }

    override suspend fun getPodLogs(clusterId: String, namespace: String, podName: String, containerName: String?): List<LogEntry> {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        return try {
            val logs = coreV1Api.readNamespacedPodLog(podName, namespace, containerName, null, null, null, null, null, null, null)
            logs.split("\n").map { line ->
                LogEntry(
                    timestamp = "",
                    level = "INFO",
                    message = line,
                    container = containerName ?: ""
                )
            }
        } catch (e: ApiException) {
            throw Exception("Failed to get pod logs: ${e.message}")
        }
    }

    override suspend fun streamPodLogs(clusterId: String, namespace: String, podName: String, containerName: String?): Flow<LogEntry> {
        return flow {
            // Implementation for streaming logs would go here
            // For now, we'll just emit the logs as a single batch
            val logs = getPodLogs(clusterId, namespace, podName, containerName)
            logs.forEach { emit(it) }
        }
    }

    override suspend fun execIntoPod(clusterId: String, namespace: String, podName: String, containerName: String, command: List<String>): String {
        // Terminal execution implementation would go here
        // This is a complex feature that requires WebSocket or similar streaming
        return "Terminal execution not implemented yet"
    }

    override suspend fun getServices(clusterId: String, namespace: String?): List<Service> {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        return try {
            val serviceList = if (namespace != null) {
                coreV1Api.listNamespacedService(namespace)
            } else {
                coreV1Api.listServiceForAllNamespaces()
            }
            
            serviceList.items?.map { service ->
                Service(
                    name = service.metadata?.name ?: "",
                    namespace = service.metadata?.namespace ?: "",
                    type = service.spec?.type ?: "ClusterIP",
                    clusterIP = service.spec?.clusterIP ?: "",
                    externalIP = service.status?.loadBalancer?.ingress?.firstOrNull()?.ip ?: "",
                    ports = service.spec?.ports?.map { port ->
                        ServicePort(
                            name = port.name,
                            port = port.port ?: 0,
                            targetPort = port.targetPort?.stringValue ?: port.targetPort?.intValue?.toString() ?: "0",
                            protocol = port.protocol ?: "TCP"
                        )
                    } ?: emptyList(),
                    age = calculateAge(service.metadata?.creationTimestamp),
                    labels = service.metadata?.labels ?: emptyMap(),
                    annotations = service.metadata?.annotations ?: emptyMap(),
                    creationTimestamp = service.metadata?.creationTimestamp?.time ?: 0L,
                    uid = service.metadata?.uid ?: ""
                )
            } ?: emptyList()
        } catch (e: ApiException) {
            throw Exception("Failed to get services: ${e.message}")
        }
    }

    override suspend fun getService(clusterId: String, namespace: String, name: String): Service {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        return try {
            val service = coreV1Api.readNamespacedService(name, namespace)
            Service(
                name = service.metadata?.name ?: "",
                namespace = service.metadata?.namespace ?: "",
                type = service.spec?.type ?: "ClusterIP",
                clusterIP = service.spec?.clusterIP ?: "",
                externalIP = service.status?.loadBalancer?.ingress?.firstOrNull()?.ip ?: "",
                ports = service.spec?.ports?.map { port ->
                    ServicePort(
                        name = port.name,
                        port = port.port ?: 0,
                        targetPort = port.targetPort?.stringValue ?: port.targetPort?.intValue?.toString() ?: "0",
                        protocol = port.protocol ?: "TCP"
                    )
                } ?: emptyList(),
                age = calculateAge(service.metadata?.creationTimestamp),
                labels = service.metadata?.labels ?: emptyMap(),
                annotations = service.metadata?.annotations ?: emptyMap(),
                creationTimestamp = service.metadata?.creationTimestamp?.time ?: 0L,
                uid = service.metadata?.uid ?: ""
            )
        } catch (e: ApiException) {
            throw Exception("Failed to get service: ${e.message}")
        }
    }

    override suspend fun deleteService(clusterId: String, namespace: String, name: String) {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        try {
            coreV1Api.deleteNamespacedService(name, namespace)
        } catch (e: ApiException) {
            throw Exception("Failed to delete service: ${e.message}")
        }
    }

    override suspend fun getDeployments(clusterId: String, namespace: String?): List<Deployment> {
        val appsV1Api = appsV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        return try {
            val deploymentList = if (namespace != null) {
                appsV1Api.listNamespacedDeployment(namespace)
            } else {
                appsV1Api.listDeploymentForAllNamespaces()
            }
            
            deploymentList.items?.map { deployment ->
                Deployment(
                    name = deployment.metadata?.name ?: "",
                    namespace = deployment.metadata?.namespace ?: "",
                    replicas = deployment.spec?.replicas ?: 0,
                    readyReplicas = deployment.status?.readyReplicas ?: 0,
                    availableReplicas = deployment.status?.availableReplicas ?: 0,
                    age = calculateAge(deployment.metadata?.creationTimestamp),
                    labels = deployment.metadata?.labels ?: emptyMap(),
                    annotations = deployment.metadata?.annotations ?: emptyMap(),
                    creationTimestamp = deployment.metadata?.creationTimestamp?.time ?: 0L,
                    uid = deployment.metadata?.uid ?: ""
                )
            } ?: emptyList()
        } catch (e: ApiException) {
            throw Exception("Failed to get deployments: ${e.message}")
        }
    }

    override suspend fun getDeployment(clusterId: String, namespace: String, name: String): Deployment {
        val appsV1Api = appsV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        return try {
            val deployment = appsV1Api.readNamespacedDeployment(name, namespace)
            Deployment(
                name = deployment.metadata?.name ?: "",
                namespace = deployment.metadata?.namespace ?: "",
                replicas = deployment.spec?.replicas ?: 0,
                readyReplicas = deployment.status?.readyReplicas ?: 0,
                availableReplicas = deployment.status?.availableReplicas ?: 0,
                age = calculateAge(deployment.metadata?.creationTimestamp),
                labels = deployment.metadata?.labels ?: emptyMap(),
                annotations = deployment.metadata?.annotations ?: emptyMap(),
                creationTimestamp = deployment.metadata?.creationTimestamp?.time ?: 0L,
                uid = deployment.metadata?.uid ?: ""
            )
        } catch (e: ApiException) {
            throw Exception("Failed to get deployment: ${e.message}")
        }
    }

    override suspend fun deleteDeployment(clusterId: String, namespace: String, name: String) {
        val appsV1Api = appsV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        try {
            appsV1Api.deleteNamespacedDeployment(name, namespace)
        } catch (e: ApiException) {
            throw Exception("Failed to delete deployment: ${e.message}")
        }
    }

    override suspend fun scaleDeployment(clusterId: String, namespace: String, name: String, replicas: Int) {
        val appsV1Api = appsV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        try {
            val scale = V1Scale().apply {
                spec = V1ScaleSpec().apply {
                    replicas = replicas
                }
            }
            appsV1Api.replaceNamespacedDeploymentScale(name, namespace, scale)
        } catch (e: ApiException) {
            throw Exception("Failed to scale deployment: ${e.message}")
        }
    }

    override suspend fun getConfigMaps(clusterId: String, namespace: String?): List<ConfigMap> {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        return try {
            val configMapList = if (namespace != null) {
                coreV1Api.listNamespacedConfigMap(namespace)
            } else {
                coreV1Api.listConfigMapForAllNamespaces()
            }
            
            configMapList.items?.map { configMap ->
                ConfigMap(
                    name = configMap.metadata?.name ?: "",
                    namespace = configMap.metadata?.namespace ?: "",
                    data = configMap.data ?: emptyMap(),
                    age = calculateAge(configMap.metadata?.creationTimestamp),
                    labels = configMap.metadata?.labels ?: emptyMap(),
                    annotations = configMap.metadata?.annotations ?: emptyMap(),
                    creationTimestamp = configMap.metadata?.creationTimestamp?.time ?: 0L,
                    uid = configMap.metadata?.uid ?: ""
                )
            } ?: emptyList()
        } catch (e: ApiException) {
            throw Exception("Failed to get config maps: ${e.message}")
        }
    }

    override suspend fun getConfigMap(clusterId: String, namespace: String, name: String): ConfigMap {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        return try {
            val configMap = coreV1Api.readNamespacedConfigMap(name, namespace)
            ConfigMap(
                name = configMap.metadata?.name ?: "",
                namespace = configMap.metadata?.namespace ?: "",
                data = configMap.data ?: emptyMap(),
                age = calculateAge(configMap.metadata?.creationTimestamp),
                labels = configMap.metadata?.labels ?: emptyMap(),
                annotations = configMap.metadata?.annotations ?: emptyMap(),
                creationTimestamp = configMap.metadata?.creationTimestamp?.time ?: 0L,
                uid = configMap.metadata?.uid ?: ""
            )
        } catch (e: ApiException) {
            throw Exception("Failed to get config map: ${e.message}")
        }
    }

    override suspend fun createConfigMap(clusterId: String, configMap: ConfigMap) {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        try {
            val k8sConfigMap = V1ConfigMap().apply {
                metadata = V1ObjectMeta().apply {
                    name = configMap.name
                    namespace = configMap.namespace
                    labels = configMap.labels
                    annotations = configMap.annotations
                }
                data = configMap.data
            }
            coreV1Api.createNamespacedConfigMap(configMap.namespace, k8sConfigMap)
        } catch (e: ApiException) {
            throw Exception("Failed to create config map: ${e.message}")
        }
    }

    override suspend fun updateConfigMap(clusterId: String, configMap: ConfigMap) {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        try {
            val k8sConfigMap = V1ConfigMap().apply {
                metadata = V1ObjectMeta().apply {
                    name = configMap.name
                    namespace = configMap.namespace
                    labels = configMap.labels
                    annotations = configMap.annotations
                }
                data = configMap.data
            }
            coreV1Api.replaceNamespacedConfigMap(configMap.name, configMap.namespace, k8sConfigMap)
        } catch (e: ApiException) {
            throw Exception("Failed to update config map: ${e.message}")
        }
    }

    override suspend fun deleteConfigMap(clusterId: String, namespace: String, name: String) {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        try {
            coreV1Api.deleteNamespacedConfigMap(name, namespace)
        } catch (e: ApiException) {
            throw Exception("Failed to delete config map: ${e.message}")
        }
    }

    override suspend fun getSecrets(clusterId: String, namespace: String?): List<Secret> {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        return try {
            val secretList = if (namespace != null) {
                coreV1Api.listNamespacedSecret(namespace)
            } else {
                coreV1Api.listSecretForAllNamespaces()
            }
            
            secretList.items?.map { secret ->
                Secret(
                    name = secret.metadata?.name ?: "",
                    namespace = secret.metadata?.namespace ?: "",
                    type = secret.type ?: "Opaque",
                    data = secret.data ?: emptyMap(),
                    age = calculateAge(secret.metadata?.creationTimestamp),
                    labels = secret.metadata?.labels ?: emptyMap(),
                    annotations = secret.metadata?.annotations ?: emptyMap(),
                    creationTimestamp = secret.metadata?.creationTimestamp?.time ?: 0L,
                    uid = secret.metadata?.uid ?: ""
                )
            } ?: emptyList()
        } catch (e: ApiException) {
            throw Exception("Failed to get secrets: ${e.message}")
        }
    }

    override suspend fun getSecret(clusterId: String, namespace: String, name: String): Secret {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        return try {
            val secret = coreV1Api.readNamespacedSecret(name, namespace)
            Secret(
                name = secret.metadata?.name ?: "",
                namespace = secret.metadata?.namespace ?: "",
                type = secret.type ?: "Opaque",
                data = secret.data ?: emptyMap(),
                age = calculateAge(secret.metadata?.creationTimestamp),
                labels = secret.metadata?.labels ?: emptyMap(),
                annotations = secret.metadata?.annotations ?: emptyMap(),
                creationTimestamp = secret.metadata?.creationTimestamp?.time ?: 0L,
                uid = secret.metadata?.uid ?: ""
            )
        } catch (e: ApiException) {
            throw Exception("Failed to get secret: ${e.message}")
        }
    }

    override suspend fun createSecret(clusterId: String, secret: Secret) {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        try {
            val k8sSecret = V1Secret().apply {
                metadata = V1ObjectMeta().apply {
                    name = secret.name
                    namespace = secret.namespace
                    labels = secret.labels
                    annotations = secret.annotations
                }
                type = secret.type
                data = secret.data
            }
            coreV1Api.createNamespacedSecret(secret.namespace, k8sSecret)
        } catch (e: ApiException) {
            throw Exception("Failed to create secret: ${e.message}")
        }
    }

    override suspend fun updateSecret(clusterId: String, secret: Secret) {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        try {
            val k8sSecret = V1Secret().apply {
                metadata = V1ObjectMeta().apply {
                    name = secret.name
                    namespace = secret.namespace
                    labels = secret.labels
                    annotations = secret.annotations
                }
                type = secret.type
                data = secret.data
            }
            coreV1Api.replaceNamespacedSecret(secret.name, secret.namespace, k8sSecret)
        } catch (e: ApiException) {
            throw Exception("Failed to update secret: ${e.message}")
        }
    }

    override suspend fun deleteSecret(clusterId: String, namespace: String, name: String) {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        try {
            coreV1Api.deleteNamespacedSecret(name, namespace)
        } catch (e: ApiException) {
            throw Exception("Failed to delete secret: ${e.message}")
        }
    }

    override suspend fun getEvents(clusterId: String, namespace: String?): List<Event> {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        return try {
            val eventList = if (namespace != null) {
                coreV1Api.listNamespacedEvent(namespace)
            } else {
                coreV1Api.listEventForAllNamespaces()
            }
            
            eventList.items?.map { event ->
                Event(
                    name = event.metadata?.name ?: "",
                    namespace = event.metadata?.namespace ?: "",
                    reason = event.reason ?: "",
                    message = event.message ?: "",
                    type = event.type ?: "",
                    count = event.count ?: 0,
                    firstTimestamp = event.firstTimestamp?.time ?: 0L,
                    lastTimestamp = event.lastTimestamp?.time ?: 0L,
                    involvedObject = event.involvedObject?.name ?: ""
                )
            } ?: emptyList()
        } catch (e: ApiException) {
            throw Exception("Failed to get events: ${e.message}")
        }
    }

    override suspend fun getNamespaces(clusterId: String): List<String> {
        val coreV1Api = coreV1Apis[clusterId] ?: throw Exception("Cluster not connected")
        
        return try {
            val namespaceList = coreV1Api.listNamespace()
            namespaceList.items?.mapNotNull { it.metadata?.name } ?: emptyList()
        } catch (e: ApiException) {
            throw Exception("Failed to get namespaces: ${e.message}")
        }
    }

    override suspend fun getResourceYaml(clusterId: String, kind: String, namespace: String?, name: String): String {
        // YAML export implementation would go here
        return "YAML export not implemented yet"
    }

    override suspend fun updateResourceYaml(clusterId: String, yaml: String) {
        // YAML update implementation would go here
    }

    override suspend fun createResourceYaml(clusterId: String, yaml: String) {
        // YAML create implementation would go here
    }

    private fun calculateAge(creationTimestamp: Date?): String {
        if (creationTimestamp == null) return "Unknown"
        
        val now = Date()
        val diff = now.time - creationTimestamp.time
        
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        
        return when {
            days > 0 -> "${days}d"
            hours > 0 -> "${hours}h"
            minutes > 0 -> "${minutes}m"
            else -> "${seconds}s"
        }
    }
}