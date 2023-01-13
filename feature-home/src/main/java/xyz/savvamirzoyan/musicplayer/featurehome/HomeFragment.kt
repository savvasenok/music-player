package xyz.savvamirzoyan.musicplayer.featurehome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import xyz.savvamirzoyan.featuremusicplayerservice.ui.MusicPlayerManager
import xyz.savvamirzoyan.musicplayer.appcore.CoreDiffUtilsGetter
import xyz.savvamirzoyan.musicplayer.appcore.CoreFragment
import xyz.savvamirzoyan.musicplayer.appcore.CoreRecyclerViewAdapter
import xyz.savvamirzoyan.musicplayer.appcore.load
import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.featurehome.databinding.FragmentHomeBinding
import xyz.savvamirzoyan.musicplayer.featurehome.model.SongFingerprint

@AndroidEntryPoint
class HomeFragment : CoreFragment<FragmentHomeBinding>() {

    private val adapter by lazy {
        CoreRecyclerViewAdapter(
            fingerprints = listOf(SongFingerprint()),
            diffUtilCallbackGetter = object : CoreDiffUtilsGetter {
                override fun get(old: List<Model.Ui>, new: List<Model.Ui>): DiffUtil.Callback =
                    object : DiffUtil.Callback() {
                        override fun getOldListSize() = old.size
                        override fun getNewListSize() = new.size
                        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = false
                        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = false
                    }
            }
        )
    }

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFlowListeners()
        setupLastSongsRecyclerView()
    }

    private fun setupFlowListeners() {

        binding.playlist1.root.setOnClickListener { }

        collect(viewModel.playlistsStateFlow) { state ->

            binding.playlist1.root.isVisible = state.isFirstVisible
            binding.playlist2.root.isVisible = state.isSecondVisible
            binding.playlist3.root.isVisible = state.isThirdVisible
            binding.playlist4.root.isVisible = state.isFourthVisible
            binding.playlist5.root.isVisible = state.isFifthVisible
            binding.playlist6.root.isVisible = state.isSixthVisible

            if (state.isFirstVisible) {
                binding.playlist1.ivPicture.load(state.first?.pictureUrl)
                binding.playlist1.tvTitle.text = state.first?.title?.get(requireContext())
                binding.playlist1.indicatorMusicPlaying.isVisible = state.first?.isPlaying ?: false
            }

            if (state.isSecondVisible) {
                binding.playlist2.ivPicture.load(state.second?.pictureUrl)
                binding.playlist2.tvTitle.text = state.second?.title?.get(requireContext())
                binding.playlist2.indicatorMusicPlaying.isVisible = state.second?.isPlaying ?: false
            }

            if (state.isThirdVisible) {
                binding.playlist3.ivPicture.load(state.third?.pictureUrl)
                binding.playlist3.tvTitle.text = state.third?.title?.get(requireContext())
                binding.playlist3.indicatorMusicPlaying.isVisible = state.third?.isPlaying ?: false
            }

            if (state.isFourthVisible) {
                binding.playlist4.ivPicture.load(state.fourth?.pictureUrl)
                binding.playlist4.tvTitle.text = state.fourth?.title?.get(requireContext())
                binding.playlist4.indicatorMusicPlaying.isVisible = state.fourth?.isPlaying ?: false
            }

            if (state.isFifthVisible) {
                binding.playlist5.ivPicture.load(state.fifth?.pictureUrl)
                binding.playlist5.tvTitle.text = state.fifth?.title?.get(requireContext())
                binding.playlist5.indicatorMusicPlaying.isVisible = state.fifth?.isPlaying ?: false
            }

            if (state.isSixthVisible) {
                binding.playlist6.ivPicture.load(state.sixth?.pictureUrl)
                binding.playlist6.tvTitle.text = state.sixth?.title?.get(requireContext())
                binding.playlist6.indicatorMusicPlaying.isVisible = state.sixth?.isPlaying ?: false
            }
        }
        collect(viewModel.toolbarGreetingTextFlow) {
            binding.tvToolbarGreetings.text = it.get(requireContext())
        }
        collect(viewModel.toolbarChipsStateFlow) {
            binding.chipClear.isVisible = it.isCancelChipVisible

            binding.chipMusic.isVisible = it.isMusicChipVisible
            binding.chipMusic.isSelected = it.isMusicSelected

            binding.chipPodcastsAndShows.isVisible = it.isPodcastsAndShowsChipVisible
            binding.chipPodcastsAndShows.isSelected = it.isPodcastsAndShowsChipSelected
        }
        collect(viewModel.lastPlayedSongsStateFlow) {
            adapter.update(it.songs)
        }
        collect(viewModel.isLastSongsSectionVisible) {
            binding.sectionLastPlayedSongs.isVisible = it
        }
    }

    private fun setupLastSongsRecyclerView() {
        binding.rvLastPlayedSongs.adapter = adapter
        binding.rvLastPlayedSongs.layoutManager = LinearLayoutManager(requireContext())
    }
}