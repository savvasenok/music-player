package xyz.savvamirzoyan.musicplayer.feature_playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewModel
import xyz.savvamirzoyan.musicplayer.appcore.TextValue
import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager

class PlaylistViewModel @AssistedInject constructor(
    @Assisted private val albumId: StringID,
    private val musicPlayerManager: UseCaseMusicPlayerManager
) : CoreViewModel() {

    private val _playlistInfoFlow = MutableStateFlow<PlaylistInfoUi?>(null)
    val playlistInfoFlow: Flow<PlaylistInfoUi> = _playlistInfoFlow.filterNotNull()

    private val _albumSongsFlow = MutableStateFlow<List<StringID>>(emptyList())
    val albumSongsFlow: Flow<List<StringID>> = _albumSongsFlow.filter { it.isNotEmpty() }

    init {
        viewModelScope.launch {
            whileLoading {
                musicPlayerManager.getAlbum(albumId)
                    .also { albumDomain ->
                        PlaylistInfoUi(
                            coverUrl = albumDomain.coverPictureUrl,
                            albumTitle = TextValue(albumDomain.title),
                            albumDescription = TextValue(albumDomain.description ?: ""),
                            authorName = TextValue(albumDomain.authorName),
                            authorPictureUrl = albumDomain.authorPictureUrl ?: "",
                            likesAndDuration = TextValue("137623 likes • 42m")
                        ).also { _playlistInfoFlow.emit(it) }
                    }
                    .also { albumDomain ->
                        albumDomain.songs
                            .map { it.id }
                            .also { _albumSongsFlow.emit(it) }
                    }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(albumId: StringID): PlaylistViewModel
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