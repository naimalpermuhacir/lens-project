package com.openlens.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.openlens.android.ui.screens.clusters.ClustersScreen
import com.openlens.android.ui.screens.workloads.WorkloadsScreen
import com.openlens.android.ui.screens.services.ServicesScreen
import com.openlens.android.ui.screens.storage.StorageScreen
import com.openlens.android.ui.screens.config.ConfigScreen
import com.openlens.android.ui.screens.monitoring.MonitoringScreen
import com.openlens.android.ui.screens.settings.SettingsScreen

@Composable
fun OpenLensNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Clusters.route,
        modifier = modifier
    ) {
        composable(Screen.Clusters.route) {
            ClustersScreen(navController = navController)
        }
        composable(Screen.Workloads.route) {
            WorkloadsScreen(navController = navController)
        }
        composable(Screen.Services.route) {
            ServicesScreen(navController = navController)
        }
        composable(Screen.Storage.route) {
            StorageScreen(navController = navController)
        }
        composable(Screen.Config.route) {
            ConfigScreen(navController = navController)
        }
        composable(Screen.Monitoring.route) {
            MonitoringScreen(navController = navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}