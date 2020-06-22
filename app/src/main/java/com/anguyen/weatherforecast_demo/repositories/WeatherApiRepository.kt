package com.anguyen.weatherforecast_demo.repositories

import com.anguyen.weatherforecast_demo.utils.CallBackData
import com.google.gson.JsonArray
import com.google.gson.JsonObject

interface WeatherApiRepository {

    fun testApi(callBackData: CallBackData<JsonObject>)

    fun getCurrentWeatherInfo(cityApiName: String, tempUnit: String?, callBackData: CallBackData<JsonObject>)

    fun getHourlyDailyWeatherInfo(coordinate: JsonObject?, tempUnit: String?, callBackData: CallBackData<JsonArray>)

    fun readCityListFile(callBackData: CallBackData<JsonArray>)

}