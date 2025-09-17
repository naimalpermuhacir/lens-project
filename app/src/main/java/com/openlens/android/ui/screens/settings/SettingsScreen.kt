package com.openlens.android.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.openlens.android.R
import com.openlens.android.ui.components.OpenLensBottomNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nav_settings)) }
            )
        },
        bottomBar = {
            OpenLensBottomNavigation(navController = navController)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.theme),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                Card {
                    Column {
                        ListItem(
                            headlineContent = { Text(stringResource(R.string.theme_light)) },
                            leadingContent = {
                                RadioButton(
                                    selected = uiState.theme == "light",
                                    onClick = { viewModel.setTheme("light") }
                                )
                            }
                        )
                        ListItem(
                            headlineContent = { Text(stringResource(R.string.theme_dark)) },
                            leadingContent = {
                                RadioButton(
                                    selected = uiState.theme == "dark",
                                    onClick = { viewModel.setTheme("dark") }
                                )
                            }
                        )
                        ListItem(
                            headlineContent = { Text(stringResource(R.string.theme_system)) },
                            leadingContent = {
                                RadioButton(
                                    selected = uiState.theme == "system",
                                    onClick = { viewModel.setTheme("system") }
                                )
                            }
                        )
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                Text(
                    text = stringResource(R.string.notifications),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                Card {
                    ListItem(
                        headlineContent = { Text(stringResource(R.string.notifications)) },
                        trailingContent = {
                            Switch(
                                checked = uiState.notificationsEnabled,
                                onCheckedChange = { viewModel.setNotificationsEnabled(it) }
                            )
                        }
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                Text(
                    text = stringResource(R.string.about),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                Card {
                    ListItem(
                        headlineContent = { Text("OpenLens Android") },
                        supportingContent = { Text("Version 1.0.0") }
                    )
                }
            }
        }
    }
}