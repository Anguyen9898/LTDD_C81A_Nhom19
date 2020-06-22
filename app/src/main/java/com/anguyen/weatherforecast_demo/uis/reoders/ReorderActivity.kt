package com.anguyen.weatherforecast_demo.uis.reoders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anguyen.weatherforecast_demo.R
import com.anguyen.weatherforecast_demo.adapters.CallbackItemTouch
import com.anguyen.weatherforecast_demo.adapters.reoder.ReOderRecycleAdapter
import com.anguyen.weatherforecast_demo.adapters.reoder.ReOderRecycleTouchCallback
import com.anguyen.weatherforecast_demo.commons.onClick
import com.anguyen.weatherforecast_demo.presenters.ReOderPresenter
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_reorder.*
const val tag = "setting's-share-pref"

class ReorderActivity : AppCompatActivity(), CallbackItemTouch, ReoderView {

    private lateinit var mPresenter: ReOderPresenter
    private lateinit var mOrders: ArrayList<String>
    private var tempOrder = JsonArray()
    private lateinit var mAdapter: ReOderRecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reorder)

        setupAll()
    }

    private fun setupAll(){
        mOrders = ArrayList()
        mPresenter = ReOderPresenter(this, this)
        val lastOrder = mPresenter.getOrderFromSharePref()
        //Create Default order
        if (lastOrder == "" || lastOrder == "[]"){
            mOrders.add(getString(R.string.txt_detail))
            mOrders.add(getString(R.string.txt_hourly))
            mOrders.add(getString(R.string.txt_daily))
        }else{
            val lastOrderArray = JsonParser().parse(lastOrder).asJsonArray
            lastOrderArray.forEach {
                mOrders.add(it.asJsonPrimitive.asString)
            }
        }
        setupRecyclerView()

        btn_back.onClick { onBackPressed() }

        //Setup recyclerview's swiped to delete animation
        val itemTouchHelperCallback: ItemTouchHelper.Callback = ReOderRecycleTouchCallback(this)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycle_reoder)

    }

    private fun setupRecyclerView(){
        mAdapter = ReOderRecycleAdapter(this, mOrders)
        recycle_reoder.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
    }

    override fun itemTouchOnMove(oldPosition: Int, newPosition: Int) {
        mOrders.add(newPosition, mOrders.removeAt(oldPosition))
        mAdapter.notifyItemMoved(oldPosition, newPosition)
    }

    override fun onBackPressed() {
        mPresenter.saveOderToSharePref(mOrders.toString())
        super.onBackPressed()
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun onSaved() {
        Log.d(tag, mPresenter.getOrderFromSharePref().toString())
    }

}