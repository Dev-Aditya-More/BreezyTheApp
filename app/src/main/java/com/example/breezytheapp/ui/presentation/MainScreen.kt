package com.example.breezytheapp.ui.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.breezytheapp.R
import com.example.breezytheapp.data.remote.ForecastDay
import com.example.breezytheapp.data.remote.ForecastModel
import com.example.breezytheapp.data.remote.NetworkResponse
import com.example.breezytheapp.ui.presentation.viewModel.WeatherViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun WeatherPage(viewModel: WeatherViewModel = viewModel(factory = WeatherViewModelFactory(LocalContext.current))) {
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var city by remember { mutableStateOf("") }

    val weatherResult = viewModel.weatherResult.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    var isSearchOpen by remember { mutableStateOf(false) }
    LaunchedEffect(weatherResult.value) {
        if (weatherResult.value is NetworkResponse.Success) {
            delay(300) // allow UI to settle before collapse
            isSearchOpen = false
            city = ""
        }
    }

    // Choose dynamic gradient (fallback or weather-aware later)
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF222831), Color(0xFF393E46)) // dark, modern gradient
    )

    // Permission & Location Logic
    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
            return@LaunchedEffect
        }

        try {
            delay(500)
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, null
            ).await()

            if (location == null) {
                city = "Delhi"
                viewModel.getData("Delhi")
                return@LaunchedEffect
            }

            val addresses = withContext(Dispatchers.IO) {
                Geocoder(context, Locale.getDefault())
                    .getFromLocation(location.latitude, location.longitude, 1)
            }

            val cityName = addresses?.firstOrNull()?.locality
            city = cityName ?: "Delhi"
            viewModel.getData(city)

        } catch (e: Exception) {
            e.printStackTrace()
            city = "Delhi"
            viewModel.getData("Delhi")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 25.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Menu Icon – stays constant
                IconButton(onClick = { /* open drawer or saved cities */ }) {
                    Icon(Icons.Default.Menu, contentDescription = "Saved Cities", tint = Color.White)
                }

                // Animated Search Bar
                AnimatedVisibility(
                    visible = isSearchOpen,
                    enter = fadeIn() + expandHorizontally(expandFrom = Alignment.End),
                    exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.End)
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 56.dp),
                        value = city,
                        onValueChange = { city = it },
                        label = {
                            Text("Search for any location", color = Color.White)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        trailingIcon = {
                            Row(modifier = Modifier.wrapContentWidth()) {
                                if (city.isNotBlank()) {
                                    IconButton(onClick = { city = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear text",
                                            tint = Color.White
                                        )
                                    }
                                }
                                IconButton(onClick = {
                                    viewModel.getData(city)
                                    keyboardController?.hide()
                                    isSearchOpen = false // <-- Close search bar after submission
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    )
                }

                IconButton(onClick = {
                    isSearchOpen = !isSearchOpen
                    if (!isSearchOpen) {
                        keyboardController?.hide()
                        city = "" // optional: clear search on close
                    }
                }) {
                    Icon(
                        imageVector = if (isSearchOpen) Icons.Default.Close else Icons.Default.Search,
                        contentDescription = if (isSearchOpen) "Close Search" else "Open Search",
                        tint = Color.White
                    )
                }
            }



            when (val result = weatherResult.value) {
                is NetworkResponse.Idle -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.undraw_delivery_location_um5t),
                            contentDescription = "Nothing to show",
                            modifier = Modifier.size(240.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "No location selected",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )

                        Text(
                            text = "Search for a city above to see weather info.",
                            fontSize = 14.sp,
                            color = Color.LightGray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                is NetworkResponse.Loading -> {
                    CircularProgressIndicator(color = Color.White)
                }

                is NetworkResponse.Success -> {
                    WeatherDetails(data = result.data)
                }

                is NetworkResponse.Error -> {
                    Text(
                        text = result.message,
                        color = Color.Red,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                null -> {}
            }
        }
    }
}

@Composable
fun WeatherDetails(data: ForecastModel) {


    Box(
        modifier = Modifier.fillMaxSize()
//            .padding(bottom = 20.dp)
//            .background(backgroundBrush)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location icon",
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.Top),
                        tint = Color.White
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = data.location.name,
                            fontSize = 35.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = data.location.country,
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Text(
                    text = " ${data.current.temp_c} °c",
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White
                )
            }

            item {
                AsyncImage(
                    modifier = Modifier
                        .size(160.dp).fillMaxWidth()
                        .wrapContentWidth(align = Alignment.CenterHorizontally),
                    model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                    contentDescription = "Condition icon"
                )
            }

            item {
                Text(
                    text = data.current.condition.text,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            WeatherKeyVal("Humidity", data.current.humidity)
                            WeatherKeyVal("Wind Speed", data.current.wind_kph + " km/h")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            WeatherKeyVal("UV", data.current.uv)
                            WeatherKeyVal("Precipitation", data.current.precip_mm + " mm")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            WeatherKeyVal("Local Time", data.location.localtime.split(" ")[1])
                            WeatherKeyVal("Local Date", data.location.localtime.split(" ")[0])
                        }
                    }
                }
            }

            data.forecast?.forecastday?.let { forecastList ->
                item {
                    ForecastSection(forecastDays = forecastList)
                }
            }
        }
    }

}

@Composable
fun WeatherKeyVal(key : String, value : String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }
}

@Composable
fun ForecastSection(forecastDays: List<ForecastDay>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(forecastDays) { forecast ->
            Column(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .width(72.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = getDayOfWeek(forecast.date),
                    style = MaterialTheme.typography.bodySmall
                )
                AsyncImage(
                    model = "https:${forecast.day.condition.icon}",
                    contentDescription = forecast.day.condition.text,
                    modifier = Modifier.size(48.dp)
                )

                Text(
                    text = "${forecast.day.maxtemp_c.toFloatOrNull()?.toInt() ?: "--"}° / ${forecast.day.mintemp_c.toFloatOrNull()?.toInt() ?: "--"}°",
                    style = MaterialTheme.typography.labelSmall
                )

            }
        }
    }
}

fun getDayOfWeek(date: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val localDate = LocalDate.parse(date, formatter)
    return localDate.dayOfWeek.name.take(3) // e.g. MON
}
