package com.openlens.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cluster(
    val id: String,
    val name: String,
    val url: String,
    val kubeconfig: String,
    val isConnected: Boolean = false,
    val lastConnected: Long? = null,
    val version: String? = null,
    val nodes: Int = 0,
    val namespaces: List<String> = emptyList()
) : Parcelable