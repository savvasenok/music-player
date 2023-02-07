package xyz.savvamirzoyan.musicplayer.feature_fullscale_player

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import xyz.savvamirzoyan.featuremusicplayerservice.ui.MusicPlayer
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewModel
import xyz.savvamirzoyan.musicplayer.appcore.mapper.TimeToStringMapper
import xyz.savvamirzoyan.musicplayer.appcore.uistate.TextValue
import xyz.savvamirzoyan.musicplayer.core.PictureUrl
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongCompilationDomain
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.RepeatModeDomain
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager
import javax.inject.Inject

@HiltViewModel
class FullscalePlayerViewModel @Inject constructor(
    private val timeToStringMapper: TimeToStringMapper,
    private val musicPlayer: MusicPlayer,
    musicPlayerManager: UseCaseMusicPlayerManager
) : CoreViewModel() {

    val isPlayingFlow = musicPlayerManager.isPlayingFlow

    val toolbarTextFlow: Flow<ToolbarTextsUi> = musicPlayerManager.currentCompilationFlow
        .filterNotNull()
        .map {
            when (it) {
                is SongCompilationDomain.AlbumDomain -> ToolbarTextsUi(
                    title = TextValue(R.string.title_playing_from_album),
                    subtitle = TextValue(it.title)
                )
                is SongCompilationDomain.PlaylistDomain -> ToolbarTextsUi(
                    title = TextValue(R.string.title_playing_from_playlist),
                    subtitle = TextValue(it.title)
                )
            }
        }

    val pictureFlow: Flow<PictureUrl> = musicPlayerManager.currentCompilationFlow
        .filterNotNull()
        .map {
            when (it) {
                is SongCompilationDomain.AlbumDomain -> it.coverPictureUrl
                is SongCompilationDomain.PlaylistDomain -> it.coverPictureUrl
            }
        }

    val songTitleFlow: Flow<TextValue> = musicPlayerManager.currentSongFlow
        .filterNotNull()
        .map { TextValue(it.title) }

    val songArtistFlow: Flow<TextValue> = musicPlayerManager.currentSongFlow
        .filterNotNull()
        .map { TextValue(it.artist) }

    private val _isSongLikedFlow = MutableStateFlow(false)
    val isSongLikedFlow = _isSongLikedFlow

    val songProgressFlow = musicPlayerManager.currentSongProgressFlow

    val songProgressTextFlow: Flow<TextValue> = combine(
        songProgressFlow, musicPlayerManager.currentSongDurationFlow
    ) { progress, duration -> duration * progress / 100 }
        .map { TextValue(timeToStringMapper.map(it)) }

    val songDurationTextFlow: Flow<TextValue> = musicPlayerManager.currentSongDurationFlow
        .map { TextValue(timeToStringMapper.map(it)) }

    private val _subAction1IconIdFlow = MutableStateFlow(xyz.savvamirzoyan.musicplayer.appcore.R.drawable.ic_shuffle)
    val subAction1IconIdFlow: Flow<Int> = _subAction1IconIdFlow

    val isSubAction1SelectedFlow: Flow<Boolean> = musicPlayerManager.isInShuffleModeFlow

    val subAction2IconIdFlow: Flow<Int> = musicPlayerManager.repeatModeFlow
        .map {
            when (it) {
                RepeatModeDomain.NONE -> R.drawable.ic_repeat
                RepeatModeDomain.REPEAT -> R.drawable.ic_repeat
                RepeatModeDomain.REPEAT_ONCE -> R.drawable.ic_repeat_once
            }
        }

    val isSubAction2SelectedFlow: Flow<Boolean> = musicPlayerManager.repeatModeFlow
        .map { it != RepeatModeDomain.NONE }

    fun onPlayButtonClick() {
        musicPlayer.togglePlay()
    }

    fun onSkipForwardClick() {
        musicPlayer.skipForward()
    }

    fun onSkipBackClick() {
        musicPlayer.skipBackwards()
    }

    fun onSubAction1Click() {
        musicPlayer.toggleShuffle()
    }

    fun onSubAction2Click() {
        musicPlayer.toggleRepeatMode()
    }

    fun onMusicScrolled(progress: Int) {
        musicPlayer.seekToProgress(progress)
    }
}