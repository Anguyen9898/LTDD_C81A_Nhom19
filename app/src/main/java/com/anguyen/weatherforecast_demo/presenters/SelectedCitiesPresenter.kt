package com.anguyen.weatherforecast_demo.presenters

import android.content.Context
import android.util.Log
import com.anguyen.weatherforecast_demo.R
import com.anguyen.weatherforecast_demo.commons.KEY_SETTING_SHARE_PREF
import com.anguyen.weatherforecast_demo.commons.KEY_SHARE_PREF_TEMP_UNIT
import com.anguyen.weatherforecast_demo.commons.get
import com.anguyen.weatherforecast_demo.commons.isNetworkConnected
import com.anguyen.weatherforecast_demo.models.CitiesModel
import com.anguyen.weatherforecast_demo.models.CurrentWeatherModel
import com.anguyen.weatherforecast_demo.repositories.WeatherApiRepository
import com.anguyen.weatherforecast_demo.repositories.WeatherApiRepositoryImp
import com.anguyen.weatherforecast_demo.rooms.managers.SelectedCitiesManager
import com.anguyen.weatherforecast_demo.uis.selectedcities.SelectedCitiesView
import com.anguyen.weatherforecast_demo.utils.CallBackData
import com.google.gson.JsonObject

class SelectedCitiesPresenter(
    private val mContext: Context,
    private val mView: SelectedCitiesView?
) {
    private val weatherApiRepository: WeatherApiRepository = WeatherApiRepositoryImp(
        mContext.getString(R.string.open_weather_api_key)
    )

    private val sharePref = mContext.getSharedPreferences(KEY_SETTING_SHARE_PREF, Context.MODE_PRIVATE)

    private val mManager = SelectedCitiesManager(mContext)

    fun getSelectedCitiesFromStorage(){

        mManager.getAllSelectedCites(object: SelectedCitiesManager.OnDataCallback{

            override fun onSuccess(citiesModels: List<CitiesModel>?) {
                Log.d("SelectedRespond", citiesModels.toString())
                mView?.onGettingSuccess(citiesModels)
            }

            override fun onFail() {
                Log.d("SelectedRespond", "fail")
                mView?.onDataIsEmpty()
            }

        })
    }

    fun readCurrentWeatherApi(cityApiKey: String, onGettingData: (CurrentWeatherModel?) -> Unit){

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

        weatherApiRepository.getCurrentWeatherInfo(cityApiKey, tempUnit,  object:
            CallBackData<JsonObject> {

            override fun onSuccess(vararg repsondData: JsonObject) {
                val currentData = CurrentWeatherModel.convertFromJsonObject(repsondData[0])
                onGettingData(currentData)
            }

            override fun onFailure(vararg message: String?) {
                onGettingData(null)
            }

        })

    }

    fun removeCity(cityId: String){
        mManager.removeCity(cityId)
    }

}