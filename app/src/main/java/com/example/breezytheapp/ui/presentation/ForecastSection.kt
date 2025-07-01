package com.example.breezytheapp.ui.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.breezytheapp.data.Forecast

@Composable
fun ForecastSection(forecast: List<Forecast>) {
    LazyRow {
        items(forecast) { day ->
            Column(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .width(72.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(day.day)
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${day.icon}@2x.png",
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Text("${day.max}°/${day.min}°", fontSize = 12.sp)
            }
        }
    }
}
