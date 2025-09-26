package com.leafbound.wastemgt.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.leafbound.wastemgt.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    Column {
        Text(text = "Home Screen")
        Button(onClick = { navController.navigate(Screen.Camera.route) }) {
            Text("Go to Camera")
        }
        Button(onClick = { navController.navigate(Screen.Maps.route) }) {
            Text("Go to Maps")
        }
        Button(onClick = { navController.navigate(Screen.Recommendations.route) }) {
            Text("Go to Recommendations")
        }
    }
}