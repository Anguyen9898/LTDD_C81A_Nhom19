package com.anguyen.weatherforecast_demo.rooms.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anguyen.weatherforecast_demo.models.CitiesModel
import com.anguyen.weatherforecast_demo.rooms.daos.SelectedCitiesDao
import com.anguyen.weatherforecast_demo.rooms.databases.SelectedCitiesDatabase.Companion.DATABASE_VERSION

@Database(entities = [CitiesModel::class], exportSchema = false, version = DATABASE_VERSION)
abstract class SelectedCitiesDatabase: RoomDatabase(){

    abstract fun selectedCitiesDao(): SelectedCitiesDao

    companion object{
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "selected-cities-database"
        var INSTANCE: SelectedCitiesDatabase? = null

        fun getRiderInstance(context: Context): SelectedCitiesDatabase? {
            if(INSTANCE == null){
                synchronized (SelectedCitiesDao::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        SelectedCitiesDatabase::class.java,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }
    }

}