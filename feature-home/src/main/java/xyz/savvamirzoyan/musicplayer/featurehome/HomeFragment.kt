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
import xyz.savvamirzoyan.musicplayer.appcore.setText
import xyz.savvamirzoyan.musicplayer.feature_songs_list.SongsListFragment
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

        setupFragment(binding.appbar, binding.toolbar)

        setupDefaultFlows(viewModel)
        setupFlowListeners()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.playlist1.root.setOnClickListener {
            viewModel.onCompilationClick(0)
        }

        binding.playlist2.root.setOnClickListener {
            viewModel.onCompilationClick(1)
        }

        binding.playlist3.root.setOnClickListener {
            viewModel.onCompilationClick(2)
        }

        binding.playlist4.root.setOnClickListener {
            viewModel.onCompilationClick(3)
        }

        binding.playlist5.root.setOnClickListener {
            viewModel.onCompilationClick(4)
        }

        binding.playlist6.root.setOnClickListener {
            viewModel.onCompilationClick(5)
        }
    }

    private fun setupFlowListeners() {

        collect(viewModel.lastPlayedCompilationsFlow) { state ->
            binding.playlist1.root.isVisible = state.isFirstVisible
            binding.playlist2.root.isVisible = state.isSecondVisible
            binding.playlist3.root.isVisible = state.isThirdVisible
            binding.playlist4.root.isVisible = state.isFourthVisible
            binding.playlist5.root.isVisible = state.isFifthVisible
            binding.playlist6.root.isVisible = state.isSixthVisible

            if (state.isFirstVisible) {
                binding.playlist1.playableImageView.image.load(state.first?.pictureUrl)
                binding.playlist1.playableImageView.setIsPlaying(state.first?.isPlaying ?: false)
                state.first?.title?.let { binding.playlist1.tvTitle.setText(it) }
            }

            if (state.isSecondVisible) {
                binding.playlist2.playableImageView.image.load(state.second?.pictureUrl)
                state.second?.title?.let { binding.playlist2.tvTitle.setText(it) }
                binding.playlist2.playableImageView.setIsPlaying(state.second?.isPlaying ?: false)
            }

            if (state.isThirdVisible) {
                binding.playlist3.playableImageView.image.load(state.third?.pictureUrl)
                state.third?.title?.let { binding.playlist3.tvTitle.setText(it) }
                binding.playlist3.playableImageView.setIsPlaying(state.third?.isPlaying ?: false)
            }

            if (state.isFourthVisible) {
                binding.playlist4.playableImageView.image.load(state.fourth?.pictureUrl)
                state.fourth?.title?.let { binding.playlist4.tvTitle.setText(it) }
                binding.playlist4.playableImageView.setIsPlaying(state.fourth?.isPlaying ?: false)
            }

            if (state.isFifthVisible) {
                binding.playlist5.playableImageView.image.load(state.fifth?.pictureUrl)
                state.fifth?.title?.let { binding.playlist5.tvTitle.setText(it) }
                binding.playlist5.playableImageView.setIsPlaying(state.fifth?.isPlaying ?: false)
            }

            if (state.isSixthVisible) {
                binding.playlist6.playableImageView.image.load(state.sixth?.pictureUrl)
                state.sixth?.title?.let { binding.playlist6.tvTitle.setText(it) }
                binding.playlist6.playableImageView.setIsPlaying(state.sixth?.isPlaying ?: false)
            }
        }

        collect(viewModel.toolbarGreetingTextFlow) {
            binding.tvToolbarGreetings.setText(it)
        }

        collect(viewModel.toolbarChipsStateFlow) {
            binding.chipClear.isVisible = it.isCancelChipVisible

            binding.chipMusic.isVisible = it.isMusicChipVisible
            binding.chipMusic.isSelected = it.isMusicSelected

            binding.chipPodcastsAndShows.isVisible = it.isPodcastsAndShowsChipVisible
            binding.chipPodcastsAndShows.isSelected = it.isPodcastsAndShowsChipSelected
        }

        collectStarted(viewModel.lastPlayedSongsStateFlow) {
            val fragment = SongsListFragment.newInstance(it)
            childFragmentManager.beginTransaction().add(binding.fragmentSongsList.id, fragment).commit()
        }

        collect(viewModel.isLastSongsSectionVisible) {
            binding.sectionLastPlayedSongs.isVisible = it
        }
    }
}