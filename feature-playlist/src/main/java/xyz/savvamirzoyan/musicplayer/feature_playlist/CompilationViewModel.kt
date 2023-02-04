package xyz.savvamirzoyan.musicplayer.feature_playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewModel
import xyz.savvamirzoyan.musicplayer.appcore.uistate.TextValue
import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongCompilationDomain
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager
import kotlin.random.Random

class CompilationViewModel @AssistedInject constructor(
    @Assisted private val compilationId: StringID,
    private val musicPlayerManagerUseCase: UseCaseMusicPlayerManager
) : CoreViewModel() {

    private val _playlistInfoFlow = MutableStateFlow<PlaylistInfoUi?>(null)
    val playlistInfoFlow: Flow<PlaylistInfoUi> = _playlistInfoFlow.filterNotNull()

    private val _albumSongsFlow = MutableStateFlow<List<StringID>>(emptyList())
    val albumSongsFlow: Flow<List<StringID>> = _albumSongsFlow.filter { it.isNotEmpty() }

    private val _downloadAlbumProgress = MutableStateFlow(0)
    val downloadAlbumProgress: Flow<Int> = _downloadAlbumProgress
    val isDownloadAlbumProgressVisibleFlow: Flow<Boolean> = downloadAlbumProgress
        .map {
            if (it in 1 until 100) {
                true
            } else if (it >= 100) {
                delay(600)
                false
            } else false
        }

    private val _isAlbumLikedFlow = MutableStateFlow(false)
    val isAlbumLikedFlow: Flow<Boolean> = _isAlbumLikedFlow

    private val _isInShuffleMode = MutableStateFlow(false)
    val isInShuffleModeFlow: Flow<Boolean> = _isInShuffleMode

    val isPlaylistPlaying: Flow<Boolean> = combine(
        musicPlayerManagerUseCase.currentSongFlow,
        musicPlayerManagerUseCase.isPlayingFlow
    ) { currentSong/*, currentCompilation*/, isPlaying ->
        currentSong?.compilationId == compilationId && isPlaying
    }.distinctUntilChanged { old, new -> old == new }

    init {
        viewModelScope.launch {
            whileLoading {
                musicPlayerManagerUseCase.getCompilation(compilationId)
                    .also { compilation ->
                        when (compilation) {
                            is SongCompilationDomain.AlbumDomain -> PlaylistInfoUi(
                                coverUrl = compilation.coverPictureUrl,
                                albumTitle = TextValue(compilation.title),
                                albumDescription = TextValue(compilation.description ?: ""),
                                authorName = TextValue(compilation.authorName),
                                authorPictureUrl = compilation.authorPictureUrl ?: "",
                                briefInformation = TextValue(
                                    TextValue(R.plurals.fans_amount, compilation.fans, compilation.fans),
                                    TextValue(R.string.album_brief_info_separator),
                                    TextValue(
                                        R.plurals.tracks_amount,
                                        compilation.songs.count(),
                                        compilation.songs.count()
                                    )
                                )
                            ).also { _playlistInfoFlow.emit(it) }
                            is SongCompilationDomain.PlaylistDomain -> {}
                        }
                    }
                    .also { albumDomain ->
                        albumDomain.songs
                            .map { it.id }
                            .also { _albumSongsFlow.emit(it) }
                    }
            }
        }
    }

    fun onLikeClick() {
        viewModelScope.launch {
//            _isAlbumLikedFlow.emit(!_isAlbumLikedFlow.first())
        }
    }

    fun onDownloadAlbumClick() {
        viewModelScope.launch {
            var progress = 0

            while (progress <= 100) {
                val wait = Random.nextInt(5, 10) * 100L
                val pb = Random.nextInt(10, 20)
                progress += pb
                _downloadAlbumProgress.emit(progress)
                delay(wait)
            }
        }
    }

    fun onPlayButtonClick() {
        viewModelScope.launch {
            musicPlayerManagerUseCase.playCompilation(compilationId)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(compilationId: StringID): CompilationViewModel
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            factory: Factory,
            albumId: StringID
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = factory.create(albumId) as T
        }
    }
}