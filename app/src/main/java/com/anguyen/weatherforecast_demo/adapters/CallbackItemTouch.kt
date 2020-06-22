package com.anguyen.weatherforecast_demo.adapters

import androidx.recyclerview.widget.RecyclerView

interface CallbackItemTouch {
    fun itemTouchOnMove(oldPosition: Int, newPosition: Int)
    fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int)
}