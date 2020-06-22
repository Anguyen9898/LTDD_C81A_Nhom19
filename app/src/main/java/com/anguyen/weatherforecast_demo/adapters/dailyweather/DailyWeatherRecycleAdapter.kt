package com.anguyen.weatherforecast_demo.adapters.dailyweather

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anguyen.weatherforecast_demo.R
import com.anguyen.weatherforecast_demo.models.DailyWeatherModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.daily_weather_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import com.anguyen.weatherforecast_demo.commons.*

class DailyWeatherRecycleAdapter(
    private val mContext: Context,
    private val mDailyWeatherData: List<DailyWeatherModel?>
): RecyclerView.Adapter<DailyWeatherRecycleAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.daily_weather_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = mDailyWeatherData.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val dailyWeatherModel = mDailyWeatherData[i]

        //Set date TextView
        val date = Date((dailyWeatherModel?.dt?.asLong!!)*1000)
        val strDay = SimpleDateFormat("EEEE dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(date)
        holder.txtDay.text = strDay

        //Set icon image
        val icon = dailyWeatherModel.weather?.get(0)?.asJsonObject?.get("icon")?.asString
        val iconUrl = "http://openweathermap.org/img/w/$icon.png"
        Glide.with(mContext).load(iconUrl).into(holder.imgStatus)

        //Set temperature TextView
        holder.txtTemp.text = "${dailyWeatherModel.temp?.asJsonObject?.get("day")?.asDouble?.toInt()}$STR_TEMP_UNIT"
//        holder.txtMax.text = "${mainElement?.get("temp_max")?.asDouble?.toInt()}$STR_TEMP_UNIT"
//        holder.txtMin.text = "${mainElement?.get("temp_min")?.asDouble?.toInt()}$STR_TEMP_UNIT"

    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtDay = itemView.txt_day!!
        val imgStatus = itemView.img_weather_status!!
        val txtTemp = itemView.txt_temp!!

    }

}