package xyz.savvamirzoyan.musicplayer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.featuremusicplayerservice.ui.MusicPlayerManager
import xyz.savvamirzoyan.musicplayer.appcore.CoreActivity
import xyz.savvamirzoyan.musicplayer.appcore.hideWithAnimation
import xyz.savvamirzoyan.musicplayer.appcore.showWithAnimation
import xyz.savvamirzoyan.musicplayer.databinding.ActivityMainBinding
import xyz.savvamirzoyan.musicplayer.feature_mini_player.FragmentMiniPlayer
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : CoreActivity() {

    @Inject
    lateinit var musicPlayerManager: MusicPlayerManager

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainActivityViewModel>()

    private val navController by lazy { findNavController(R.id.fragment_mainNavHost) }
    private val bottomNavigationView by lazy { findViewById<BottomNavigationView>(R.id.bottomNavigationView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMiniPlayer()
        setupFlowListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        musicPlayerManager.onClear()
    }

    override fun loading(state: Boolean) {
        when (state) {
            true -> binding.pbContentLoading.show()
            false -> binding.pbContentLoading.hide()
        }
    }

    private fun setupFlowListeners() {
        lifecycleScope.launch {
            viewModel.isSomethingPlayingFlow.collect {
                if (it) binding.fragmentMiniPlayer.showWithAnimation()
                else binding.fragmentMiniPlayer.hideWithAnimation()
            }
        }
    }

    private fun setupMiniPlayer() {
        val fragment = FragmentMiniPlayer()
        supportFragmentManager.beginTransaction().add(binding.fragmentMiniPlayer.id, fragment).commit()
    }
}