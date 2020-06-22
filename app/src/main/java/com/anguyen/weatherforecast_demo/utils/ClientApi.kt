package com.anguyen.weatherforecast_demo.utils

import com.anguyen.weatherforecast_demo.repositories.WeatherApiService

class ClientApi: BaseApi(){
    fun weatherApiService(): WeatherApiService?{
        return this.getService(WeatherApiService::class.java, ConfigApi.BASE_URL)
    }
}