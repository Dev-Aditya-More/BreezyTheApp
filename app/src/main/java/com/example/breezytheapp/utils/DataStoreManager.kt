package com.example.breezytheapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
object DataStoreManager {
    private val CITY_KEY = stringPreferencesKey("city")

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "weather_prefs")

    suspend fun saveCity(context: Context, city: String) {
        context.dataStore.edit { prefs ->
            prefs[CITY_KEY] = city
        }
    }

    fun readCity(context: Context): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[CITY_KEY] ?: "Delhi" // default fallback
        }
    }
}
