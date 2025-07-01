package com.example.breezytheapp

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.breezytheapp.ui.presentation.MainScreen
import com.example.breezytheapp.ui.theme.BreezyTheAppTheme
import kotlinx.coroutines.delay
import androidx.compose.ui.text.font.FontStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BreezyTheAppTheme {
                val context = LocalContext.current
                val navController = rememberNavController()

                val isConnected = hasInternet()

                LaunchedEffect(isConnected) {
                    if (!isConnected) {
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show()
                    }
                }

                val locationPermissionGranted = remember { mutableStateOf(false) }

                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { granted ->
                        locationPermissionGranted.value = granted
                        if (!granted) {
                            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") {
                        SplashScreen(navController, onRequestPermission = {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        })
                    }
                    composable("main") {
                        MainScreen()
                    }
                }
            }
        }
    }

    private fun hasInternet(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

@Composable
fun SplashScreen(
    navController: NavHostController,
    onRequestPermission: () -> Unit
) {
    val alreadyRequested = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1200)
        if (!alreadyRequested.value) {
            onRequestPermission()
            alreadyRequested.value = true
        }
        delay(1000)
        navController.navigate("main") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF4FC3F7),
                        Color(0xFFFCE38A),
                        Color(0xFFF38181)
                    )
                )
            )
    ) {
        Text(
            text = "Breezy",
            fontSize = 48.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Light,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
