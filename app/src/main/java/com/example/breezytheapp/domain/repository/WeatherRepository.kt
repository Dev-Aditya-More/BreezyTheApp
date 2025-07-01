package com.example.breezytheapp.domain.repository

import com.example.breezytheapp.data.remote.WeatherApi
import com.example.breezytheapp.data.remote.toWeatherData
import com.example.breezytheapp.domain.models.WeatherData

class WeatherRepository(private val api: WeatherApi) {
    suspend fun getWeather(city: String): WeatherData {
        return api.getCurrentWeather(city).toWeatherData()
    }
}
