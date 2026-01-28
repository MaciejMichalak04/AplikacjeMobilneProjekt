package com.example.weatherapp_final.data

import android.content.Context
import androidx.room.*
import com.example.weatherapp_final.model.FavoriteCity
import kotlinx.coroutines.flow.Flow

// 1.  Interfejs do sterowania bazą
@Dao
interface WeatherDao {
    @Query("SELECT * FROM fav_city_table")
    fun getFavorites(): Flow<List<FavoriteCity>> // Flow sam odświeża listę

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: FavoriteCity)

    @Delete
    suspend fun delete(city: FavoriteCity)
}

// 2. Baza Danych
@Database(entities = [FavoriteCity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        fun getDatabase(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}