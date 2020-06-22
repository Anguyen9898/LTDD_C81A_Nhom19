package com.anguyen.weatherforecast_demo.uis.mains

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.get
import com.anguyen.weatherforecast_demo.R
import com.anguyen.weatherforecast_demo.adapters.viewpagertabs.MainViewPagerTabsAdapter
import com.anguyen.weatherforecast_demo.commons.*
import com.anguyen.weatherforecast_demo.models.CitiesModel
import com.anguyen.weatherforecast_demo.models.CurrentWeatherModel
import com.anguyen.weatherforecast_demo.presenters.MainPresenter
import com.anguyen.weatherforecast_demo.uis.selectedcities.SelectedCitiesActivity
import com.anguyen.weatherforecast_demo.uis.setting.SettingActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_weather_tabs_item.*

class MainActivity : AppCompatActivity(),
    MainView {

    private lateinit var progressDialog: KProgressHUD

    private var mPresenter: MainPresenter? = null
    private var mMainAdapter: MainViewPagerTabsAdapter? = null

    private var mCurrentData: Array<CurrentWeatherModel?>? = null
    private lateinit var mCitiesModels: List<CitiesModel>

    private var cityViewPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressDialog = initProgressDialog(this)
        progressDialog.show()

        setupAll()
    }

    override fun onResume() {
        super.onResume()
        //Update if setting was changed
        setupAll()
    }

    private fun setupAll(){
        //Call Current Weather Presenter
        mPresenter = MainPresenter(this, this)
        mPresenter?.getSelectedCitesFromDatabase()

        //Click Handler
        img_menu.onClick { startActivity(Intent(this, SelectedCitiesActivity::class.java)) }
        img_setting.onClick { startActivity(Intent(this, SettingActivity::class.java)) }

        //Get user's selected city clicked from SelectedCitiesActivity
        if(intent?.extras?.getInt(SelectedCitiesActivity.KEY_VIEW_POS) != null) {
            cityViewPos = intent?.extras?.getInt( SelectedCitiesActivity.KEY_VIEW_POS)!!
        }

    }

//    private fun orderHandler(){
//        if()
//    }

    override fun showInternetError() {
        progressDialog.dismiss()
        showInternetErrorDialog()
    }

    override fun onSelectedCitiesGettingSuccess(citiesModels: List<CitiesModel>) {
        mCitiesModels = citiesModels

        //Saving index item to make sure adapter is initialized right order
        var index = 0
        while (index < mCitiesModels.size){
            mPresenter?.readCurrentWeatherApi(mCitiesModels[index].cityApiKey, index)
            index++
        }
    }

    override fun updateUI(currentWeatherModel: CurrentWeatherModel?, dataOrder: Int) {

        //Setup Current Weather UI
        if (currentWeatherModel == null){
            startActivity(Intent(this, SelectedCitiesActivity::class.java))
            return
        }

        //Setup Current Weather Data (Use array in order to make sure adapter is initialized right order)
        if(mCurrentData == null){
            mCurrentData = Array(mCitiesModels.size) { currentWeatherModel }
        }else{
            mCurrentData!![dataOrder] = currentWeatherModel
        }

        //Setup adapter
        if (mMainAdapter == null){
            mMainAdapter = MainViewPagerTabsAdapter(this, mCurrentData!!, mPresenter!!, mCitiesModels)
            view_container.adapter = mMainAdapter
        }else{
            mMainAdapter?.notifyDataSetChanged()
        }

        //Move to page which was selected from SelectedCitiesActivity by user
        //Only active if adapter have been setup completely
        if(mMainAdapter?.itemCount == mCurrentData?.size){
            view_container.setCurrentItem(cityViewPos, false)

//            //Scroll to top if user slide to next page
//            view_container.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//                    mMainAdapter?.getItemView()?.findViewById<ScrollView>(R.id.scroll_container)?.fullScroll(ScrollView.FOCUS_UP)
//                }
//            })
        }

        //Attach to TabLayout
        TabLayoutMediator(tab_container, view_container){ _, _ -> }.attach()

        //Close progress dialog
        progressDialog.dismiss()

    }

    override fun showDataGettingFailure(message: String?) {
        progressDialog.dismiss()
        showDataGettingFailed(message!!)
    }


}
