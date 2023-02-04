package xyz.savvamirzoyan.musicplayer.feature_mini_player

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import xyz.savvamirzoyan.musicplayer.appcore.CoreFragment
import xyz.savvamirzoyan.musicplayer.appcore.SongUi
import xyz.savvamirzoyan.musicplayer.appcore.load
import xyz.savvamirzoyan.musicplayer.appcore.setText
import xyz.savvamirzoyan.musicplayer.feature_mini_player.databinding.FragmentMiniPlayerBinding

@AndroidEntryPoint
class FragmentMiniPlayer : CoreFragment<FragmentMiniPlayerBinding>() {

    private val viewModel by viewModels<MiniPlayerViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMiniPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFlowListeners()
        setupClickListeners()
    }

    private fun setupFlowListeners() {

        collect(viewModel.currentSongFlow) {
            setupMiniPlayer(it)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            collect(viewModel.currentSongProgressFlow) {
                binding.pbTrack.setProgress(it, true)
            }
        } else {
            collect(viewModel.currentSongProgressFlow) {
                binding.pbTrack.progress = it
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonPlay.setOnClickListener {
            viewModel.onButtonPlayClick()
        }
    }

    private fun setupMiniPlayer(song: SongUi) {
        binding.ivPicture.load(song.albumPictureUrl)
        binding.tvSongTitle.setText(song.title)
        binding.tvSongAuthor.setText(song.artist)
        binding.buttonPlay.isChecked = song.isPlaying
    }
}