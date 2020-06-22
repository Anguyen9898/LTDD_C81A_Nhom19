package com.anguyen.weatherforecast_demo.uis.selectedcities

import com.anguyen.weatherforecast_demo.models.CitiesModel

interface SelectedCitiesView {

    fun onGettingSuccess(callbackData: List<CitiesModel>?)

    fun onDataIsEmpty()

    fun showInternetError()

}