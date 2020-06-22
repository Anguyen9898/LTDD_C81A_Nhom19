package com.anguyen.weatherforecast_demo.uis.abouts

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anguyen.weatherforecast_demo.R
import com.anguyen.weatherforecast_demo.commons.onClick
import kotlinx.android.synthetic.main.activity_about.*

const val API_URL = "http://openweathermap.org/"
const val GIT_URL = "http://github.com/"

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setupAll()
    }

    private fun setupAll(){
        btn_back.onClick { onBackPressed() }

        txt_source.onClick { startActivity(API_URL.createBrowserIntent()) }

        img_weather_api.onClick { startActivity(API_URL.createBrowserIntent()) }
        img_git.onClick { startActivity(GIT_URL.createBrowserIntent()) }
    }

    private fun String.createBrowserIntent() = Intent(Intent.ACTION_VIEW, Uri.parse(this))
}