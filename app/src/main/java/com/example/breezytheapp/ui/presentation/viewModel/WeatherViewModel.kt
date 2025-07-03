package com.example.breezytheapp.ui.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breezytheapp.data.remote.NetworkResponse
import com.example.breezytheapp.data.remote.RetrofitInstance
import kotlinx.coroutines.launch
import com.example.breezytheapp.data.remote.WeatherModel
import com.example.breezytheapp.utils.Constants.API_KEY

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city: String){

        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {

            try {
                val response = weatherApi.getCurrentWeather(API_KEY, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Failed to load data.")
                }
            }
            catch (e: Exception) {
                _weatherResult.value = NetworkResponse.Error("Error: Failed to load data")
            }

        }
    }
}
