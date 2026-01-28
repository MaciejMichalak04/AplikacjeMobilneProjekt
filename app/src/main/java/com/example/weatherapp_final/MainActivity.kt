package com.example.weatherapp_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp_final.screens.FavoritesScreen
import com.example.weatherapp_final.screens.WeatherScreen
import com.example.weatherapp_final.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: WeatherViewModel = viewModel() // ViewModel przeżywa zmianę ekranów

            NavHost(navController = navController, startDestination = "weather") {
                // Ekran 1: Pogoda
                composable("weather") {
                    WeatherScreen(navController = navController, viewModel = viewModel)
                }

                // Ekran 2: Ulubione (Baza Danych Room)
                composable("favorites") {
                    FavoritesScreen(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}