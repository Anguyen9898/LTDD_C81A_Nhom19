package com.anguyen.weatherforecast_demo.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class BaseApi {

    private var retrofitAdapter: Retrofit? = null

    fun <T> getService(tClass: Class<T>, url: String): T? {

        if(retrofitAdapter == null){
            val client = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(80, TimeUnit.SECONDS)
                .build()

            retrofitAdapter = Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        return retrofitAdapter?.create(tClass)

    }

}