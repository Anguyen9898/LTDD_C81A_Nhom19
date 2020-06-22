package com.anguyen.weatherforecast_demo.commons

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.anguyen.weatherforecast_demo.R

private fun errorDialog(from: Context, titleId: Int, messageId: Int) {

    AlertDialog.Builder(from).apply {

        setTitle(from.getString(titleId))

        setMessage(from.getString(messageId))

        setPositiveButton(from.getString(R.string.general_positive_button)){ dialog, _ -> dialog.dismiss() }

    }.show()!!

}

private fun errorDialog(from: Context, title: String, message: String) {

    AlertDialog.Builder(from).apply {

        setTitle(title)

        setIcon(R.drawable.ic_sad_emoji)

        setMessage(message)

        setPositiveButton(from.getString(R.string.general_positive_button)){ dialog, _ -> dialog.dismiss() }

    }.show()!!

}

private fun confirmDialog(from: Context, titleId: Int, messageId: Int, confirmFunction: ()-> Unit)  {

    AlertDialog.Builder(from).apply {

        setTitle (from.getString(titleId))

        setMessage(from.getString(messageId))

        setIcon(R.drawable.ic_warning)

        setPositiveButton(from.getString(R.string.general_positive_button)){ _, _ -> confirmFunction() }

        setNegativeButton(from.getString(R.string.general_negative_button)){ dialog, _ -> dialog.cancel() }

    }.show()!!
}

fun Context.showGeneralErrorDialog(){
    errorDialog(this, R.string.general_error_title, R.string.general_error_message)
}

fun Context.showInternetErrorDialog() {
    confirmDialog(this, R.string.internet_error_title, R.string.internet_error_message){
        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    }
}

fun Context.showDataGettingFailed(message: String){
    errorDialog(this, getString(R.string.general_error_title),  message)
}



