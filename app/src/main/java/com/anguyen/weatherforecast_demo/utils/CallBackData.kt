package com.anguyen.weatherforecast_demo.utils

interface CallBackData<T> {
    fun onSuccess(vararg t: T)
    fun onFailure(vararg message: String?)
}