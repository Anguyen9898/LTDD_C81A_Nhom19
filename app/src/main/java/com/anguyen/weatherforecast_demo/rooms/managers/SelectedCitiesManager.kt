package com.anguyen.weatherforecast_demo.rooms.managers

import android.content.Context
import android.os.AsyncTask
import com.anguyen.weatherforecast_demo.models.CitiesModel
import com.anguyen.weatherforecast_demo.rooms.daos.SelectedCitiesDao
import com.anguyen.weatherforecast_demo.rooms.databases.SelectedCitiesDatabase
import java.lang.RuntimeException

class SelectedCitiesManager(mContext: Context) {

    private val mSelectedCitiesDao: SelectedCitiesDao

    init {
        val selectedCitiesDatabase = SelectedCitiesDatabase.getRiderInstance(mContext)!!
        mSelectedCitiesDao = selectedCitiesDatabase.selectedCitiesDao()
    }

    interface OnDataCallback{
        fun onSuccess(citiesModels: List<CitiesModel>?)
        fun onFail()
    }

    fun insertCity(city: CitiesModel, listener: OnDataCallback){
        val insertCityAsync = InsertCityAsync(mSelectedCitiesDao, listener)
        insertCityAsync.execute(city)
    }

    private class InsertCityAsync(
        private val mSelectedCitiesDao: SelectedCitiesDao,
        private val listener: OnDataCallback
    ): AsyncTask<CitiesModel, Unit, Unit>(){

        private var callbackResult: CitiesModel? = null

        override fun doInBackground(vararg cities: CitiesModel?): Unit? {
            callbackResult = try {
                mSelectedCitiesDao.insertCity(*cities)
                cities[0]!!
            }catch (ex: RuntimeException){ //Catch choosing selected city error
                null
            }

            return null
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            if(callbackResult == null){
                listener.onFail()
            }else{
                listener.onSuccess(listOf(callbackResult!!))
            }
        }

    }

    fun getAllSelectedCites(listener: OnDataCallback){
        val getAllSelectedCitesAsync = GetAllSelectedCitesAsync(mSelectedCitiesDao, listener)
        getAllSelectedCitesAsync.execute()
    }

    private class GetAllSelectedCitesAsync(
        private val mSelectedCitiesDao: SelectedCitiesDao,
        private val listener: OnDataCallback
    ): AsyncTask<List<CitiesModel>, Unit, Unit>(){

        private var callbackResult: List<CitiesModel>? = null
        private var mSize = 0

        override fun doInBackground(vararg cities: List<CitiesModel>?): Unit? {
            val result = mSelectedCitiesDao.getAllSelectedCites()
            callbackResult = result
            mSize = result.size
            return null
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            if(callbackResult == null || mSize == 0 ){
                listener.onFail()
            }else{
                listener.onSuccess(callbackResult!!)
            }
        }

    }

    fun removeCity(cityId: String){
        val removeCityAsync = RemoveCityAsync(mSelectedCitiesDao)
        removeCityAsync.execute(cityId)
    }

    private class RemoveCityAsync(private val mSelectedCitiesDao: SelectedCitiesDao): AsyncTask<String, Unit, Unit>(){
        override fun doInBackground(vararg cityId: String?): Unit?{
            mSelectedCitiesDao.removeCity(cityId[0]!!)
            return null
        }
    }

}