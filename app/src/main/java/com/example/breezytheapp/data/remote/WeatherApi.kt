package com.example.breezytheapp.data.remote

import com.example.breezytheapp.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String = Constants.API_KEY,
        @Query("units") units: String = "metric"
    ): WeatherDto
}
