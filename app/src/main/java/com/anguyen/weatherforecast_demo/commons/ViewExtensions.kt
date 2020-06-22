package com.anguyen.weatherforecast_demo.commons

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

inline fun View.onClick(crossinline onClickHandler: () -> Unit){
    setOnClickListener { onClickHandler() }
}

inline fun View.onTouch(crossinline onTouchHandler: (MotionEvent) -> Unit){
    setOnTouchListener { _, event ->
        onTouchHandler(event)
        performClick()
        return@setOnTouchListener true
    }
}

fun View.bgColorChangeAnimation(to: Int){
    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), this.solidColor, to)

    colorAnimation.apply {
        duration = 250 //milliseconds

        addUpdateListener {
            setBackgroundColor(it.animatedValue as Int)
        }

        start()
    }

}

inline fun SearchView.onQueryRequest(crossinline onQueryTextChange: (String?) -> Unit
                                     , crossinline onQueryTextSubmit: (String?) -> Unit){

    setOnQueryTextListener( object : SearchView.OnQueryTextListener{

        override fun onQueryTextSubmit(query: String?): Boolean {
            onQueryTextSubmit(query)
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            onQueryTextChange(newText)
            return false
        }

    })

}

inline fun Snackbar.onDismissed(crossinline dismissCallbackHandler: () -> Unit) {
   addCallback(object: Snackbar.Callback(){
       override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
           val dismissEvents = arrayOf(DISMISS_EVENT_SWIPE, DISMISS_EVENT_ACTION,
               DISMISS_EVENT_TIMEOUT, DISMISS_EVENT_MANUAL, DISMISS_EVENT_CONSECUTIVE)

           if(dismissEvents.any { event == it }){
               dismissCallbackHandler()
           }
       }
   })
}

fun Fragment.setup(from: FragmentActivity, replacedId: Int, bundle: Bundle?) {
    this.arguments = bundle
    from.supportFragmentManager
        .beginTransaction()
        .replace(replacedId, this)
        .commit()
}


inline fun EditText.onTextChanged(crossinline onTextChangeHandler: (String) -> Unit){
    addTextChangedListener(object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChangeHandler(s?.toString() ?: "")
        }
    })
}

inline fun EditText.onFocusChanged(crossinline onFocusChangeHandler: (modeId: Int) -> Unit){
    val resizeMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
    val panMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN

    setOnFocusChangeListener{_, hasFocus ->
        if(hasFocus) {
            onFocusChangeHandler(resizeMode)
        } else {
            onFocusChangeHandler(panMode)
        }
    }
}

inline fun BottomNavigationView.onItemSelected(crossinline onItemSelectedHandler
                                               : (menuItem: MenuItem) -> Unit){
    setOnNavigationItemSelectedListener{
        onItemSelectedHandler(it)
        return@setOnNavigationItemSelectedListener true
    }
}

inline fun RadioGroup.onCheckedChangeListener(crossinline onItemClickHandler: () -> Unit){
    setOnCheckedChangeListener{ _, _ ->  onItemClickHandler()}
}

fun AppCompatActivity.backToPrevious() {
    if(supportFragmentManager.backStackEntryCount > 0){
        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}

inline fun Toolbar.onItemClick(crossinline onItemClickHandler: (menuItem: MenuItem) -> Unit){
    setOnMenuItemClickListener {
        onItemClickHandler (it)
        true
    }
}
