package com.example.breezytheapp.utils

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.compose.ui.text.font.FontVariation.Settings

object Constants {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val API_KEY = "95ce7931174580a31b6fc6898f4d5a94"
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

fun openLocationSettings(context: Context) {
    val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}
