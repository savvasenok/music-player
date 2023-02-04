package xyz.savvamirzoyan.musicplayer.feature_mini_player

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewModel
import xyz.savvamirzoyan.musicplayer.appcore.SongUi
import xyz.savvamirzoyan.musicplayer.appcore.uistate.TextValue
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager
import javax.inject.Inject

@HiltViewModel
class MiniPlayerViewModel @Inject constructor(
    private val musicPlayerManager: UseCaseMusicPlayerManager
) : CoreViewModel() {

    val currentSongFlow: Flow<SongUi> = combine(
        musicPlayerManager.currentSongFlow.filterNotNull(),
        musicPlayerManager.isPlayingFlow
    ) { song, isPlaying ->
        SongUi(
            id = song.id,
            albumId = song.albumId,
            title = TextValue(song.title),
            artist = TextValue(song.artist),
            albumPictureUrl = song.albumPictureUrl,
            isExplicit = song.isExplicit,
            onClickListener = {},
            isPlaying = isPlaying
        )
    }

    val currentSongProgressFlow: Flow<Int> = musicPlayerManager.currentSongProgressFlow

    fun onButtonPlayClick() {
        viewModelScope.launch {
            musicPlayerManager.playOrPause()
        }
    }
}
