package com.anguyen.weatherforecast_demo.adapters.reoder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anguyen.weatherforecast_demo.R
import kotlinx.android.synthetic.main.reoder_item.view.*
import kotlin.collections.ArrayList

class ReOderRecycleAdapter(
    private val mContext: Context,
    private val mOrders: ArrayList<String>
): RecyclerView.Adapter<ReOderRecycleAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.reoder_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = mOrders.size

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.txt.text = mOrders[i]
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txt = itemView.txt!!
    }

}