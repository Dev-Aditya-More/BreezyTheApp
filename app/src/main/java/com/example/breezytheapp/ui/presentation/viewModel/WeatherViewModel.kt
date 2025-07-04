package com.example.breezytheapp.ui.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breezytheapp.data.remote.ForecastModel
import com.example.breezytheapp.data.remote.NetworkResponse
import com.example.breezytheapp.data.remote.RetrofitInstance
import kotlinx.coroutines.launch
import com.example.breezytheapp.data.remote.WeatherModel
import com.example.breezytheapp.utils.Constants
import com.example.breezytheapp.utils.Constants.API_KEY

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<ForecastModel>>(NetworkResponse.Idle)
    val weatherResult : LiveData<NetworkResponse<ForecastModel>> = _weatherResult

    fun getData(city: String) {
        _weatherResult.postValue(NetworkResponse.Loading)

        viewModelScope.launch {
            try {
                val response = weatherApi.getForecastWeather(apiKey = API_KEY, city = city, days = 5)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.postValue(NetworkResponse.Success(it))
                    } ?: run {
                        _weatherResult.postValue(NetworkResponse.Error("Empty response body"))
                    }
                } else {
                    _weatherResult.postValue(NetworkResponse.Error("Failed to load data."))
                }
            } catch (e: Exception) {
                _weatherResult.postValue(NetworkResponse.Error("Error: Failed to load data"))
            }
        }
    }
}
