package com.anguyen.weatherforecast_demo.adapters.selectedcities

import android.graphics.Canvas
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.anguyen.weatherforecast_demo.R
import com.anguyen.weatherforecast_demo.adapters.CallbackItemTouch

class SelectedCitiesItemTouchCallback(private val callbackItemTouch: CallbackItemTouch): ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled() = true
    override fun isItemViewSwipeEnabled() = true

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int{
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        callbackItemTouch.itemTouchOnMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        callbackItemTouch.onSwiped(viewHolder, viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }else{
            val foregroundView = (viewHolder as SelectedCitiesRecycleAdapter.ViewHolder).itemView
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if(actionState != ItemTouchHelper.ACTION_STATE_DRAG){
            val foregroundView = (viewHolder as SelectedCitiesRecycleAdapter.ViewHolder).itemView
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView = (viewHolder as SelectedCitiesRecycleAdapter.ViewHolder).itemView
        //Set color of item which user swipes
        foregroundView.background = ContextCompat.getDrawable(foregroundView.context, R.drawable.main_bg_gradient)
        //Clear view of item which user swipes
        getDefaultUIUtil().clearView(foregroundView)
    }

//    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
//        super.onSelectedChanged(viewHolder, actionState)
//        if(viewHolder != null){
//            val foregroundView = (viewHolder as SelectedCitiesRecycleAdapter.ViewHolder).itemView
//            if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
//                foregroundView.setBackgroundColor(Color.GRAY)
//            }
//        }
//    }
//
//    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
//        return super.convertToAbsoluteDirection(flags, layoutDirection)
//    }

}