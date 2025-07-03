package com.example.breezytheapp.data.remote

import com.example.breezytheapp.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/v1/current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String = Constants.API_KEY,
        @Query("q") city: String
    ): retrofit2.Response<WeatherModel>

}

