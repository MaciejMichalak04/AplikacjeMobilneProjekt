package com.example.weatherapp_final.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_city_table")
data class FavoriteCity(
    @PrimaryKey
    val name: String // Nazwa miasta będzie kluczem (nie można dodać dwa razy tego samego)
)