package com.openlens.android.ui.screens.monitoring

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.openlens.android.R
import com.openlens.android.ui.components.OpenLensBottomNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonitoringScreen(
    navController: NavController,
    viewModel: MonitoringViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nav_monitoring)) }
            )
        },
        bottomBar = {
            OpenLensBottomNavigation(navController = navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Monitoring dashboard coming soon",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}