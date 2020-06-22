package com.anguyen.weatherforecast_demo.adapters.hourlyweather

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anguyen.weatherforecast_demo.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.daily_weather_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import com.anguyen.weatherforecast_demo.commons.*
import com.anguyen.weatherforecast_demo.models.HourlyWeatherModel

class HourWeatherRecycleAdapter(
    private val mContext: Context,
    private val mHourWeatherData: List<HourlyWeatherModel?>
): RecyclerView.Adapter<HourWeatherRecycleAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.hourly_weather_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = mHourWeatherData.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val hourlyWeatherModel = mHourWeatherData[i]

        //Set date TextView
        val date = Date((hourlyWeatherModel?.dt?.asLong!!)*1000)
        val strDay = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(date)
        holder.txtDay.text = strDay

        //Set icon image
        val icon = hourlyWeatherModel.weather?.get(0)?.asJsonObject?.get("icon")?.asString
        val iconUrl = "http://openweathermap.org/img/w/$icon.png"
        Glide.with(mContext).load(iconUrl).into(holder.imgStatus)

        //Set temperature TextView
        holder.txtTemp.text = "${hourlyWeatherModel.temp?.asDouble?.toInt()}$STR_TEMP_UNIT"
//        holder.txtMax.text = "${mainElement?.get("temp_max")?.asDouble?.toInt()}$STR_TEMP_UNIT"
//        holder.txtMin.text = "${mainElement?.get("temp_min")?.asDouble?.toInt()}$STR_TEMP_UNIT"

    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtDay = itemView.txt_day!!
        val imgStatus = itemView.img_weather_status!!
        val txtTemp = itemView.txt_temp!!

    }

}