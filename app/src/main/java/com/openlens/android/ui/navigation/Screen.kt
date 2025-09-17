package com.openlens.android.ui.navigation

sealed class Screen(val route: String) {
    object Clusters : Screen("clusters")
    object Workloads : Screen("workloads")
    object Services : Screen("services")
    object Storage : Screen("storage")
    object Config : Screen("config")
    object Monitoring : Screen("monitoring")
    object Settings : Screen("settings")
}