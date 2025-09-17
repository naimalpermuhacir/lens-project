package com.openlens.android.ui.screens.storage

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
fun StorageScreen(
    navController: NavController,
    viewModel: StorageViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nav_storage)) }
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
                text = "Storage management coming soon",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}