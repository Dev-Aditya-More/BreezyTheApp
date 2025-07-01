package com.example.breezytheapp.ui.presentation.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breezytheapp.data.WeatherUiState
import com.example.breezytheapp.data.remote.WeatherApi
import com.example.breezytheapp.domain.models.WeatherData
import com.example.breezytheapp.domain.repository.WeatherRepository
import com.example.breezytheapp.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel : ViewModel() {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(WeatherApi::class.java)
    private val repository = WeatherRepository(api)

    var weatherState by mutableStateOf<WeatherData?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    val savedCities = MutableStateFlow<List<String>>(emptyList())

    fun loadWeather(city: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                weatherState = repository.getWeather(city)
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}
