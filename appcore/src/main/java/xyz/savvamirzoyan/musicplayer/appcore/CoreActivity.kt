package xyz.savvamirzoyan.musicplayer.appcore

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

abstract class CoreActivity : AppCompatActivity() {

    // counts how many time loader was requested
    private var loadersCounter: Int = 0
        set(value) {

            field = if (value < 0) 0 else value

            if (field == 0) loading(false)
            else loading(true)
        }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            window.decorView.rootWindowInsets.displayCutout.logd("CUTOUT")
        }
    }

    fun startLoading() {
        Log.d("SPAMEGGS", "startLoading() called")
        loadersCounter++
    }

    fun stopLoading() {
        Log.d("SPAMEGGS", "stopLoading() called")
        loadersCounter--
    }

    protected abstract fun loading(state: Boolean)
}