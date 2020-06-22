package com.anguyen.weatherforecast_demo.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anguyen.weatherforecast_demo.rooms.databases.SelectedCitiesDatabase
import com.anguyen.weatherforecast_demo.rooms.databases.SelectedCitiesDatabase.Companion.DATABASE_NAME
import com.google.gson.JsonObject
import java.io.Serializable

@Entity (tableName = DATABASE_NAME)
data class CitiesModel(
    @PrimaryKey (autoGenerate = false)
    var cityApiKey: String,
    @ColumnInfo (name = "city_name")
    val cityName: String = "",
    @ColumnInfo (name = "city_coordinate")
    val coordinate: String = "",
    @ColumnInfo (name = "temperature")
    val temp: String = ""
)

