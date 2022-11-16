package xyz.savvamirzoyan.musicplayer.featurehome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import xyz.savvamirzoyan.musicplayer.appcore.CoreFragment
import xyz.savvamirzoyan.musicplayer.appcore.load
import xyz.savvamirzoyan.musicplayer.featurehome.databinding.FragmentHomeBinding

@AndroidEntryPoint
class HomeFragment : CoreFragment<FragmentHomeBinding>() {

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFlowListeners()
    }

    private fun setupFlowListeners() {
        collect(viewModel.playlistsStateFlow) { state ->

            binding.playlist1.root.isVisible = state.isFirstVisible
            binding.playlist2.root.isVisible = state.isSecondVisible
            binding.playlist3.root.isVisible = state.isThirdVisible
            binding.playlist4.root.isVisible = state.isFourthVisible
            binding.playlist5.root.isVisible = state.isFifthVisible
            binding.playlist6.root.isVisible = state.isSixthVisible

            if (state.isFirstVisible) {
                binding.playlist1.ivPicture.load(state.first?.pictureUrl)
                binding.playlist1.tvTitle.text = state.first?.title
                binding.playlist1.indicatorMusicPlaying.isVisible = state.first?.isPlaying ?: false
            }

            if (state.isSecondVisible) {
                binding.playlist2.ivPicture.load(state.second?.pictureUrl)
                binding.playlist2.tvTitle.text = state.second?.title
                binding.playlist2.indicatorMusicPlaying.isVisible = state.second?.isPlaying ?: false
            }

            if (state.isThirdVisible) {
                binding.playlist3.ivPicture.load(state.third?.pictureUrl)
                binding.playlist3.tvTitle.text = state.third?.title
                binding.playlist3.indicatorMusicPlaying.isVisible = state.third?.isPlaying ?: false
            }

            if (state.isFourthVisible) {
                binding.playlist4.ivPicture.load(state.fourth?.pictureUrl)
                binding.playlist4.tvTitle.text = state.fourth?.title
                binding.playlist4.indicatorMusicPlaying.isVisible = state.fourth?.isPlaying ?: false
            }

            if (state.isFifthVisible) {
                binding.playlist5.ivPicture.load(state.fifth?.pictureUrl)
                binding.playlist5.tvTitle.text = state.fifth?.title
                binding.playlist5.indicatorMusicPlaying.isVisible = state.fifth?.isPlaying ?: false
            }

            if (state.isSixthVisible) {
                binding.playlist6.ivPicture.load(state.sixth?.pictureUrl)
                binding.playlist6.tvTitle.text = state.sixth?.title
                binding.playlist6.indicatorMusicPlaying.isVisible = state.sixth?.isPlaying ?: false
            }
        }
    }
}