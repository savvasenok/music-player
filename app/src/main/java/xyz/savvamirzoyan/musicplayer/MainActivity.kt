package xyz.savvamirzoyan.musicplayer

import android.os.Bundle
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.progressindicator.LinearProgressIndicator
import dagger.hilt.android.AndroidEntryPoint
import xyz.savvamirzoyan.featuremusicplayerservice.ui.MusicPlayerManager
import xyz.savvamirzoyan.musicplayer.appcore.CoreActivity
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : CoreActivity() {

    @Inject
    lateinit var musicPlayerManager: MusicPlayerManager

    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var fragmentHolder: ViewGroup

    private val navController by lazy { findNavController(R.id.fragment_mainNavHost) }
    private val bottomNavigationView by lazy { findViewById<BottomNavigationView>(R.id.bottomNavigationView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.pb_contentLoading)
        fragmentHolder = findViewById(R.id.fragment_mainNavHost)
    }

    override fun onDestroy() {
        super.onDestroy()
        musicPlayerManager.onClear()
    }

    override fun loading(state: Boolean) {
        when (state) {
            true -> progressBar.show()
            false -> progressBar.hide()
        }
    }
}