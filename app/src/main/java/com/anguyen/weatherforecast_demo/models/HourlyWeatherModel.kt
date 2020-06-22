package com.anguyen.weatherforecast_demo.models

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

data class HourlyWeatherModel(
    var dt: JsonPrimitive? = null,
    var temp: JsonPrimitive? = null,
    var weather: JsonArray? = null
//    var realFeel: JsonPrimitive? = null,
//    var pressure: JsonPrimitive? = null,
//    var humidity: JsonPrimitive? = null,
//    var dewPoint: JsonPrimitive? = null,
//    var windSpeed: JsonPrimitive? = null,
//    var windDeg: JsonPrimitive? = null,
//    var clouds: JsonPrimitive? = null
){
    companion object {

        fun convertFromJson(hourlyWeatherJson: JsonArray): ArrayList<HourlyWeatherModel>{
            val hourDetails = ArrayList<HourlyWeatherModel>()

            hourlyWeatherJson.asJsonArray.forEach { jsonElement ->

                val hourElement = jsonElement.asJsonObject

                hourDetails.add(
                    HourlyWeatherModel(
                        hourElement["dt"].asJsonPrimitive,
                        hourElement["temp"].asJsonPrimitive,
                        hourElement["weather"].asJsonArray
//                        hourElement["feels_like"].asJsonPrimitive,
//                        hourElement["pressure"].asJsonPrimitive,
//                        hourElement["humidity"].asJsonPrimitive,
//                        hourElement["dew_point"].asJsonPrimitive,
//                        hourElement["wind_speed"].asJsonPrimitive,
//                        hourElement["wind_deg"].asJsonPrimitive,
//                        hourElement["clouds"].asJsonPrimitive
                    )
                )

            }

            return hourDetails
        }

    }
}