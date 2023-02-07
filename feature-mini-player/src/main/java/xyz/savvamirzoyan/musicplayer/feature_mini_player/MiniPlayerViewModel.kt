package xyz.savvamirzoyan.musicplayer.feature_mini_player

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.featuremusicplayerservice.MusicServiceConnection
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewModel
import xyz.savvamirzoyan.musicplayer.appcore.SongUi
import xyz.savvamirzoyan.musicplayer.appcore.mapper.SongDomainToUiMapper
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager
import javax.inject.Inject

@HiltViewModel
class MiniPlayerViewModel @Inject constructor(
    private val musicPlayerManager: UseCaseMusicPlayerManager,
    private val songDomainToUiMapper: SongDomainToUiMapper,
    private val musicServiceConnection: MusicServiceConnection
) : CoreViewModel() {

    val currentSongFlow: Flow<SongUi> = combine(
        musicPlayerManager.currentSongFlow.filterNotNull(),
        musicPlayerManager.isPlayingFlow
    ) { song, isPlaying -> songDomainToUiMapper.map(song, song.id, isPlaying) {} }

    val currentSongProgressFlow: Flow<Int> = musicPlayerManager.currentSongProgressFlow

    fun onButtonPlayClick() {
        viewModelScope.launch {
            if (musicPlayerManager.isPlayingFlow.firstOrNull() == true) musicServiceConnection.transportControls.pause()
            else musicServiceConnection.transportControls.play()
        }
    }
}
