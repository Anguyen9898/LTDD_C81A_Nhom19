package com.anguyen.weatherforecast_demo.models

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

data class CurrentWeatherModel(
    var coord: JsonObject? = null,
    var weather: JsonArray? = null,
    var base: JsonPrimitive? = null,
    var main: JsonObject? = null,
    var wind: JsonObject? = null,
    var clouds: JsonObject? = null,
    var dt: JsonPrimitive? = null,
    var sys: JsonObject? = null,
    var timezone: JsonPrimitive? = null,
    var id: JsonPrimitive? = null,
    var name: JsonPrimitive? = null,
    var cod: JsonPrimitive? = null
){

    companion object{

        fun convertFromJsonObject(objResponded: JsonObject) = CurrentWeatherModel(
            objResponded["coord"].asJsonObject,
            objResponded["weather"].asJsonArray,
            objResponded["base"].asJsonPrimitive,
            objResponded["main"].asJsonObject,
            objResponded["wind"].asJsonObject,
            objResponded["clouds"].asJsonObject,
            objResponded["dt"].asJsonPrimitive,
            objResponded["sys"].asJsonObject,
            objResponded["timezone"].asJsonPrimitive,
            objResponded["id"].asJsonPrimitive,
            objResponded["name"].asJsonPrimitive,
            objResponded["cod"].asJsonPrimitive
        )

    }

}