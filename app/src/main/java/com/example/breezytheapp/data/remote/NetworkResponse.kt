package com.example.breezytheapp.data.remote

sealed class NetworkResponse<out T> {

    object Idle : NetworkResponse<Nothing>()
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    data class Error(val message: String) : NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()

}