package com.anguyen.weatherforecast_demo.adapters.viewpagertabs

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anguyen.weatherforecast_demo.R
import com.anguyen.weatherforecast_demo.adapters.hourlyweather.HourWeatherRecycleAdapter
import com.anguyen.weatherforecast_demo.adapters.dailyweather.DailyWeatherRecycleAdapter
import com.anguyen.weatherforecast_demo.commons.*
import com.anguyen.weatherforecast_demo.models.CitiesModel
import com.anguyen.weatherforecast_demo.models.CurrentWeatherModel
import com.anguyen.weatherforecast_demo.presenters.MainPresenter
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.main_weather_tabs_item.view.*
import kotlinx.android.synthetic.main.main_weather_tabs_item.view.container_daily
import kotlinx.android.synthetic.main.main_weather_tabs_item.view.container_detail
import kotlinx.android.synthetic.main.main_weather_tabs_item.view.container_hourly
import kotlinx.android.synthetic.main.main_weather_tabs_item.view.container_overview
import java.text.SimpleDateFormat
import java.util.*

@Suppress("UNUSED_CHANGED_VALUE")
class MainViewPagerTabsAdapter(
    private val mContext: Context,
    private val mWeatherData: Array<CurrentWeatherModel?>,
    private val mPresenter: MainPresenter,
    private val mCityModels: List<CitiesModel>
): RecyclerView.Adapter<MainViewPagerTabsAdapter.ViewHolder>(){

    private var itemView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.main_weather_tabs_item, parent, false))
    }

    override fun getItemCount(): Int = mWeatherData.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        itemView = holder.itemView

        val weatherData = mWeatherData[i]
