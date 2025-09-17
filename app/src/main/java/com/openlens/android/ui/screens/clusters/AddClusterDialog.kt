package com.openlens.android.ui.screens.clusters

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.openlens.android.R

@Composable
fun AddClusterDialog(
    onDismiss: () -> Unit,
    onAddCluster: (name: String, url: String, kubeconfig: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var kubeconfig by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_cluster)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.cluster_name)) },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text(stringResource(R.string.cluster_url)) },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = kubeconfig,
                    onValueChange = { kubeconfig = it },
                    label = { Text(stringResource(R.string.kubeconfig)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && url.isNotBlank() && kubeconfig.isNotBlank()) {
                        onAddCluster(name, url, kubeconfig)
                    }
                },
                enabled = name.isNotBlank() && url.isNotBlank() && kubeconfig.isNotBlank()
            ) {
                Text(stringResource(R.string.connect))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}