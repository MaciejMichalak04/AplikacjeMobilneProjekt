package com.example.weatherapp_final.data

import com.example.weatherapp_final.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    // Zapytanie o pogodę po współrzędnych
    @GET("data/2.5/weather")
    suspend fun getWeatherByGps(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): WeatherResponse

    // Zapytanie o pogodę po nazwie miasta
    @GET("data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): WeatherResponse
}