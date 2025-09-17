package com.openlens.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KubernetesResource(
    val name: String,
    val namespace: String,
    val kind: String,
    val status: String,
    val age: String,
    val labels: Map<String, String> = emptyMap(),
    val annotations: Map<String, String> = emptyMap(),
    val creationTimestamp: Long,
    val uid: String
) : Parcelable

@Parcelize
data class Pod(
    val name: String,
    val namespace: String,
    val status: String,
    val ready: String,
    val restarts: Int,
    val age: String,
    val node: String,
    val ip: String,
    val containers: List<Container> = emptyList(),
    val labels: Map<String, String> = emptyMap(),
    val annotations: Map<String, String> = emptyMap(),
    val creationTimestamp: Long,
    val uid: String
) : Parcelable

@Parcelize
data class Container(
    val name: String,
    val image: String,
    val ready: Boolean,
    val restartCount: Int,
    val state: String,
    val cpu: String = "0",
    val memory: String = "0"
) : Parcelable

@Parcelize
data class Service(
    val name: String,
    val namespace: String,
    val type: String,
    val clusterIP: String,
    val externalIP: String,
    val ports: List<ServicePort>,
    val age: String,
    val labels: Map<String, String> = emptyMap(),
    val annotations: Map<String, String> = emptyMap(),
    val creationTimestamp: Long,
    val uid: String
) : Parcelable

@Parcelize
data class ServicePort(
    val name: String?,
    val port: Int,
    val targetPort: String,
    val protocol: String = "TCP"
) : Parcelable

@Parcelize
data class Deployment(
    val name: String,
    val namespace: String,
    val replicas: Int,
    val readyReplicas: Int,
    val availableReplicas: Int,
    val age: String,
    val labels: Map<String, String> = emptyMap(),
    val annotations: Map<String, String> = emptyMap(),
    val creationTimestamp: Long,
    val uid: String
) : Parcelable

@Parcelize
data class ConfigMap(
    val name: String,
    val namespace: String,
    val data: Map<String, String>,
    val age: String,
    val labels: Map<String, String> = emptyMap(),
    val annotations: Map<String, String> = emptyMap(),
    val creationTimestamp: Long,
    val uid: String
) : Parcelable

@Parcelize
data class Secret(
    val name: String,
    val namespace: String,
    val type: String,
    val data: Map<String, String>,
    val age: String,
    val labels: Map<String, String> = emptyMap(),
    val annotations: Map<String, String> = emptyMap(),
    val creationTimestamp: Long,
    val uid: String
) : Parcelable

@Parcelize
data class Event(
    val name: String,
    val namespace: String,
    val reason: String,
    val message: String,
    val type: String,
    val count: Int,
    val firstTimestamp: Long,
    val lastTimestamp: Long,
    val involvedObject: String
) : Parcelable

@Parcelize
data class LogEntry(
    val timestamp: String,
    val level: String,
    val message: String,
    val container: String
) : Parcelable