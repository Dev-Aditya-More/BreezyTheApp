package com.example.breezytheapp.data.remote

import com.example.breezytheapp.data.Forecast
import com.example.breezytheapp.domain.models.WeatherData
import com.example.breezytheapp.domain.models.dummyForecast

fun WeatherDto.toWeatherData(): WeatherData {
    val weather = weather.firstOrNull()
    return WeatherData(
        iconUrl = "https://openweathermap.org/img/wn/${weather?.icon}@2x.png",
        city = name,
        temperature = main.temp.toInt(),
        description = weather?.description ?: "N/A",
        icon = weather?.icon ?: "01d",
        wind = wind.speed,
        humidity = main.humidity,
        pressure = main.pressure,
        forecast = dummyForecast()
    )
}
