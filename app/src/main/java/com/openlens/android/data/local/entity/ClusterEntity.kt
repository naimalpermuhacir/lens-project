package com.openlens.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openlens.android.data.model.Cluster

@Entity(tableName = "clusters")
data class ClusterEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val url: String,
    val kubeconfig: String,
    val isConnected: Boolean = false,
    val lastConnected: Long? = null,
    val version: String? = null,
    val nodes: Int = 0,
    val namespaces: String = "" // JSON string of namespaces
)

fun ClusterEntity.toCluster(): Cluster {
    return Cluster(
        id = id,
        name = name,
        url = url,
        kubeconfig = kubeconfig,
        isConnected = isConnected,
        lastConnected = lastConnected,
        version = version,
        nodes = nodes,
        namespaces = emptyList() // TODO: Parse JSON string
    )
}

fun Cluster.toEntity(): ClusterEntity {
    return ClusterEntity(
        id = id,
        name = name,
        url = url,
        kubeconfig = kubeconfig,
        isConnected = isConnected,
        lastConnected = lastConnected,
        version = version,
        nodes = nodes,
        namespaces = "" // TODO: Convert to JSON string
    )
}