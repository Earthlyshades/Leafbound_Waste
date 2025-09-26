package com.leafbound.wastemgt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leafbound.wastemgt.screen.HomeScreen
import com.leafbound.wastemgt.screen.CameraScreen
import com.leafbound.wastemgt.screen.MapsScreen
import com.leafbound.wastemgt.screen.RecommendationsScreen
import com.leafbound.wastemgt.navigation.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WasteMgmtApp()
        }
    }
}

@Composable
fun WasteMgmtApp() {
    val navController = rememberNavController()

    Scaffold { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = androidx.compose.ui.Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Camera.route) { CameraScreen(navController) }
            composable(Screen.Maps.route) { MapsScreen(navController) }
            composable(Screen.Recommendations.route) { backStackEntry ->
                val barcode = backStackEntry.arguments?.getString("barcode")
                RecommendationsScreen(navController, barcode)
            }
        }
    }
}