//        val tabId = mCityTabIDs[i]
//
//        holder.txtViewID.text = tabId
//        cityViewIDRespond.getCityViewIdAndPos(holder.txtViewID.text.toString(), i)

        //Read JsonObject
        val weatherElement = weatherData?.weather?.get(0)?.asJsonObject
        val mainElement = weatherData?.main?.asJsonObject
        val sysElement = weatherData?.sys?.asJsonObject

        //Set address TextView
        holder.txtAddress.text = "${weatherData?.name?.asString}, ${sysElement?.get("country")?.asString}"

        //Set date TextView
        val date = Date((weatherData?.dt?.asLong!!)*1000)
        val updateAt = SimpleDateFormat("EEEE dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(date)
        holder.txtUpdateAt.text = updateAt

        //Set status TextView
        holder.txtStatus.text = weatherElement?.get("main")?.asString

        //Set icon image
        val icon = weatherElement?.get("icon")?.asString
        val iconUrl = "http://openweathermap.org/img/w/$icon.png"
        Glide.with(mContext).load(iconUrl).into(holder.imgStatusIco)

        //Set temperature TextView
        holder.txtTemp.text = "${mainElement?.get("temp")?.asDouble?.toInt()}$STR_TEMP_UNIT"
        holder.txtMax.text = "${mainElement?.get("temp_max")?.asDouble?.toInt()}$STR_TEMP_UNIT"
        holder.txtMin.text = "${mainElement?.get("temp_min")?.asDouble?.toInt()}$STR_TEMP_UNIT"
        holder.txtRealFeel.text = "${mainElement?.get("feels_like")?.asDouble?.toInt()}$STR_TEMP_UNIT"

        //Set sunrise TextView
        val sunriseElement = Date((sysElement?.get("sunrise")?.asLong!!)*1000)
        val sunriseTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(sunriseElement)
        holder.txtSunRise.text = sunriseTime
        //Set sunset TextView
        val sunsetElement = Date((sysElement.get("sunset")?.asLong!!)*1000)
        val sunsetTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(sunsetElement)
        holder.txtSunSet.text = sunsetTime

        //Set wind speed TextView
        val windElement = weatherData.wind?.asJsonObject
        holder.txtWind.text = "${windElement?.get("speed")?.asString}$STR_WIND_SPEED_UNIT"

        //Set pressure TextView
        holder.txtPressure.text = "${mainElement?.get("pressure")?.asString}$STR_PRESSURE_UNIT"

        //Set humidity TextView
        holder.txtHumidity.text = "${mainElement?.get("humidity")?.asString}$STR_PERCENT_UNIT"

        //Set cloudiness TextView
        holder.txtCloud.text = "${weatherData.clouds?.asJsonObject?.get("all")?.asString} $STR_PERCENT_UNIT"

        //Get Hourly & Daily Weather Api Data
        val citiesModel = mCityModels[i]
        mPresenter.readHourlyDailyWeatherApi(citiesModel.coordinate){ hourlyWeatherModels, dailyWeatherModels->

            //Set hourly UI
            dailyWeatherModels?.removeAt(0) //Delete current day

            val hourlyAdapter =
                HourWeatherRecycleAdapter(
                    mContext,
                    hourlyWeatherModels!!
                )
            holder.recyclerHourly.apply {
                layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
                adapter = hourlyAdapter
            }

            //Set daily UI
            dailyWeatherModels?.removeAt(0) //Delete current day

            val dailyAdapter = DailyWeatherRecycleAdapter(mContext, dailyWeatherModels!!)
            holder.recyclerDaily.apply {
                layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
                adapter = dailyAdapter
            }

        }

        //ReOder UI
        reOder(holder)

    }

    private fun reOder(holder: ViewHolder){
        val order = mPresenter.getOderSetting()

        val orderArray: JsonArray?
        //Create default order
        if(order == "" || order == "[]") {
            orderArray = JsonArray()

            orderArray.add(mContext.getString(R.string.txt_detail))
            orderArray.add(mContext.getString(R.string.txt_hourly))
            orderArray.add(mContext.getString(R.string.txt_daily))
        }else{
            orderArray = JsonParser().parse(order).asJsonArray
        }

        val container1 = orderArray?.get(0)?.asJsonPrimitive?.asString
        val container2 = orderArray?.get(1)?.asJsonPrimitive?.asString


        var tmpLayout: View? = null

        val viewArr = arrayListOf(
            holder.detailContainer,
            holder.hourlyContainer,
            holder.dailyContainer
        )

        viewArr.forEach { loop1 ->

            if((loop1[0] as TextView).text.toString() == container1) {
                (loop1.layoutParams as RelativeLayout.LayoutParams)
                    .addRule(RelativeLayout.BELOW, holder.overViewContainer.id)

                tmpLayout = loop1
            }

        }

        viewArr.remove(tmpLayout)

        viewArr.forEach { loop2 ->

            if((loop2[0] as TextView).text.toString() == container2) {
                (loop2.layoutParams as RelativeLayout.LayoutParams)
                    .addRule(RelativeLayout.BELOW, tmpLayout?.id!!)

                tmpLayout = loop2
            }
        }

        viewArr.remove(tmpLayout)

        (viewArr[0].layoutParams as RelativeLayout.LayoutParams)
            .addRule(RelativeLayout.BELOW, tmpLayout?.id!!)


    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtAddress = itemView.txt_address!!
        val txtUpdateAt = itemView.txt_updated_at!!
        val imgStatusIco = itemView.img_weather_status!!
        val txtStatus = itemView.txt_status!!
        val txtTemp = itemView.txt_temp!!
        val txtMax = itemView.txt_temp_max!!
        val txtMin = itemView.txt_temp_min!!
        val txtRealFeel = itemView.txt_real_feel!!
        val txtSunRise = itemView.txt_sunrise!!
        val txtSunSet = itemView.txt_sunset!!
        val txtWind = itemView.txt_wind!!
        val txtPressure = itemView.txt_pressure!!
        val txtHumidity = itemView.txt_humidity!!
        val txtCloud = itemView.txt_cloudiness!!

        val recyclerHourly = itemView.recycle_hourly_weather!!
        val recyclerDaily = itemView.recycle_daily_weather!!

        val overViewContainer = itemView.container_overview!!
        val detailContainer = itemView.container_detail!!
        val dailyContainer = itemView.container_daily!!
        val hourlyContainer = itemView.container_hourly!!
    }

}