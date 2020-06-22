package com.anguyen.weatherforecast_demo.adapters.selectedcities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anguyen.weatherforecast_demo.R
import com.anguyen.weatherforecast_demo.commons.STR_TEMP_UNIT
import com.anguyen.weatherforecast_demo.commons.onClick
import com.anguyen.weatherforecast_demo.models.CitiesModel
import com.anguyen.weatherforecast_demo.presenters.SelectedCitiesPresenter
import com.bumptech.glide.Glide
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.selecleted_city_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SelectedCitiesRecycleAdapter(
    private val mContext: Context,
    private val mCities: ArrayList<CitiesModel>,
    private val mPresenter: SelectedCitiesPresenter,
    private val progressDialog: KProgressHUD,
    private val mOnClickListener: OnRecycleSelectedCitiesItemClick
): RecyclerView.Adapter<SelectedCitiesRecycleAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.selecleted_city_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = mCities.size

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val city = mCities[i]
        holder.txtCityApiId.text = city.cityApiKey
        holder.txtCityName.text = city.cityName

        mPresenter.readCurrentWeatherApi(city.cityApiKey){ currentWeatherModel ->

            //Read JsonObject
            val weatherElement = currentWeatherModel?.weather?.get(0)?.asJsonObject
            val mainElement = currentWeatherModel?.main?.asJsonObject

            //Set date TextView
            val date = Date((currentWeatherModel?.dt?.asLong!!)*1000)
            val updateAt = SimpleDateFormat("EEEE dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(date)
            holder.txtUpdateAt.text = updateAt

            //Set icon image
            val icon = weatherElement?.get("icon")?.asString
            val iconUrl = "http://openweathermap.org/img/w/$icon.png"
            Glide.with(mContext).load(iconUrl).into(holder.imgStatus)

            //Set temperature TextView
            holder.txtTemp.text = "${mainElement?.get("temp")?.asDouble?.toInt()}$STR_TEMP_UNIT"

        }

        holder.onItemClick(i)

        //Close dialog progress
        progressDialog.dismiss()

    }

    fun removeItem(position: Int){
        mCities.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(position: Int, item: CitiesModel){
        mCities.add(position, item)
        notifyItemInserted(position)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtCityApiId= itemView.txt_id!!
        val txtCityName = itemView.txt_city_name!!
        val txtUpdateAt = itemView.txt_updated_at!!
        val imgStatus = itemView.img_weather_status!!
        val txtTemp = itemView.txt_temp!!

        fun onItemClick(selectedItem: Int){
            itemView.onClick {
                mOnClickListener.onItemClickHandler(selectedItem)
            }
        }
    }

    interface OnRecycleSelectedCitiesItemClick{
        fun onItemClickHandler(selectedItem: Int)
    }

}