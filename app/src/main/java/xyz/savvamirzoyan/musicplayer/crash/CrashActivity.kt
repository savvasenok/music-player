package xyz.savvamirzoyan.musicplayer.crash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.musicplayer.GlobalExceptionHandler
import xyz.savvamirzoyan.musicplayer.MainActivity
import xyz.savvamirzoyan.musicplayer.appcore.CoreActivity
import xyz.savvamirzoyan.musicplayer.appcore.setState
import xyz.savvamirzoyan.musicplayer.databinding.ActivityCrashBinding

class CrashActivity : CoreActivity() {

    private lateinit var binding: ActivityCrashBinding

    override fun loading(state: Boolean) = Unit

    private val viewModel by viewModels<CrashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrashBinding.inflate(layoutInflater)
        setClickListeners()
        setFlowListeners()
        setContentView(binding.root)
    }

    override fun hideMiniPlayer() = Unit
    override fun showMiniPlayer() = Unit
    override fun hideBottomNavigation() = Unit
    override fun showBottomNavigation() = Unit

    private fun setFlowListeners() {
        lifecycleScope.launch {
            viewModel.actionFinishActivityFlow.collect { finishAffinity() }
        }

        lifecycleScope.launch {
            viewModel.buttonStateFlow.collect {
                binding.buttonReport.setState(it)
            }
        }
    }

    private fun setClickListeners() {
        binding.buttonReport.setOnClickListener {
            viewModel.reportError(GlobalExceptionHandler.getThrowable(intent))
        }

        binding.tvRestartApp.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}