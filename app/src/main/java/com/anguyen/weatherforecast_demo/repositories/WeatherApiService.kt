package com.anguyen.weatherforecast_demo.repositories

import com.anguyen.weatherforecast_demo.utils.ConfigApi
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface WeatherApiService {

    @GET(ConfigApi.Api.SAMPLE_WEATHER)
    fun testApi(): Call<JsonObject>

    @GET
    fun getCurrentWeatherInfo(@Url urlReadApi: String): Call<JsonObject>

    @GET
    fun getHourlyDailyWeatherInfo(@Url urlReadApi: String): Call<JsonObject>

    @GET(ConfigApi.Api.CITY_LIST_FILE_PATH)
    fun readCityListFile(): Call<JsonArray>

}