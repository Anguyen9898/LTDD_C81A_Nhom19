package com.anguyen.weatherforecast_demo.presenters

import android.content.Context
import com.anguyen.weatherforecast_demo.commons.*
import com.anguyen.weatherforecast_demo.uis.reoders.ReoderView

class ReOderPresenter(
    mContext: Context,
    private val mView: ReoderView?
) {

    private val sharePref = mContext.getSharedPreferences(
        KEY_SETTING_SHARE_PREF,
        Context.MODE_PRIVATE
    )

    fun saveOderToSharePref(selectedOrder: String){
        sharePref.put(KEY_SHARE_PREF_TEMP_ODER, selectedOrder)
        mView?.onSaved()
    }

    fun getOrderFromSharePref() = sharePref.get(KEY_SHARE_PREF_TEMP_ODER, "")

}