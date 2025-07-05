package com.example.breezytheapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.breezytheapp.ui.presentation.WeatherPage
import com.example.breezytheapp.ui.presentation.WeatherViewModelFactory
import com.example.breezytheapp.ui.presentation.viewModel.WeatherViewModel
import com.example.breezytheapp.ui.theme.BreezyTheAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = WeatherViewModelFactory(applicationContext)
        val weatherViewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]

        setContent {
            BreezyTheAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherPage(weatherViewModel)
                }
            }
        }
    }
}


