package xyz.savvamirzoyan.musicplayer.feature_songs_list

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.featuremusicplayerservice.ui.MusicPlayerManager
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewModel
import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager
import javax.inject.Inject

@HiltViewModel
class SongsListViewModel @Inject constructor(
    private val musicPlayerManagerUseCase: UseCaseMusicPlayerManager,
    private val musicPlayerManager: MusicPlayerManager,
    private val songDomainToUiMapper: SongDomainToUiMapper
) : CoreViewModel() {

    private val _songsListFlow = MutableStateFlow<List<SongUi>>(emptyList())
    val songsListFlow: Flow<List<SongUi>> = _songsListFlow

    fun requestSongs(songsIds: List<StringID>) {
        viewModelScope.launch {
            whileLoading {
                songsIds
                    .map { musicPlayerManagerUseCase.getSong(it) }
                    .map { songDomainToUiMapper.map(it, ::playOrToggleSong) }
                    .also { _songsListFlow.emit(it) }
            }
        }
    }

    private fun playOrToggleSong(songId: StringID, albumId: StringID) {
        viewModelScope.launch {
            musicPlayerManagerUseCase.getSongs(songId, albumId)
                .also { songs -> musicPlayerManager.playOrToggleSong(songs) }
        }
    }
}
