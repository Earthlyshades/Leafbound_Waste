package com.leafbound.wastemgt.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leafbound.wastemgt.screen.HomeScreen
import androidx.navigation.NavController
import com.leafbound.wastemgt.screen.CameraScreen
import com.leafbound.wastemgt.screen.MapsScreen
import com.leafbound.wastemgt.screen.RecommendationsScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    startDestination: String = "home"
){
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(HOME_SCREEN) { HomeScreen(navHostController) }
        composable(CAMERA_SCREEN) { CameraScreen(navHostController) }
        composable(MAPS_SCREEN) { MapsScreen(navHostController) }
        composable(RECOMMENDATIONS_SCREEN) { backStackEntry ->
            val barcode = backStackEntry.arguments?.getString("barcode")
            RecommendationsScreen(navHostController, barcode)
        }
    }
}

