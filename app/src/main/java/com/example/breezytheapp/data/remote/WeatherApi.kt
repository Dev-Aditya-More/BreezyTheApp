package com.example.breezytheapp.data.remote

import com.example.breezytheapp.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/v1/forecast.json")
    suspend fun getForecastWeather(
        @Query("key") apiKey: String = Constants.API_KEY,
        @Query("q") city: String,
        @Query("days") days: Int = 5
    ): retrofit2.Response<ForecastModel>

}

