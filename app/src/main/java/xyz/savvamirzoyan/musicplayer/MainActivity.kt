package xyz.savvamirzoyan.musicplayer

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.featuremusicplayerservice.ui.MusicPlayer
import xyz.savvamirzoyan.musicplayer.appcore.*
import xyz.savvamirzoyan.musicplayer.databinding.ActivityMainBinding
import xyz.savvamirzoyan.musicplayer.feature_mini_player.FragmentMiniPlayer
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : CoreActivity() {

    @Inject
    lateinit var musicPlayer: MusicPlayer

    @Inject
    lateinit var deepLinkBuilder: DeepLinkBuilder

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

    override fun loading(state: Boolean) {
        when (state) {
            true -> binding.pbContentLoading.show()
            false -> binding.pbContentLoading.hide()
        }
    }

    override fun hideMiniPlayer() {
        binding.fragmentMiniPlayer.hideSlideDownWithAnimation()
    }

    override fun showMiniPlayer() {
        binding.fragmentMiniPlayer.showSlideUpWithAnimation()
    }

    override fun hideBottomNavigation() {
        binding.bottomNavigationView.hideSlideDownWithAnimation()
    }

    override fun showBottomNavigation() {
        binding.bottomNavigationView.showSlideUpWithAnimation()
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

        binding.fragmentMiniPlayer.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_bottom_fade_in)
                .setExitAnim(com.google.android.material.R.anim.abc_fade_out)
                .setPopExitAnim(R.anim.slide_out_bottom_fade_out)
                .setPopEnterAnim(com.google.android.material.R.anim.abc_fade_in)
                .build()
            val request = NavDeepLinkRequest.Builder
                .fromUri(Uri.parse(deepLinkBuilder.fullscalePlayerDeepLink))
                .build()
            navController.navigate(request = request, navOptions = navOptions)
        }
    }
}