package com.example.breezytheapp.data.remote

data class WeatherDto(
    val name: String,
    val main: MainDto,
    val weather: List<WeatherDescription>,
    val wind: WindDto
)

data class MainDto(val temp: Double, val humidity: Int, val pressure: Int)
data class WindDto(val speed: Double)
data class WeatherDescription(val main: String, val description: String, val icon: String)
