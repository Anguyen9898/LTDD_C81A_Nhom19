package com.anguyen.weatherforecast_demo.uis.cityselector

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anguyen.weatherforecast_demo.R
import com.anguyen.weatherforecast_demo.adapters.cityselector.CitySelectorRecycleAdapter
import com.anguyen.weatherforecast_demo.commons.*
import com.anguyen.weatherforecast_demo.models.CitiesModel
import com.anguyen.weatherforecast_demo.presenters.CitySelectorDialogPresenter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.JsonArray
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.fragment_selected_cities_dialog.*

interface NotifyItemIsSelected{
    fun onSelected()
}

class CitySelectorFragmentDialog(
    private val mNotifyDataChange: NotifyItemIsSelected,
    private val progressDialog: KProgressHUD
) : BottomSheetDialogFragment(),
    CitySelectorDialogView, CitySelectorRecycleAdapter.OnRecycleCitySelectorItemClick {

    private lateinit var mPresenter: CitySelectorDialogPresenter

    private lateinit var mAdapter: CitySelectorRecycleAdapter
    private lateinit var mCities: ArrayList<CitiesModel>

    private lateinit var citiesData: JsonArray

    companion object{
        const val TAG = "CitySelectorFragmentDialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_selected_cities_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAll()
    }

    private fun setupAll(){

        //Call City-Selector Presenter
        mPresenter = CitySelectorDialogPresenter(context!!, this)
        mPresenter.getAllCities()

        //Set cancel click
        txt_cancel.onClick { dismiss() }

        //Close Selected Activity's Progress Dialog
        progressDialog.dismiss()

    }

    private fun setupRecyclerView(strSearch: String?){

        mCities = ArrayList()

        if(strSearch != null){
            citiesData.forEach { element ->

                val cityApiKey = element?.asJsonObject?.get("name")?.asString!!
                //val cityName = element.asJsonObject?.get("detail")?.asString!!
                val country = element.asJsonObject?.get("country")?.asString!!
                val coordinate = element.asJsonObject?.get("coord")?.asJsonObject

                if (cityApiKey.contains(strSearch, true)){
                    mCities.add(CitiesModel(cityApiKey, "$cityApiKey, $country", coordinate.toString()))
                }
            }
        }

        mAdapter = CitySelectorRecycleAdapter(context!!, mCities, this)

        if(mAdapter.itemCount == 0){
            onFindCityFailure()
        }

        recycle_cities.apply {
            layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }

    }

    override fun onItemClickHandler(cityDetail: CitiesModel) {
        mPresenter.saveCityToDatabase(cityDetail)
        mNotifyDataChange.onSelected()
    }

    override fun onGetAllCitiesSuccess(data: JsonArray?) {
        citiesData = data!!

        //SetupSearchView
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        search_city.apply {
            //set info
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            //set hint
            queryHint = getString(R.string.hint_search)
            //set on searching handler
            onQueryRequest(
                onQueryTextChange = {
                    if(it != ""){
                        setupRecyclerView(it)
                    }else{
                        setupRecyclerView(null)
                    }
                },
                onQueryTextSubmit = {}
            )
        }

    }

    override fun onGetDataFailure(message: String?) {
        this.dismiss()
        context?.showDataGettingFailed(message!!)
    }

    override fun onFindCityFailure() {
        context?.showToast("Not Found")
    }

    override fun onCityInfoSavingSuccess() {
        dismiss()
    }

    override fun onCityInfoSavingFailure() {
        context?.showToast(getString(R.string.id_conflict))
    }
    
}