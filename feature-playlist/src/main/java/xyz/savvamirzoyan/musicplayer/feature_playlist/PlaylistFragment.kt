package xyz.savvamirzoyan.musicplayer.feature_playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import xyz.savvamirzoyan.musicplayer.appcore.CoreFragment
import xyz.savvamirzoyan.musicplayer.appcore.load
import xyz.savvamirzoyan.musicplayer.appcore.setText
import xyz.savvamirzoyan.musicplayer.appcore.setTitle
import xyz.savvamirzoyan.musicplayer.feature_playlist.databinding.FragmentPlaylistBinding
import xyz.savvamirzoyan.musicplayer.feature_songs_list.SongsListFragment
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistFragment : CoreFragment<FragmentPlaylistBinding>() {

    @Inject
    lateinit var viewModelFactory: PlaylistViewModel.Factory
    private val viewModel by viewModels<PlaylistViewModel>(factoryProducer = {
        PlaylistViewModel.provideFactory(
            viewModelFactory,
            arguments?.getString(KEY_PLAYLIST_ID) ?: throw IllegalArgumentException("No album id provided")
        )
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFragment(binding.toolbar)
        setupFlowListeners()
    }

    private fun setupFlowListeners() {

        setupDefaultFlows(viewModel)

        collect(viewModel.playlistInfoFlow) {
            binding.ivAlbumPicture.load(it.coverUrl)
            binding.ivAuthor.load(it.authorPictureUrl)
            binding.tvTitle.setText(it.albumTitle)
            binding.tvDescription.setText(it.albumDescription)
            binding.tvAuthor.setText(it.authorName)
            binding.tvLikesAndDuration.setText(it.likesAndDuration)
            binding.collapsingToolbar.setTitle(it.albumTitle)
        }

        collect(viewModel.albumSongsFlow) {
            val fragment = SongsListFragment.newInstance(it)
            childFragmentManager.beginTransaction().add(binding.fragmentSongsList.id, fragment).commit()
        }
    }

    companion object {
        private const val KEY_PLAYLIST_ID = "KEY_PLAYLIST_ID"
    }
}