package com.example.breezytheapp.data.remote

data class ForecastModel(
    val location: Location,
    val current: Current,
    val forecast: ForecastContainer? = null
)

data class ForecastContainer(
    val forecastday: List<ForecastDay>? = null
)

data class ForecastDay(
    val date: String,
    val day: Day
)

data class Day(
    val avgtemp_c: String,
    val maxtemp_c: String,
    val mintemp_c: String,
    val condition: Condition
)