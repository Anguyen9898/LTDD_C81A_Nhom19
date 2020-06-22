package com.anguyen.weatherforecast_demo.uis.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.anguyen.weatherforecast_demo.R
import com.anguyen.weatherforecast_demo.commons.onClick
import com.anguyen.weatherforecast_demo.presenters.SettingPresenter
import com.anguyen.weatherforecast_demo.uis.abouts.AboutActivity
import com.anguyen.weatherforecast_demo.uis.reoders.ReorderActivity
import kotlinx.android.synthetic.main.activity_setting.*

const val tag = "setting's-share-pref"

class SettingActivity : AppCompatActivity(), SettingView {

    private var isSelectorVisible = false
    private var selectedTempUnit = "metric"

    private lateinit var mPresenter: SettingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setupAll()
    }

    private fun setupAll(){
        //Initialize presenter
        mPresenter = SettingPresenter(this, this)

        //Get selected temperature unit from before
        selectedTempUnit = mPresenter.getTempUnitFromSharePref()!!

        if(selectedTempUnit == "" || selectedTempUnit == "metric"){
            txt_selected_unit.text = txt_metric.text.toString()
        }else{
            txt_selected_unit.text = txt_imperial.text.toString()
        }

        //Click Handler
        main_container.onClick { //Close unit selector if it's opening while clicking anywhere
            if(isSelectorVisible){
                card_unit_selector.visibility = View.INVISIBLE
                isSelectorVisible = false
            }
        }

        btn_back.onClick { onBackPressed() }

        container_unit.onClick {
            if(isSelectorVisible){
                card_unit_selector.visibility = View.INVISIBLE
            }else{
                card_unit_selector.visibility = View.VISIBLE
            }
            isSelectorVisible = !isSelectorVisible
        }

        txt_metric.onClick {
            //Set new selected unit after selector closed
            txt_selected_unit.text = txt_metric.text.toString()

            //Set value to save to storage
            selectedTempUnit = "metric"

            //Close selector
            card_unit_selector.visibility = View.INVISIBLE
            isSelectorVisible = false
        }

        txt_imperial.onClick {
            //Set new selected unit after selector closed
            txt_selected_unit.text = txt_imperial.text.toString()

            //Set value to save to storage
            selectedTempUnit = "imperial"

            //Close selector
            card_unit_selector.visibility = View.INVISIBLE
            isSelectorVisible = false
        }

        container_reorder.onClick { startActivity(Intent(this, ReorderActivity::class.java)) }

        container_about.onClick { startActivity(Intent(this, AboutActivity::class.java)) }

    }

//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        if(isSelectorVisible){ //Turn unit selector off if it is open
//            card_unit_selector.visibility = View.INVISIBLE
//            isSelectorVisible = false
//        }
//        return super.dispatchTouchEvent(ev)
//    }

    override fun onBackPressed() {
        if(isSelectorVisible){ //Turn unit selector off if it is open
            card_unit_selector.visibility = View.INVISIBLE
            isSelectorVisible = false
        }else{
            mPresenter.saveTempUnitToSharePref(selectedTempUnit)
            super.onBackPressed()
        }
    }

    override fun onSaved() {
        Log.d(tag, mPresenter.getTempUnitFromSharePref().toString())
    }

}