package com.example.breezytheapp.ui.presentation.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breezytheapp.data.remote.ForecastModel
import com.example.breezytheapp.data.remote.NetworkResponse
import com.example.breezytheapp.data.remote.RetrofitInstance
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import com.example.breezytheapp.utils.Constants.API_KEY
import com.example.breezytheapp.utils.DataStoreManager
import kotlinx.coroutines.flow.distinctUntilChanged

class WeatherViewModel(private val context: Context) : ViewModel() {
    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<ForecastModel>>(NetworkResponse.Idle)
    val weatherResult: LiveData<NetworkResponse<ForecastModel>> = _weatherResult

    private val _currentCity = mutableStateOf("Delhi")
    val currentCity: State<String> = _currentCity

    init {
        viewModelScope.launch {
            DataStoreManager.readCity(context)
                .distinctUntilChanged()
                .collect { savedCity ->
                    _currentCity.value = savedCity
                    getData(savedCity)
                }
        }
    }

    fun getData(city: String) {
        _weatherResult.postValue(NetworkResponse.Loading)

        viewModelScope.launch {
            try {
                val response = weatherApi.getForecastWeather(apiKey = API_KEY, city = city, days = 5)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.postValue(NetworkResponse.Success(it))
                        _currentCity.value = city
                        DataStoreManager.saveCity(context, city)
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

