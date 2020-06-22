package com.anguyen.weatherforecast_demo.presenters

import android.content.Context
import com.anguyen.weatherforecast_demo.commons.KEY_SETTING_SHARE_PREF
import com.anguyen.weatherforecast_demo.commons.KEY_SHARE_PREF_TEMP_UNIT
import com.anguyen.weatherforecast_demo.commons.get
import com.anguyen.weatherforecast_demo.commons.put
import com.anguyen.weatherforecast_demo.uis.setting.SettingView

class SettingPresenter(mContext: Context, private val mView: SettingView?) {
    private val sharePref = mContext.getSharedPreferences(KEY_SETTING_SHARE_PREF, Context.MODE_PRIVATE)

    fun saveTempUnitToSharePref(selectedUnit: String){
        sharePref.put(KEY_SHARE_PREF_TEMP_UNIT, selectedUnit)
        mView?.onSaved()
    }

    fun getTempUnitFromSharePref() = sharePref.get(KEY_SHARE_PREF_TEMP_UNIT, "")

}