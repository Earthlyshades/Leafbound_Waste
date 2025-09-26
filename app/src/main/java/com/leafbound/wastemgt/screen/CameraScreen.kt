package com.leafbound.wastemgt.screen

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.leafbound.wastemgt.screen.permissions.CameraPermissionScreen
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import androidx.compose.runtime.LaunchedEffect
import com.leafbound.wastemgt.navigation.Screen




@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraScreen(navController: NavController) {
    var scannedBarcode by remember { mutableStateOf<String?>(null) }
    var previewView: PreviewView? by remember { mutableStateOf(null) }
    Column (modifier = Modifier.background(Color.Blue)) {
        Box(modifier = Modifier.fillMaxWidth().height(80.dp).background(Color.Black), contentAlignment = Alignment.Center){
            Text(text="Take a picture of your barcode", fontSize = 35.sp, color = Color.White)
        }
        Box(modifier = Modifier.fillMaxWidth().height(900.dp)){
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        previewView = this
                    }
                }
            )
        }
    }
    CameraPermissionScreen(
        onPermissionGranted = {
            val context: Context = LocalContext.current
            val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                // 1. Preview setup
                val preview = Preview.Builder().build().apply {
                    previewView?.surfaceProvider?.let { setSurfaceProvider(it) }
                }

                // 2. Barcode scanner setup
                val scanner = BarcodeScanning.getClient()
                val analysisUseCase = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also { analysis ->
                        analysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                            val mediaImage = imageProxy.image
                            if (mediaImage != null) {
                                val inputImage = InputImage.fromMediaImage(
                                    mediaImage,
                                    imageProxy.imageInfo.rotationDegrees
                                )
                                scanner.process(inputImage)
                                    .addOnSuccessListener { barcodes ->
                                        for (barcode in barcodes) {
                                            val rawValue = barcode.rawValue
                                            if (!rawValue.isNullOrEmpty()) {
                                                scannedBarcode = rawValue
                                                Log.d("CameraScreen", "Barcode detected: $rawValue")
                                            }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("CameraScreen", "Barcode scan failed", e)
                                    }
                                    .addOnCompleteListener {
                                        imageProxy.close()
                                    }
                            } else {
                                imageProxy.close()
                            }
                        }
                    }

                // 3. Bind both use cases
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        analysisUseCase
                    )
                } catch (exc: Exception) {
                    Log.e("CameraScreen", "Use case binding failed", exc)
                }
            }, ContextCompat.getMainExecutor(context))
        }
    )

    LaunchedEffect(scannedBarcode) {
        scannedBarcode?.let { code ->
            navController.navigate(Screen.Recommendations.createRoute(code))
        }
    }
}
