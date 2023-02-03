package xyz.savvamirzoyan.musicplayer.feature_playlist

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import xyz.savvamirzoyan.musicplayer.appcore.CoreFragment
import xyz.savvamirzoyan.musicplayer.appcore.load
import xyz.savvamirzoyan.musicplayer.appcore.setText
import xyz.savvamirzoyan.musicplayer.appcore.setTitle
import xyz.savvamirzoyan.musicplayer.feature_playlist.databinding.FragmentCompilationBinding
import xyz.savvamirzoyan.musicplayer.feature_songs_list.SongsListFragment
import javax.inject.Inject

@AndroidEntryPoint
class CompilationFragment : CoreFragment<FragmentCompilationBinding>() {

    @Inject
    lateinit var viewModelFactory: CompilationViewModel.Factory
    private val viewModel by viewModels<CompilationViewModel>(factoryProducer = {
        CompilationViewModel.provideFactory(
            viewModelFactory,
            arguments?.getString(KEY_PLAYLIST_ID) ?: throw IllegalArgumentException("No album id provided")
        )
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCompilationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFragment(binding.toolbar)
        setupFlowListeners()
        setupClickListeners()
    }

    private fun setupClickListeners() {

        binding.buttonDownload.setOnClickListener {
            viewModel.onDownloadAlbumClick()
        }

        binding.buttonPlay.setOnClickListener {
            viewModel.onPlayButtonClick()
        }

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setupFlowListeners() {

        setupDefaultFlows(viewModel)

        collect(viewModel.playlistInfoFlow) {
            binding.ivAlbumPicture.load(it.coverUrl)
            binding.ivAuthor.load(it.authorPictureUrl)
            binding.tvTitle.setText(it.albumTitle)
            binding.tvDescription.setText(it.albumDescription)
            binding.tvAuthor.setText(it.authorName)
            binding.tvBriefInfo.setText(it.briefInformation)
            binding.collapsingToolbar.setTitle(it.albumTitle)
        }

        collect(viewModel.albumSongsFlow) {
            val fragment = SongsListFragment.newInstance(it)
            childFragmentManager.beginTransaction().add(binding.fragmentSongsList.id, fragment).commit()
        }

        collect(viewModel.isDownloadAlbumProgressVisibleFlow) {
            binding.buttonDownload.isEnabled = !it

            when (it) {
                true -> binding.pbDownload.show()
                false -> binding.pbDownload.hide()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            collect(viewModel.downloadAlbumProgress) {
                binding.pbDownload.setProgress(it, true)
            }
        } else {
            collect(viewModel.downloadAlbumProgress) {
                binding.pbDownload.progress = it
            }
        }

        collect(viewModel.isAlbumLikedFlow) {
            binding.buttonLike.isChecked = it
            cancel()

            // setup listener only after value was changed
            binding.buttonLike.setOnClickListener {
                viewModel.onLikeClick()
            }
        }

        collect(viewModel.isInShuffleModeFlow) {
            binding.buttonShuffle.isChecked = it
            cancel()
        }

        collect(viewModel.isPlaylistPlaying) {
            binding.buttonPlay.toggle(it)
        }
    }

    companion object {
        private const val KEY_PLAYLIST_ID = "KEY_PLAYLIST_ID"
    }
}