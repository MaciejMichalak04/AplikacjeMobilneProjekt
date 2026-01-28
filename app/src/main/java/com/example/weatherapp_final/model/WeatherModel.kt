package com.example.weatherapp_final.model

// To jest główna odpowiedź z serwera
data class WeatherResponse(
    val name: String,         // Nazwa miasta
    val main: Main,           // Sekcja z temperaturą
    val weather: List<Weather>, // Sekcja z opisem pogody
    val coord: Coord          // Współrzędne
)

data class Main(
    val temp: Double,      // Temperatura
    val humidity: Int,     // Wilgotność
    val pressure: Int      // Ciśnienie
)

data class Weather(
    val description: String, // Np. "clear sky"
    val icon: String         // Kod ikonki, np. "10d"
)

data class Coord(
    val lat: Double,
    val lon: Double
)