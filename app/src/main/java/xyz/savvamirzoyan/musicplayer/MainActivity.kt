package xyz.savvamirzoyan.musicplayer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import xyz.savvamirzoyan.featuremusicplayerservice.MusicPlayerService
import xyz.savvamirzoyan.featuremusicplayerservice.ui.MusicPlayerManager
import xyz.savvamirzoyan.musicplayer.appcore.CoreActivity
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : CoreActivity() {

    @Inject
    lateinit var musicPlayerManager: MusicPlayerManager

    private val navController by lazy { findNavController(R.id.fragment_mainNavHost) }
    private val bottomNavigationView by lazy { findViewById<BottomNavigationView>(R.id.bottomNavigationView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun startPlayerService() {
        val intent = Intent(this, MusicPlayerService::class.java)

        if (Build.VERSION.SDK_INT >= 26) startForegroundService(intent)
        else startService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        musicPlayerManager.onClear()
    }
}