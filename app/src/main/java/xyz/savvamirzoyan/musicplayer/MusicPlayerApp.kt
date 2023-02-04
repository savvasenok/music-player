package xyz.savvamirzoyan.musicplayer

import android.app.Application
import android.os.Build
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import xyz.savvamirzoyan.musicplayer.crash.CrashActivity

@HiltAndroidApp
class MusicPlayerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (!BuildConfig.DEBUG) GlobalExceptionHandler.initialize(this, CrashActivity::class.java)
        if (Build.VERSION.SDK_INT >= 31) DynamicColors.applyToActivitiesIfAvailable(this)
    }
}