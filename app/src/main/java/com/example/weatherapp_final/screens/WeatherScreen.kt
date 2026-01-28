package com.example.weatherapp_final.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.weatherapp_final.viewmodel.WeatherViewModel
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(navController: NavController, viewModel: WeatherViewModel) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var cityQuery by remember { mutableStateOf("") }

    // Obserwujemy listę ulubionych bezpośrednio tutaj
    val favList by viewModel.favorites.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            getLocationAndFetchWeather(context, fusedLocationClient, viewModel)
        }
    }

    LaunchedEffect(Unit) {
        if (hasLocationPermission(context)) {
            getLocationAndFetchWeather(context, fusedLocationClient, viewModel)
        } else {
            permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather App") }
                // Usunęliśmy przycisk nawigacji do osobnego ekranu
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- 1. WYSZUKIWARKA ---
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = cityQuery,
                    onValueChange = { cityQuery = it },
                    label = { Text("Miasto") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { viewModel.loadWeatherByCity(cityQuery) }) {
                    Icon(Icons.Default.Search, "Szukaj")
                }
                IconButton(onClick = {
                    if (hasLocationPermission(context)) getLocationAndFetchWeather(context, fusedLocationClient, viewModel)
                    else permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
                }) {
                    Icon(Icons.Default.LocationOn, "GPS")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 2. LISTA ULUBIONYCH (NOWOŚĆ) ---
            if (favList.isNotEmpty()) {
                Text(
                    text = "Twoje ulubione:",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(favList) { city ->
                        // Kafelek z miastem
                        SuggestionChip(
                            onClick = {
                                // Kliknięcie w miasto ładuje pogodę
                                viewModel.loadWeatherByCity(city.name)
                            },
                            label = { Text(city.name) },
                            icon = {
                                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                            },
                            // Ikona usuwania (X) po prawej stronie
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 3. WYNIKI POGODY ---
            if (viewModel.isLoading) {
                CircularProgressIndicator()
            } else if (viewModel.error != null) {
                Text(text = viewModel.error!!, color = Color.Red)
            } else if (viewModel.weatherResult != null) {
                val weather = viewModel.weatherResult!!

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = weather.name, fontSize = 40.sp, fontWeight = FontWeight.Bold)

                    // Przycisk "Dodaj do ulubionych" (zmieniamy ikonę jeśli już jest na liście)
                    val isFavorite = favList.any { it.name == weather.name }
                    IconButton(onClick = {
                        if (isFavorite) {
                            // Opcjonalnie: usuwanie po kliknięciu serca (jeśli chcesz)
                            val cityToRemove = favList.first { it.name == weather.name }
                            viewModel.removeFavorite(cityToRemove)
                            Toast.makeText(context, "Usunięto z ulubionych", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.addFavorite(weather.name)
                            Toast.makeText(context, "Dodano do ulubionych", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Zapisz",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                }

                Text(text = "${weather.main.temp.toInt()}°C", fontSize = 64.sp, color = MaterialTheme.colorScheme.primary)
                Text(text = weather.weather.firstOrNull()?.description ?: "", fontSize = 20.sp)

                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Wilgotność: ${weather.main.humidity}%")
                        Text("Ciśnienie: ${weather.main.pressure} hPa")
                    }
                }
            }
        }
    }
}

// Funkcja pomocnicza do sprawdzania uprawnień
fun hasLocationPermission(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

// Funkcja pomocnicza do pobierania GPS
@SuppressLint("MissingPermission")
fun getLocationAndFetchWeather(
    context: Context,
    fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient,
    viewModel: WeatherViewModel
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            viewModel.loadWeatherByGps(location.latitude, location.longitude)
        } else {
            Toast.makeText(context, "Błąd GPS: Brak lokalizacji", Toast.LENGTH_SHORT).show()
        }
    }.addOnFailureListener {
        Toast.makeText(context, "Błąd GPS: ${it.message}", Toast.LENGTH_SHORT).show()
    }
}