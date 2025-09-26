package com.leafbound.wastemgt.screen

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun RecommendationsScreen(
    navController: NavController,
    barcode: String?
) {
    // Use barcode here
    Text("Scanned barcode: $barcode")
}
