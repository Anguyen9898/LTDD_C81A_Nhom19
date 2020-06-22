package com.anguyen.weatherforecast_demo.rooms.daos

import androidx.room.*
import com.anguyen.weatherforecast_demo.models.CitiesModel

@Dao
interface SelectedCitiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Throws(RuntimeException::class)
    fun insertCity(vararg cities: CitiesModel?)

    @Query("SELECT * FROM `selected-cities-database`")
    fun getAllSelectedCites(): List<CitiesModel>

    @Query("DELETE FROM `selected-cities-database` WHERE cityApiKey = :cityId")
    fun removeCity(cityId: String)

    @Query("DELETE FROM `selected-cities-database`")
    fun clearAllCities()

}