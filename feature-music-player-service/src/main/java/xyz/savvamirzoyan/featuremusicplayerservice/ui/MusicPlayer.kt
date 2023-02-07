package xyz.savvamirzoyan.featuremusicplayerservice.ui

import android.support.v4.media.session.PlaybackStateCompat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.featuremusicplayerservice.MusicServiceConnection
import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.RepeatModeDomain
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager
import javax.inject.Inject

// class to manage player from local UI (buttons, seekbars etc)

interface MusicPlayer {

    fun play()
    fun pause()
    fun togglePlay()
    fun skipForward()
    fun skipBackwards()
    fun toggleShuffle()
    fun toggleRepeatMode()
    fun seekToProgress(progress: Int)

    class Base @Inject constructor(
        private val musicPlayerManagerUseCase: UseCaseMusicPlayerManager,
        private val musicServiceConnection: MusicServiceConnection,
        private val scope: CoroutineScope
    ) : MusicPlayer {

        init {

            scope.launch {
                musicPlayerManagerUseCase.currentUserSelectedSongFlow.collect {
                    it?.also { playSong(it.id) }
                }
            }
        }

        private fun playSong(songID: StringID) {
            musicServiceConnection.transportControls.playFromMediaId(songID, null)
        }

        override fun play() {
            musicServiceConnection.transportControls.play()
        }

        override fun pause() {
            musicServiceConnection.transportControls.pause()
        }

        override fun togglePlay() {
            scope.launch {
                if (musicPlayerManagerUseCase.isPlayingFlow.firstOrNull() == true) pause() else play()
            }
        }

        override fun skipForward() {
            musicServiceConnection.transportControls.skipToNext()
        }

        override fun skipBackwards() {
            scope.launch {
                val progressPercent = (musicPlayerManagerUseCase.currentSongProgressFlow.firstOrNull() ?: 0) / 100.0
                val songDuration = musicPlayerManagerUseCase.currentSongDurationFlow.firstOrNull() ?: 0

                // if less then 3 seconds passed from the beginning of the song
                if (songDuration * progressPercent < 2) musicServiceConnection.transportControls.skipToPrevious()
                else musicServiceConnection.transportControls.seekTo(0)
            }
        }

        override fun toggleShuffle() {
            scope.launch {
                val isInShuffle = musicPlayerManagerUseCase.isInShuffleModeFlow.firstOrNull() ?: false

                if (isInShuffle) musicServiceConnection.transportControls.setShuffleMode(SHUFFLE_MODE_NONE)
                else musicServiceConnection.transportControls.setShuffleMode(SHUFFLE_MODE_GROUP)
            }
        }

        override fun toggleRepeatMode() {
            scope.launch {
                when (musicPlayerManagerUseCase.repeatModeFlow.firstOrNull() ?: RepeatModeDomain.NONE) {
                    RepeatModeDomain.NONE -> REPEAT_MODE_ALL
                    RepeatModeDomain.REPEAT -> REPEAT_MODE_ONE
                    RepeatModeDomain.REPEAT_ONCE -> REPEAT_MODE_NONE
                }.also { musicServiceConnection.transportControls.setRepeatMode(it) }
            }
        }

        override fun seekToProgress(progress: Int) {
            scope.launch {
                val duration = musicPlayerManagerUseCase.currentSongDurationFlow.firstOrNull() ?: 0
                val seekToInMilliSeconds = progress / 100.0 * duration * 1000
                musicServiceConnection.transportControls.seekTo(seekToInMilliSeconds.toLong())
            }
        }
    }
}