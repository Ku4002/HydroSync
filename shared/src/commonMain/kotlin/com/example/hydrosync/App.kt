package com.example.hydrosync

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import ui.screens.DashboardScreen
import ui.screens.PlantsScreen
import ui.screens.HistoryScreen
import ui.screens.SettingsScreen

enum class Screen {
    Dashboard,
    Plants,
    History,
    Settings
}

@Composable
fun App() {

    var currentScreen by remember {
        mutableStateOf(Screen.Dashboard)
    }

    MaterialTheme {

        when (currentScreen) {

            Screen.Dashboard -> DashboardScreen(
                onHistoryClick = {
                    currentScreen = Screen.History
                },
                onPlantClick = {
                    currentScreen = Screen.Plants
                }
            )

            Screen.Plants -> PlantsScreen(
                onBackClick = {
                    currentScreen = Screen.Dashboard
                }
            )

            Screen.History -> HistoryScreen(
                onBackClick = {
                    currentScreen = Screen.Dashboard
                }
            )

            Screen.Settings -> SettingsScreen()
        }
    }
}