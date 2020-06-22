package com.anguyen.weatherforecast_demo.uis.mains

import com.anguyen.weatherforecast_demo.models.CitiesModel
import com.anguyen.weatherforecast_demo.models.CurrentWeatherModel

interface MainView {

    fun showInternetError()

    fun onSelectedCitiesGettingSuccess(citiesModels: List<CitiesModel>)

    fun updateUI(currentWeatherModel: CurrentWeatherModel?, dataOrder: Int)

    fun showDataGettingFailure(message: String?)

}