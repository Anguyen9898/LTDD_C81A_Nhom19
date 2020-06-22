package com.anguyen.weatherforecast_demo.uis.starts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.anguyen.weatherforecast_demo.R
import com.anguyen.weatherforecast_demo.presenters.StartPresenter
import com.anguyen.weatherforecast_demo.uis.mains.MainActivity
import com.anguyen.weatherforecast_demo.uis.selectedcities.SelectedCitiesActivity
import kotlinx.android.synthetic.main.activity_start.*

const val FADEOUT_DURATION = 2050L

class StartActivity : AppCompatActivity(), Animation.AnimationListener, StartView{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        setupAll()
    }

    private fun setupAll(){
        //Set Animation
        val fadeout = AlphaAnimation(1f, 1f)
        fadeout.duration = FADEOUT_DURATION
        fadeout.setAnimationListener(this)

        gif_start_logo.startAnimation(fadeout)
    }

    override fun onAnimationRepeat(animation: Animation?) = Unit

    override fun onAnimationStart(animation: Animation?){
        gif_start_logo.setBackgroundResource(R.drawable.gif_logo)
        //Set logo height
        gif_start_logo.layoutParams.height = gif_start_logo.width
    }

    override fun onAnimationEnd(animation: Animation?) = updateUI()

    private fun updateUI(){
        StartPresenter(this, this).updateUI()
        finish()
    }

    override fun openMainUI() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun jumpToCitySelectorUI() {
        startActivity(Intent(this, SelectedCitiesActivity::class.java))
    }

}