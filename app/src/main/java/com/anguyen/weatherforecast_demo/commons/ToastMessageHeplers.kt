package com.anguyen.weatherforecast_demo.commons

import android.content.Context
import android.widget.Toast

fun Context.showToast(messageId: Int) = Toast.makeText(this, this.getString(messageId), Toast.LENGTH_LONG).show()

fun Context.showToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
