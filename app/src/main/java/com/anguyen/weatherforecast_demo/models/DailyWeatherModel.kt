package com.anguyen.weatherforecast_demo.models

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

data class DailyWeatherModel(
    var dt: JsonPrimitive? = null,
    var sunrise: JsonPrimitive? = null,
    var sunset: JsonPrimitive? = null,
    var temp: JsonObject? = null,
    var realFeel: JsonObject? = null,
    var pressure: JsonPrimitive? = null,
    var humidity: JsonPrimitive? = null,
    var dewPoint: JsonPrimitive? = null,
    var windSpeed: JsonPrimitive? = null,
    var windDeg: JsonPrimitive? = null,
    var weather: JsonArray? = null,
    var clouds: JsonPrimitive? = null,
    //var rain: JsonPrimitive? = null,
    var uvi: JsonPrimitive? = null
){
    companion object {

        fun convertFromJson(dailyWeatherJson: JsonArray): ArrayList<DailyWeatherModel>{
            val dailyDetails = ArrayList<DailyWeatherModel>()

            dailyWeatherJson.asJsonArray.forEach { jsonElement ->

                val dailyElement = jsonElement.asJsonObject

                dailyDetails.add(
                    DailyWeatherModel(
                        dailyElement["dt"].asJsonPrimitive,
                        dailyElement["sunrise"].asJsonPrimitive,
                        dailyElement["sunset"].asJsonPrimitive,
                        dailyElement["temp"].asJsonObject,
                        dailyElement["feels_like"].asJsonObject,
                        dailyElement["pressure"].asJsonPrimitive,
                        dailyElement["humidity"].asJsonPrimitive,
                        dailyElement["dew_point"].asJsonPrimitive,
                        dailyElement["wind_speed"].asJsonPrimitive,
                        dailyElement["wind_deg"].asJsonPrimitive,
                        dailyElement["weather"].asJsonArray,
                        dailyElement["clouds"].asJsonPrimitive,
                        //dailyElement["rain"].asJsonPrimitive,
                        dailyElement["uvi"].asJsonPrimitive
                    )
                )

            }

            return dailyDetails
        }

    }
}