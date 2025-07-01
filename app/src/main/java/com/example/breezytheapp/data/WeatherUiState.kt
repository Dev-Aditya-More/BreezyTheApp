package com.example.breezytheapp.data

data class WeatherUiState(
    val city: String,
    val temperature: Int,
    val description: String,
    val iconUrl: String,
    val wind: Double,
    val humidity: Int,
    val pressure: Int,
    val forecast: List<Forecast>
)

data class Forecast(
    val day: String,
    val icon: String,
    val max: Int,
    val min: Int
)

