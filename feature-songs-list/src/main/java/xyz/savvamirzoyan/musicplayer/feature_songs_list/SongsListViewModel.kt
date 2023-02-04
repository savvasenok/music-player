package xyz.savvamirzoyan.musicplayer.feature_songs_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewModel
import xyz.savvamirzoyan.musicplayer.appcore.SongUi
import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager

class SongsListViewModel @AssistedInject constructor(
    private val musicPlayerManagerUseCase: UseCaseMusicPlayerManager,
    private val songDomainToUiMapper: SongDomainToUiMapper,
    @Assisted songsIds: List<StringID>
) : CoreViewModel() {

    init {
        viewModelScope.launch {
            whileLoading {
                songsIds
                    .map { musicPlayerManagerUseCase.getSong(it) }
                    .also { _songsListFlow.emit(it) }
            }
        }
    }

    private val _songsListFlow = MutableStateFlow<List<SongDomain>>(emptyList())
    val songsListFlow: Flow<List<SongUi>> = combine(
        _songsListFlow,
        musicPlayerManagerUseCase.currentSongFlow,
        musicPlayerManagerUseCase.isPlayingFlow
    ) { songs, currentSong, isPlaying ->
        songs.map { song -> songDomainToUiMapper.map(song, currentSong?.id, isPlaying, ::playOrToggleSong) }
    }

    private fun playOrToggleSong(songId: StringID) {
        viewModelScope.launch {
            musicPlayerManagerUseCase.play(songId)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(songsIds: List<StringID>): SongsListViewModel
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            factory: Factory,
            songsIds: List<StringID>
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = factory.create(songsIds) as T
        }
    }
}
