package xyz.savvamirzoyan.musicplayer.feature_fullscale_player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import xyz.savvamirzoyan.musicplayer.appcore.CoreFragment
import xyz.savvamirzoyan.musicplayer.appcore.load
import xyz.savvamirzoyan.musicplayer.appcore.setText
import xyz.savvamirzoyan.musicplayer.appcore.updateProgress
import xyz.savvamirzoyan.musicplayer.feature_fullscale_player.databinding.FragmentFullscalePlayerBinding
import javax.inject.Inject

@AndroidEntryPoint
class FragmentFullscalePlayer @Inject constructor() : CoreFragment<FragmentFullscalePlayerBinding>() {

    private var isScrollingMusic = false

    private val viewModel by viewModels<FullscalePlayerViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFullscalePlayerBinding.inflate(inflater, container, false)
        setupFragmentWithTopBottomInsects()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupFlowListeners()
        setupScrollingTitle()
    }

    private fun setupScrollingTitle() {
        binding.tvTitle.isSelected = true
    }

    override fun onResume() {
        super.onResume()
        coreActivity.hideMiniPlayer()
        coreActivity.hideBottomNavigation()
    }

    override fun onStop() {
        super.onStop()
        coreActivity.showMiniPlayer()
        coreActivity.showBottomNavigation()
    }

    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.buttonPlay.setOnClickListener { viewModel.onPlayButtonClick() }
        binding.buttonSkipBack.setOnClickListener { viewModel.onSkipBackClick() }
        binding.buttonSkipForward.setOnClickListener { viewModel.onSkipForwardClick() }
        binding.buttonSubAction1.setOnClickListener {
            viewModel.onSubAction1Click()
        }
        binding.buttonSubAction2.setOnClickListener {
            viewModel.onSubAction2Click()
        }
        binding.sbSongProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {}

            override fun onStartTrackingTouch(p0: SeekBar?) {
                isScrollingMusic = true
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                isScrollingMusic = false
                p0?.let { seekbar -> viewModel.onMusicScrolled(seekbar.progress) }
            }
        })
    }

    private fun setupFlowListeners() {
        collect(viewModel.toolbarTextFlow) {
            binding.toolbar.title = it.title.getString(requireContext())
            binding.toolbar.subtitle = it.subtitle.getString(requireContext())
        }

        collect(viewModel.pictureFlow) {
            binding.ivPicture.load(it)
        }

        collect(viewModel.songTitleFlow) {
            binding.tvTitle.setText(it)
        }

        collect(viewModel.songArtistFlow) {
            binding.tvArtist.setText(it)
        }

        collect(viewModel.isSongLikedFlow) {
            binding.buttonLike.isChecked = it
        }

        collect(viewModel.songProgressFlow) {
            if (!isScrollingMusic) binding.sbSongProgress.updateProgress(it)
        }

        collect(viewModel.songProgressTextFlow) {
            binding.tvTimeCurrent.setText(it)
        }

        collect(viewModel.songDurationTextFlow) {
            binding.tvDuration.setText(it)
        }

        collect(viewModel.isPlayingFlow) {
            binding.buttonPlay.toggle(it)
        }

        collect(viewModel.subAction1IconIdFlow) {
            binding.buttonSubAction1.setIconResource(it)
        }

        collect(viewModel.isSubAction1SelectedFlow) {
            binding.buttonSubAction1.isChecked = it
        }

        collect(viewModel.subAction2IconIdFlow) {
            binding.buttonSubAction2.setIconResource(it)
        }

        collect(viewModel.isSubAction2SelectedFlow) {
            binding.buttonSubAction2.isChecked = it
        }
    }
}