package xyz.savvamirzoyan.musicplayer.feature_songs_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import xyz.savvamirzoyan.musicplayer.appcore.CoreDiffUtilsGetter
import xyz.savvamirzoyan.musicplayer.appcore.CoreFragment
import xyz.savvamirzoyan.musicplayer.appcore.CoreRecyclerViewAdapter
import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.feature_songs_list.databinding.FragmentSongsListBinding
import javax.inject.Inject

@AndroidEntryPoint
class SongsListFragment : CoreFragment<FragmentSongsListBinding>() {

    @Inject
    lateinit var viewModelFactory: SongsListViewModel.Factory
    private val viewModel by viewModels<SongsListViewModel>(factoryProducer = {
        SongsListViewModel.provideFactory(
            viewModelFactory,
            arguments?.getStringArrayList(KEY_SONGS_LIST) ?: listOf()
        )
    })
    private val adapter by lazy {
        CoreRecyclerViewAdapter(
            fingerprints = listOf(SongFingerprint()),
            diffUtilCallbackGetter = object : CoreDiffUtilsGetter<SongUi> {
                override fun get(old: List<SongUi>, new: List<SongUi>): DiffUtil.Callback =
                    object : DiffUtil.Callback() {
                        override fun getOldListSize() = old.size
                        override fun getNewListSize() = new.size
                        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                            old[oldItemPosition].id == new[newItemPosition].id

                        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                            (old[oldItemPosition] == new[newItemPosition])

                        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                            if (old[oldItemPosition].isPlaying != new[newItemPosition].isPlaying)
                                return SongPlayingStatusPayload(new[newItemPosition].isPlaying)
                            return null
                        }
                    }
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSongsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDefaultFlows(viewModel)
        setupLastSongsRecyclerView()
        setupFlowListeners()
    }

    private fun setupFlowListeners() {
        collect(viewModel.songsListFlow) { adapter.update(it) }
    }

    private fun setupLastSongsRecyclerView() {
        binding.root.adapter = adapter
        binding.root.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {

        private const val KEY_SONGS_LIST = "KEY_SONGS_LIST"

        fun newInstance(songs: List<StringID>) = SongsListFragment()
            .apply { arguments = bundleOf(KEY_SONGS_LIST to songs) }
    }
}