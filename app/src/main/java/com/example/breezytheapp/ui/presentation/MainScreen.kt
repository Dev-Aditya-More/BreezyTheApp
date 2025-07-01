package com.example.breezytheapp.ui.presentation

import android.Manifest
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.breezytheapp.ui.presentation.viewModel.WeatherViewModel
import com.example.breezytheapp.utils.isLocationEnabled
import com.example.breezytheapp.utils.openLocationSettings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(viewModel: WeatherViewModel = remember { WeatherViewModel() }) {
    val context = LocalContext.current
    remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val weatherState = viewModel.weatherState
    val isLoading = viewModel.isLoading
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val locationEnabled = remember { mutableStateOf(isLocationEnabled(context)) }
    var hasLoadedWeather by remember { mutableStateOf(false) }

    LaunchedEffect(locationPermissionState.status.isGranted, locationEnabled.value) {
        if (hasLoadedWeather) return@LaunchedEffect

        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
            return@LaunchedEffect
        }

        if (!locationEnabled.value) return@LaunchedEffect

        try {
            delay(1500) // Give GPS time
            val location = withTimeoutOrNull(5000) {
                getBestAvailableLocation(context)
            }

            val city = if (location != null) {
                val addresses = withContext(Dispatchers.IO) {
                    Geocoder(context, Locale.getDefault())
                        .getFromLocation(location.latitude, location.longitude, 1)
                }
                val locality = addresses?.firstOrNull()?.locality?.trim()
                if (locality.isNullOrEmpty() || locality.equals("Mountain View", ignoreCase = true)) {
                    "Delhi" // fallback
                } else {
                    locality
                }
            } else {
                "Delhi"
            }
            val finalCity = city.ifEmpty { "Delhi" }
            viewModel.loadWeather(finalCity)
        } catch (e: Exception) {
            e.printStackTrace()
            viewModel.loadWeather("Delhi")
        } finally {
            hasLoadedWeather = true
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF4FC3F7),  // Sky Blue
                Color(0xFFB2EBF2),  // Soft Cyan (intermediate)
                Color(0xFFFFF9C4),  // Pale Yellow
                Color(0xFFFFE082),  // Light Orange
                Color(0xFFFFAB91)   // Soft Peach
            )
        )
    )

    ) {
        when {
            !locationEnabled.value -> {
                LocationOffPrompt { openLocationSettings(context) }
            }

            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            weatherState != null -> {
                val state = weatherState

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(top = 35.dp)
                ) {
                    SearchBar(
                        lastSearch = state.city
                    ) { city ->
                        viewModel.loadWeather(city)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    TopAppBarContent(city = state.city)
                    Spacer(modifier = Modifier.height(16.dp))
                    WeatherCard(data = state)
                    Spacer(modifier = Modifier.height(24.dp))
                    ForecastSection(forecast = state.forecast)
                }
            }

            !hasLoadedWeather -> {
                Text("Wait a minute...", modifier = Modifier.align(Alignment.Center))
            }

            else -> {
                Text("Unable to load weather", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}



suspend fun getBestAvailableLocation(context: Context): Location? {
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    return withContext(Dispatchers.IO) {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
    }
}


