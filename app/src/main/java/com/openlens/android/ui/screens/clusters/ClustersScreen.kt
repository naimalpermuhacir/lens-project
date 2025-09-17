package com.openlens.android.ui.screens.clusters

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.openlens.android.R
import com.openlens.android.data.model.Cluster
import com.openlens.android.ui.components.ClusterCard
import com.openlens.android.ui.components.OpenLensBottomNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClustersScreen(
    navController: NavController,
    viewModel: ClustersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddClusterDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nav_clusters)) },
                actions = {
                    IconButton(onClick = { showAddClusterDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_cluster))
                    }
                }
            )
        },
        bottomBar = {
            OpenLensBottomNavigation(navController = navController)
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            uiState.clusters.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "No clusters found",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Button(onClick = { showAddClusterDialog = true }) {
                            Text(stringResource(R.string.add_cluster))
                        }
                    }
                }
            }
            
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.clusters) { cluster ->
                        ClusterCard(
                            cluster = cluster,
                            onConnectClick = { viewModel.connectToCluster(cluster.id) },
                            onDisconnectClick = { viewModel.disconnectFromCluster(cluster.id) },
                            onDeleteClick = { viewModel.deleteCluster(cluster.id) }
                        )
                    }
                }
            }
        }
    }

    if (showAddClusterDialog) {
        AddClusterDialog(
            onDismiss = { showAddClusterDialog = false },
            onAddCluster = { name, url, kubeconfig ->
                viewModel.addCluster(name, url, kubeconfig)
                showAddClusterDialog = false
            }
        )
    }
}