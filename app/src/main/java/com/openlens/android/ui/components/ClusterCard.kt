package com.openlens.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openlens.android.data.model.Cluster
import com.openlens.android.ui.theme.StatusRunning
import com.openlens.android.ui.theme.StatusUnknown

@Composable
fun ClusterCard(
    cluster: Cluster,
    onConnectClick: () -> Unit = {},
    onDisconnectClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = cluster.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = cluster.url,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = if (cluster.isConnected) StatusRunning else StatusUnknown,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = if (cluster.isConnected) "Connected" else "Disconnected",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options"
                            )
                        }
                        
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            if (cluster.isConnected) {
                                DropdownMenuItem(
                                    text = { Text("Disconnect") },
                                    onClick = {
                                        onDisconnectClick()
                                        showMenu = false
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.LinkOff, contentDescription = null)
                                    }
                                )
                            } else {
                                DropdownMenuItem(
                                    text = { Text("Connect") },
                                    onClick = {
                                        onConnectClick()
                                        showMenu = false
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Link, contentDescription = null)
                                    }
                                )
                            }
                            
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = {
                                    onDeleteClick()
                                    showMenu = false
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Delete, contentDescription = null)
                                }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Nodes: ${cluster.nodes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = cluster.version ?: "Unknown",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}