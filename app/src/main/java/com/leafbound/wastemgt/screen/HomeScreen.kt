package com.leafbound.wastemgt.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.leafbound.wastemgt.navigation.CAMERA_SCREEN
import com.leafbound.wastemgt.navigation.MAPS_SCREEN
import com.leafbound.wastemgt.navigation.RECOMMENDATIONS_SCREEN

@Composable
fun HomeScreen(navController: NavController) {
    Column {
        Text(text = "Home Screen")
        Button(onClick = { navController.navigate(CAMERA_SCREEN) }) {
            Text("Go to Camera")
        }
        Button(onClick = { navController.navigate(MAPS_SCREEN) }) {
            Text("Go to Maps")
        }
        Button(onClick = { navController.navigate(RECOMMENDATIONS_SCREEN) }) {
            Text("Go to Recommendations")
        }
    }
}