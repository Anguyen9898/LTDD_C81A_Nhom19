package com.anguyen.weatherforecast_demo.adapters.cityselector

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.anguyen.weatherforecast_demo.R
import com.anguyen.weatherforecast_demo.commons.bgColorChangeAnimation
import com.anguyen.weatherforecast_demo.commons.onClick
import com.anguyen.weatherforecast_demo.models.CitiesModel
import kotlinx.android.synthetic.main.city_selector_item.view.*

class CitySelectorRecycleAdapter(
    private val mContext: Context,
    private val mCities: List<CitiesModel>,
    private val mOnClickListener: OnRecycleCitySelectorItemClick
): RecyclerView.Adapter<CitySelectorRecycleAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.city_selector_item,
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

        holder.onItemClick(city)
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtCityApiId = itemView.txt_id!!
        val txtCityName = itemView.txt_city_name!!

        fun onItemClick(cityDetail: CitiesModel){
            itemView.onClick {
                mOnClickListener.onItemClickHandler(cityDetail)
            }
        }

    }

    interface OnRecycleCitySelectorItemClick{
        fun onItemClickHandler(cityDetail: CitiesModel)
    }


}