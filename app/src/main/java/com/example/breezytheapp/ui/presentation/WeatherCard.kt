package com.example.breezytheapp.ui.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.breezytheapp.domain.models.WeatherData

@Composable
fun WeatherCard(data: WeatherData, isDarkTheme: Boolean = isSystemInDarkTheme()) {
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val subTextColor = textColor.copy(alpha = 0.7f)
    val labelTextColor = Color.DarkGray.copy(alpha = if (isDarkTheme) 0.6f else 0.8f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)) // Frosted effect
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Weather Icon
            AsyncImage(
                model = data.iconUrl,
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .size(60.dp)
                    .padding(bottom = 10.dp)
            )

            // Temperature
            Text(
                text = "${data.temperature}°C",
                fontSize = 38.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )

            // Description
            Text(
                text = data.description.replaceFirstChar { it.uppercaseChar() },
                fontSize = 16.sp,
                color = subTextColor
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Weather Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                InfoItem(label = "Wind", value = "${data.wind} m/s", labelColor = labelTextColor, valueColor = textColor)
                InfoItem(label = "Humidity", value = "${data.humidity}%", labelColor = labelTextColor, valueColor = textColor)
                InfoItem(label = "Pressure", value = "${data.pressure} hPa", labelColor = labelTextColor, valueColor = textColor)
            }
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String, labelColor: Color, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = labelColor
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = valueColor,
            fontWeight = FontWeight.Medium
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun WeatherCardPreview() {
//    WeatherCard(
//        data = WeatherData(
//            city = "New York",
//            temperature = 25,
//            description = "Sunny",
//            iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
//            wind = 5.0,
//            humidity = 60,
//            pressure = 1012,
//            forecast = emptyList(),
//            icon = ""
//        )
//    )
//}

