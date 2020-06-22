package com.anguyen.weatherforecast_demo.commons

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun isNetworkConnected(from: Context): Boolean?{
    (from.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getNetworkCapabilities(activeNetwork)?.run {
                when{
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            } ?: false
        } else {
            activeNetworkInfo != null && activeNetworkInfo!!.isConnected
        }
    }
}
