package com.anguyen.weatherforecast_demo.repositories

import android.util.Log
import com.anguyen.weatherforecast_demo.utils.CallBackData
import com.anguyen.weatherforecast_demo.utils.ClientApi
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val tag = "WeatherData"
const val CODE_SUCCESS_RESPOND = 200

class WeatherApiRepositoryImp(private val mAppiKey: String): WeatherApiRepository {

    /**
     * This is api-testing function, disable it after project was finished
     */
    override fun testApi(callBackData: CallBackData<JsonObject>) {
        val clientApi = ClientApi()
        val bodyCall =  clientApi.weatherApiService()?.testApi()

//        bodyCall?.enqueue(object: Callback<JsonObject> {
//
//            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//
//                val code = response.code()
//
//                if(code == CODE_SUCCESS_RESPOND){
//                    if(response.body() != null) {
//                        //Logcat Writing
//                        Log.d(tag, response.body().toString())
//
//                    }else{
//                        Log.d(tag, mContext.getString(R.string.weather_api_error_message))
//                    }
//                }else{
//                    Log.d(tag, mContext.getString(R.string.weather_api_error_message))
//                }
//
//            }
//
//            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                //Logcat Writing
//                Log.d(tag, "Fail")
//                //Setup Result
//                Log.d(tag, t.message!!)
//            }
//
//        })
    }

    override fun getCurrentWeatherInfo(
        cityApiName: String,
        tempUnit: String?,
        callBackData: CallBackData<JsonObject>
    ) {

        val clientApi = ClientApi()
        val url = "data/2.5/weather?q=$cityApiName&units=$tempUnit&appid=$mAppiKey"
        val bodyCall =  clientApi.weatherApiService()?.getCurrentWeatherInfo(url)

        bodyCall?.enqueue(object: Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                Log.d(tag, response.code().toString())

                if(response.code() == CODE_SUCCESS_RESPOND){
                    if(response.body() != null) {
                        //Logcat Writing
                        Log.d(tag, response.body().toString())
                        //Setup Result
                        callBackData.onSuccess(response.body()!!)
                    }else{
                        callBackData.onFailure()
                    }
                }else{
                    callBackData.onFailure()
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                //Logcat Writing
                Log.d(tag, "Fail")
                //Setup Result
                callBackData.onFailure(t.message)
            }

        })

    }

    override fun getHourlyDailyWeatherInfo(
        coordinate: JsonObject?,
        tempUnit: String?,
        callBackData: CallBackData<JsonArray>
    ) {

        val clientApi = ClientApi()

        val lat = coordinate?.get("lat")?.asDouble.toString()
        val lon = coordinate?.get("lon")?.asDouble.toString()

        val url = "data/2.5/onecall?lat=$lat&lon=$lon&units=$tempUnit&appid=$mAppiKey"

        val bodyCall =  clientApi.weatherApiService()?.getHourlyDailyWeatherInfo(url)
        bodyCall?.enqueue(object: Callback<JsonObject>{

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                Log.d(tag, response.code().toString())

                if(response.code() == CODE_SUCCESS_RESPOND){

                    if(response.body() != null) {
                        val hourlyPart = response.body()?.get("hourly")?.asJsonArray!! //get daily part
                        val dailyPart = response.body()?.get("daily")?.asJsonArray!! //get daily part

                        //Logcat Writing
                        dailyPart.forEach{ Log.d(tag, it.asJsonObject.toString()) }
                        //Setup Result
                        callBackData.onSuccess(hourlyPart, dailyPart)

                    }else{
                        callBackData.onFailure()
                    }

                }else{
                    callBackData.onFailure()
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                //Logcat Writing
                Log.d(tag, "Fail")
                //Setup Result
                callBackData.onFailure(t.message)
            }

        })

    }

    override fun readCityListFile(callBackData: CallBackData<JsonArray>) {
        val clientApi = ClientApi()
        val bodyCall =  clientApi.weatherApiService()?.readCityListFile()

        bodyCall?.enqueue(object: Callback<JsonArray>{

            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                Log.d(tag, response.code().toString())

                if(response.code() == CODE_SUCCESS_RESPOND){

                    if(response.body() != null) {

                        val cityList = response.body()?.asJsonArray!!

                        //Logcat Writing
                        cityList.forEach{ Log.d(tag, it.asJsonObject.toString()) }
                        //Setup Result
                        callBackData.onSuccess(cityList)

                    }else{
                        callBackData.onFailure()
                    }

                }else{
                    callBackData.onFailure()
                }

            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                //Logcat Writing
                Log.d(tag, "Fail")
                //Setup Result
                callBackData.onFailure(t.message)
            }

        })
    }

}