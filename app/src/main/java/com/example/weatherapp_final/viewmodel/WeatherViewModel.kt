package com.example.weatherapp_final.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp_final.data.RetrofitInstance
import com.example.weatherapp_final.data.WeatherDatabase
import com.example.weatherapp_final.model.FavoriteCity
import com.example.weatherapp_final.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var weatherResult by mutableStateOf<WeatherResponse?>(null)

    private val db = WeatherDatabase.getDatabase(application)
    private val dao = db.weatherDao()

    // Lista ulubionych (używamy Flow, żeby UI samo się odświeżało)
    private val _favorites = MutableStateFlow<List<FavoriteCity>>(emptyList())
    val favorites = _favorites.asStateFlow()

    init {
        // Przy starcie zaczynamy obserwować bazę danych
        viewModelScope.launch {
            dao.getFavorites().collect { list ->
                _favorites.value = list
            }
        }
    }

    // Dodaj do ulubionych
    fun addFavorite(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(FavoriteCity(city))
        }
    }

    // Usuń z ulubionych
    fun removeFavorite(city: FavoriteCity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(city)
        }
    }

    // Funkcje API
    fun loadWeatherByGps(lat: Double, lon: Double) {
        viewModelScope.launch {
            isLoading = true; error = null
            try {
                val response = RetrofitInstance.api.getWeatherByGps(lat, lon, apiKey = RetrofitInstance.API_KEY)
                weatherResult = response
            } catch (e: Exception) {
                error = "Błąd: ${e.localizedMessage}"
            } finally { isLoading = false }
        }
    }

    fun loadWeatherByCity(city: String) {
        viewModelScope.launch {
            isLoading = true; error = null
            try {
                val response = RetrofitInstance.api.getWeatherByCity(city, apiKey = RetrofitInstance.API_KEY)
                weatherResult = response
            } catch (e: Exception) {
                error = "Nie znaleziono miasta."
            } finally { isLoading = false }
        }
    }
}