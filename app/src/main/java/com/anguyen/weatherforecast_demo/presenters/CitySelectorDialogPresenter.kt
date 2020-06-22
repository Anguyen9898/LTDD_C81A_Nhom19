package com.anguyen.weatherforecast_demo.presenters

import android.content.Context
import android.net.ParseException
import android.util.Log
import com.anguyen.weatherforecast_demo.R
import com.anguyen.weatherforecast_demo.models.CitiesModel
import com.anguyen.weatherforecast_demo.repositories.WeatherApiRepository
import com.anguyen.weatherforecast_demo.repositories.WeatherApiRepositoryImp
import com.anguyen.weatherforecast_demo.rooms.managers.SelectedCitiesManager
import com.anguyen.weatherforecast_demo.uis.cityselector.CitySelectorDialogView
import com.anguyen.weatherforecast_demo.utils.CallBackData
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.io.FileNotFoundException
import java.io.IOException

class CitySelectorDialogPresenter(
    private val mContext: Context,
    private val mView: CitySelectorDialogView?
){

//    private val weatherApiRepository: WeatherApiRepository = WeatherApiRepositoryImp(
//        mContext.getString(R.string.open_weather_api_key)
//    )
    private val mManager = SelectedCitiesManager(mContext)

//    fun readCiyListFile(){
//        weatherApiRepository.readCityListFile(object: CallBackData<JsonArray>{
//            override fun onSuccess(vararg respondData: JsonArray) {
//                mView?.onGetAllCitiesSuccess(respondData[0])
//            }
//
//            override fun onFailure(vararg message: String?) {
//                if(message.isEmpty()){
//                    //Other failures
//                    mView?.onGetDataFailure(mContext.getString(R.string.general_error_message))
//                }else{
//                    //ClientApi enqueue failure
//                    mView?.onGetDataFailure(message[0])
//                }
//            }
//        })
//    }

    private fun getCitiesJsonData(onResult: (String) -> Unit){
        try {
            val inputStream = mContext.assets.open("city.list.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)

            inputStream.read(buffer)
            inputStream.close()

            onResult(String(buffer, charset("UTF-8")))

        } catch (ex: IOException){
            mView?.onGetDataFailure(ex.message)
        }
    }


    fun getAllCities(){

       getCitiesJsonData { dataCallback ->
           try {
               mView?.onGetAllCitiesSuccess(JsonParser().parse(dataCallback).asJsonArray)
           }catch (ex: FileNotFoundException){
               mView?.onGetDataFailure(ex.message)
           }catch (ex: IOException){
               mView?.onGetDataFailure(ex.message)
           }catch (ex: ParseException){
               mView?.onGetDataFailure(ex.message)
           }
       }

    }

    fun saveCityToDatabase(cityDetail: CitiesModel){
        mManager.insertCity(cityDetail, object: SelectedCitiesManager.OnDataCallback{
            override fun onSuccess(citiesModels: List<CitiesModel>?) {
                Log.d("Selector", citiesModels.toString())
                mView?.onCityInfoSavingSuccess()
            }

            override fun onFail() {
                mView?.onCityInfoSavingFailure()
            }
        })
    }

}