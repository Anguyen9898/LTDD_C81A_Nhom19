package com.anguyen.weatherforecast_demo.uis.selectedcities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anguyen.weatherforecast_demo.R
import com.anguyen.weatherforecast_demo.adapters.CallbackItemTouch
import com.anguyen.weatherforecast_demo.adapters.selectedcities.SelectedCitiesItemTouchCallback
import com.anguyen.weatherforecast_demo.adapters.selectedcities.SelectedCitiesRecycleAdapter
import com.anguyen.weatherforecast_demo.commons.*
import com.anguyen.weatherforecast_demo.models.CitiesModel
import com.anguyen.weatherforecast_demo.presenters.SelectedCitiesPresenter
import com.anguyen.weatherforecast_demo.uis.cityselector.CitySelectorFragmentDialog
import com.anguyen.weatherforecast_demo.uis.cityselector.NotifyItemIsSelected
import com.anguyen.weatherforecast_demo.uis.mains.MainActivity
import com.anguyen.weatherforecast_demo.uis.setting.SettingActivity
import com.google.android.material.snackbar.Snackbar
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_selected_cities.*
import kotlin.system.exitProcess

class SelectedCitiesActivity : AppCompatActivity(),
    SelectedCitiesView,
    NotifyItemIsSelected,
    SelectedCitiesRecycleAdapter.OnRecycleSelectedCitiesItemClick, CallbackItemTouch{

    companion object{
        const val KEY_VIEW_POS = "selected_view"
    }

    private lateinit var mAdapter: SelectedCitiesRecycleAdapter
    private lateinit var mCities: ArrayList<CitiesModel>
    private var mPresenter: SelectedCitiesPresenter? = null

    private lateinit var progressDialog: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_cities)

        progressDialog = initProgressDialog(this)
        progressDialog.show()

        setupAll()
    }

    override fun onResume() {
        super.onResume()
        setupAll()
    }

    private fun setupAll(){

        //Call Selected Cities Presenter
        mPresenter = SelectedCitiesPresenter(this, this)
        mPresenter?.getSelectedCitiesFromStorage()

        //Button Handler
        btn_add.onClick { showBottomSheetDialogSelector() }
        txt_empty_message.onClick { showBottomSheetDialogSelector() }
        toolbar_container.onItemClick { 
            when(it.itemId){
                R.id.to_main -> startActivity(Intent(this, MainActivity::class.java))
                R.id.setting -> startActivity(Intent(this, SettingActivity::class.java))
                R.id.exit -> {
                    finishAffinity()
                    exitProcess(0)
                }
            }
        }

    }

    private fun showBottomSheetDialogSelector() {
        progressDialog.show()

        CitySelectorFragmentDialog(this, progressDialog).show(
            supportFragmentManager,
            CitySelectorFragmentDialog.TAG
        )
    }

    override fun onGettingSuccess(callbackData: List<CitiesModel>?) {

        //Hide ui's empty message
        txt_empty_message.visibility = View.GONE

        //Setup selected list
        mCities = ArrayList()

        //Read callback data from SharePreference
        callbackData?.forEach {
            mCities.add(CitiesModel(it.cityApiKey, it.cityName))
        }

        //Set up Adapter
        mAdapter = SelectedCitiesRecycleAdapter(this, mCities, mPresenter!!, progressDialog, this)

        //Set up RecycleView
        recycle_selected_cities.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }

        //Setup recyclerview's swiped to delete animation
        val itemTouchHelperCallback: ItemTouchHelper.Callback = SelectedCitiesItemTouchCallback(this)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycle_selected_cities)

    }

    override fun onDataIsEmpty() {
        progressDialog.dismiss()
        txt_empty_message.visibility = View.VISIBLE
    }

    override fun showInternetError() {
        progressDialog.dismiss()
        showInternetErrorDialog()
    }

    override fun onItemClickHandler(selectedItem: Int) {
        Log.d("SelectedCities", selectedItem.toString())
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(KEY_VIEW_POS, selectedItem)

        startActivity(intent)
    }

    override fun onSelected() {
        if(mPresenter != null){
            mPresenter?.getSelectedCitiesFromStorage()
        }
    }

    override fun itemTouchOnMove(oldPosition: Int, newPosition: Int) {
        mCities.add(newPosition, mCities.removeAt(oldPosition))
        mAdapter.notifyItemMoved(oldPosition, newPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val strCityId = mCities[viewHolder.adapterPosition].cityApiKey
        val strCityName = mCities[viewHolder.adapterPosition].cityName

        //Backup of removed item for undo
        val deletedItem = mCities[viewHolder.adapterPosition]
        //val deletedIndex = viewHolder.adapterPosition

        //Remove the item from recyclerview
        mAdapter.removeItem(position)

        //Show SnackBar for undo
        var isUndo = false
        val undoSnackBar = Snackbar.make(container_selected_cities, "$strCityName was removed..!", Snackbar.LENGTH_LONG)
        undoSnackBar.apply {
            setAction("Undo") {
                mAdapter.restoreItem(position, deletedItem)
                isUndo = true
            }
            setActionTextColor(Color.GREEN)
            setFinishOnTouchOutside(true)
            show()
        }

        //Remove from database after undo bar dismiss
        undoSnackBar.onDismissed {
            if (!isUndo){
                mPresenter?.removeCity(strCityId)
            }
        }
    }

}