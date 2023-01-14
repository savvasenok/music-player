package xyz.savvamirzoyan.musicplayer.appcore

import android.os.Bundle
import android.os.PersistableBundle
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

    protected abstract fun loading(state: Boolean)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    fun startLoading() {
        loadersCounter += 1
    }

    fun stopLoading() {
        loadersCounter -= 1
    }
}