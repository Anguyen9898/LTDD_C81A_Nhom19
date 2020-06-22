package com.anguyen.weatherforecast_demo.presenters

import android.content.Context
import com.anguyen.weatherforecast_demo.R
import com.anguyen.weatherforecast_demo.commons.*
import com.anguyen.weatherforecast_demo.models.CitiesModel
import com.anguyen.weatherforecast_demo.models.CurrentWeatherModel
import com.anguyen.weatherforecast_demo.models.DailyWeatherModel
import com.anguyen.weatherforecast_demo.models.HourlyWeatherModel
import com.anguyen.weatherforecast_demo.repositories.WeatherApiRepository
import com.anguyen.weatherforecast_demo.repositories.WeatherApiRepositoryImp
import com.anguyen.weatherforecast_demo.rooms.managers.SelectedCitiesManager
import com.anguyen.weatherforecast_demo.utils.CallBackData
import com.anguyen.weatherforecast_demo.uis.mains.MainView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class MainPresenter(
    private val mContext: Context,
    private val mView: MainView?
) {
    private val weatherApiRepository: WeatherApiRepository = WeatherApiRepositoryImp(
        mContext.getString(R.string.open_weather_api_key)
    )

    private val sharePref = mContext.getSharedPreferences(KEY_SETTING_SHARE_PREF, Context.MODE_PRIVATE)

    private val mManager = SelectedCitiesManager(mContext)


    /**
     * This is api-testing function, disable it after project was finished
     */
    fun testApi(){
//        weatherApiRepository.testApi(object: CallBackData<JsonObject> {
//            override fun onSuccess(respondedData: JsonObject) {}
//            override fun onFailure(vararg message: String?) {}
//        })
    }


    fun getOderSetting() = sharePref.get(KEY_SHARE_PREF_TEMP_ODER, "")

    fun getSelectedCitesFromDatabase(){
        //Read current weather api
        mManager.getAllSelectedCites(object: SelectedCitiesManager.OnDataCallback{
            override fun onSuccess(citiesModels: List<CitiesModel>?) {
                //Check if data is empty
                if(citiesModels == null) {
                    //mView?.updateUI("",null)
                    return
                }else{
                    mView?.onSelectedCitiesGettingSuccess(citiesModels)
                }

            }
            override fun onFail() {
                mView?.showDataGettingFailure(mContext.getString(R.string.empty_database))
            }
        })

    }

    fun readCurrentWeatherApi(cityApiKey: String, dataOrder: Int){

        //Check Internet Service
        if (!isNetworkConnected(mContext)!!){
            mView?.showInternetError()
            return
        }

        var tempUnit = sharePref.get(KEY_SHARE_PREF_TEMP_UNIT, "")

        //Default unit
        if (tempUnit ==""){
            tempUnit = "metric"
        }

        weatherApiRepository.getCurrentWeatherInfo(cityApiKey, tempUnit,  object: CallBackData<JsonObject>{

                override fun onSuccess(vararg repsondData: JsonObject) {
                    val currentData = CurrentWeatherModel.convertFromJsonObject(repsondData[0])
                    mView?.updateUI(currentData, dataOrder)
                }

                override fun onFailure(vararg message: String?) {
                    if(message.isEmpty()){
                        //Other failures
                        mView?.showDataGettingFailure(mContext.getString(R.string.weather_api_error_message))
                    }else{
                        //ClientApi enqueue failure
                        mView?.showDataGettingFailure(message[0])
                    }
                }

            })

    }

    fun readHourlyDailyWeatherApi(
        coordinate: String,
        onGettingData: (ArrayList<HourlyWeatherModel>?, ArrayList<DailyWeatherModel>?) -> Unit){

        val coordinateJsonObject = JsonParser().parse(coordinate) as JsonObject
        var tempUnit = sharePref.get(KEY_SHARE_PREF_TEMP_UNIT, "")

        //Default unit
        if (tempUnit ==""){
            tempUnit = "metric"
        }

        weatherApiRepository.getHourlyDailyWeatherInfo(coordinateJsonObject, tempUnit, object: CallBackData<JsonArray> {

            override fun onSuccess(vararg repsondData: JsonArray) {
                val hourlyWeatherModels = HourlyWeatherModel.convertFromJson(repsondData[0])
                val dailyWeatherModels = DailyWeatherModel.convertFromJson(repsondData[1])
                onGettingData(hourlyWeatherModels, dailyWeatherModels)
            }

            override fun onFailure(vararg message: String?) {
                if(message.isEmpty()){
                    //Other failures
                    mView?.showDataGettingFailure(mContext.getString(R.string.weather_api_error_message))
                }else{
                    //ClientApi enqueue failure
                    mView?.showDataGettingFailure(message[0])
                }
            }

        })

    }

}