package com.example.breezytheapp.domain.models

import com.example.breezytheapp.data.Forecast

data class WeatherData(
    val iconUrl: String,
    val city: String,
    val temperature: Int,
    val description: String,
    val icon: String,
    val wind: Double,
    val humidity: Int,
    val pressure: Int,
    val forecast: List<Forecast>
)

fun dummyForecast(): List<Forecast> = listOf(
    Forecast("Mon", "01d", 35, 28),
    Forecast("Tue", "02d", 32, 26),
    Forecast("Wed", "10d", 30, 24),
    Forecast("Thu", "04d", 33, 27),
    Forecast("Fri", "01d", 36, 29),
)
