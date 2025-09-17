package com.openlens.android.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.openlens.android.R
import com.openlens.android.ui.navigation.Screen

@Composable
fun OpenLensBottomNavigation(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem(
            route = Screen.Clusters.route,
            icon = Icons.Default.Cloud,
            label = stringResource(R.string.nav_clusters)
        ),
        BottomNavItem(
            route = Screen.Workloads.route,
            icon = Icons.Default.Work,
            label = stringResource(R.string.nav_workloads)
        ),
        BottomNavItem(
            route = Screen.Services.route,
            icon = Icons.Default.Dns,
            label = stringResource(R.string.nav_services)
        ),
        BottomNavItem(
            route = Screen.Storage.route,
            icon = Icons.Default.Storage,
            label = stringResource(R.string.nav_storage)
        ),
        BottomNavItem(
            route = Screen.Config.route,
            icon = Icons.Default.Settings,
            label = stringResource(R.string.nav_config)
        ),
        BottomNavItem(
            route = Screen.Monitoring.route,
            icon = Icons.Default.Analytics,
            label = stringResource(R.string.nav_monitoring)
        ),
        BottomNavItem(
            route = Screen.Settings.route,
            icon = Icons.Default.Settings,
            label = stringResource(R.string.nav_settings)
        )
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(modifier = modifier) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)