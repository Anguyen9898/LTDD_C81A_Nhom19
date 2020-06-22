package com.anguyen.weatherforecast_demo.presenters

import android.content.Context
import com.anguyen.weatherforecast_demo.models.CitiesModel
import com.anguyen.weatherforecast_demo.rooms.managers.SelectedCitiesManager
import com.anguyen.weatherforecast_demo.uis.starts.StartView

class StartPresenter(
    mContext: Context,
    private val mView: StartView?
) {

    private val mManager = SelectedCitiesManager(mContext)

    fun updateUI(){
        mManager.getAllSelectedCites(object: SelectedCitiesManager.OnDataCallback{

            override fun onSuccess(citiesModels: List<CitiesModel>?) {
                if(citiesModels?.isEmpty()!!){
                    mView?.jumpToCitySelectorUI()
                }else{
                    mView?.openMainUI()
                }
            }

            override fun onFail() {
                mView?.jumpToCitySelectorUI()
            }

        })

    }

}