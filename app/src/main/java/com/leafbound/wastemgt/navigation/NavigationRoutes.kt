package com.leafbound.wastemgt.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Camera : Screen("camera")
    object Maps : Screen("maps")
    object Recommendations : Screen("recommendations/{barcode}") {
        fun createRoute(barcode: String) = "recommendations/$barcode"
    }
}